package com.extoix.android.movies.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;

import com.extoix.android.movies.provider.MovieDetailContract.MovieDetailEntry;
import com.extoix.android.movies.utility.ContentObserver;

public class MovieDetailProviderTest extends AndroidTestCase {

    private static final String TEST_MOVIE_ID = "76341";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext.deleteDatabase(MovieDetailDbHelper.DATABASE_NAME);
    }

    public void testGetType() {
        // content://com.extoix.android.movies/movie_detail"
        String typeDir = mContext.getContentResolver().getType(MovieDetailEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.extoix.android.movies/movie_detail
        assertEquals("Error: the MovieDetailEntry CONTENT_URI should return MovieDetailEntry.CONTENT_DIR_TYPE", MovieDetailEntry.CONTENT_DIR_TYPE, typeDir);

        // content://com.extoix.android.movies/movie_detail/76341"
        String typeItem = mContext.getContentResolver().getType(MovieDetailEntry.buildMovieDetailUri(TEST_MOVIE_ID));
        // vnd.android.cursor.dir/com.extoix.android.movies/movie_detail/76341
        assertEquals("Error: the MovieDetailEntry CONTENT_URI should return MovieDetailEntry.CONTENT_ITEM_TYPE", MovieDetailEntry.CONTENT_ITEM_TYPE, typeItem);
    }

    public void testQuery() {
        MovieDetailTestHelper.insertMovieDetailvalues(mContext);

        Cursor movieDetailCursor = mContext.getContentResolver().query(MovieDetailEntry.CONTENT_URI, null, null, null, null);
        ContentValues testValues = MovieDetailTestHelper.createMovieDetailValues();

        MovieDetailTestHelper.validateCursor("testQuery", movieDetailCursor, testValues);

        if(Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Movie Detail Query did not properly set NotificationUri", movieDetailCursor.getNotificationUri(), MovieDetailEntry.CONTENT_URI);
        }
    }

    public void testQueryWithMovieId() {
        MovieDetailTestHelper.insertMovieDetailvalues(mContext);

        Cursor movieDetailCursor = mContext.getContentResolver().query(MovieDetailEntry.buildMovieDetailUri(TEST_MOVIE_ID), null, null, null, null);
        ContentValues testValues = MovieDetailTestHelper.createMovieDetailValues();

        MovieDetailTestHelper.validateCursor("testQueryWithMovieId", movieDetailCursor, testValues);

        if(Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Movie Detail Query did not properly set NotificationUri", movieDetailCursor.getNotificationUri(), MovieDetailEntry.buildMovieDetailUri(TEST_MOVIE_ID));
        }
    }

    /**
     * Test performs insert of a test record, but also a query to seek out the inserted record
     */
    public void testInsert() {
        //Insert a movie detail record
        ContentValues movieDetailValues = MovieDetailTestHelper.createMovieDetailValues();

        ContentObserver contentObserver = ContentObserver.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieDetailEntry.CONTENT_URI, true, contentObserver);
        Uri movieDetailUri = mContext.getContentResolver().insert(MovieDetailEntry.CONTENT_URI, movieDetailValues);

        //Check to see if observer received notification from provider
        contentObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(contentObserver);

        long movieDetailRowId = ContentUris.parseId(movieDetailUri);
        assertTrue(movieDetailRowId != -1);

        //Query for movie detail record
        Cursor movieDetailCursor = mContext.getContentResolver().query(MovieDetailEntry.CONTENT_URI, null, null, null, null);
        MovieDetailTestHelper.validateCursor("testInsert. Error validating MovieDetailEntry.", movieDetailCursor, movieDetailValues);

        //Check to see if observer received notification from provider
        contentObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(contentObserver);
    }

    public void testDelete() {
        testInsert();

        ContentObserver contentObserver = ContentObserver.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieDetailEntry.CONTENT_URI, true, contentObserver);

        mContext.getContentResolver().delete(MovieDetailEntry.CONTENT_URI, null, null);

        Cursor cursor = mContext.getContentResolver().query(MovieDetailEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: Records not deleted from Movie Detail table during delete", 0, cursor.getCount());
        cursor.close();

        contentObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(contentObserver);
    }

    public void testUpdate() {
        // create movie detail data to add to the database
        ContentValues movieDetailValues = MovieDetailTestHelper.createMovieDetailValues();

        Uri movieDetailUri = mContext.getContentResolver().insert(MovieDetailEntry.CONTENT_URI, movieDetailValues);
        long movieDetailRowId = ContentUris.parseId(movieDetailUri);
        assertTrue(movieDetailRowId != -1);

        // update a piece of data in the ContentValues data container
        ContentValues updatedMovieDetailValues = new ContentValues(movieDetailValues);
        updatedMovieDetailValues.put(MovieDetailEntry._ID, movieDetailRowId);
        updatedMovieDetailValues.put(MovieDetailEntry.COLUMN_OVERVIEW, "Testing new overview text");

        // get the cursor of the record in the database
        Cursor movieDetailCursor = mContext.getContentResolver().query(MovieDetailEntry.CONTENT_URI, null, null, null, null);

        // perform the update of the record in the database
        ContentObserver contentObserver = ContentObserver.getTestContentObserver();
        movieDetailCursor.registerContentObserver(contentObserver);

        int count = mContext.getContentResolver().update(MovieDetailEntry.CONTENT_URI, updatedMovieDetailValues, MovieDetailEntry._ID + "= ?", new String[]{Long.toString(movieDetailRowId)});
        assertEquals(count, 1);

        contentObserver.waitForNotificationOrFail();
        movieDetailCursor.unregisterContentObserver(contentObserver);

        // query for the piece of data and make sure it changed to the updated content values
        movieDetailCursor = mContext.getContentResolver().query(MovieDetailEntry.CONTENT_URI, null, MovieDetailEntry._ID + " = " + movieDetailRowId, null, null);
        MovieDetailTestHelper.validateCursor("testUpdate.  Error validating movie entry update.", movieDetailCursor, updatedMovieDetailValues);

        movieDetailCursor.close();
    }

}
