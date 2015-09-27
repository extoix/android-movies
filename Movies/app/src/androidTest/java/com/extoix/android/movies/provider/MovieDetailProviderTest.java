package com.extoix.android.movies.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Build;
import android.test.AndroidTestCase;

import com.extoix.android.movies.provider.MovieDetailContract.MovieDetailEntry;

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

    public void testMovieDetailQuery() {
        MovieDetailTestUtilities.insertMovieDetailvalues(mContext);

        Cursor movieDetailCursor = mContext.getContentResolver().query(MovieDetailEntry.CONTENT_URI, null, null, null, null);
        ContentValues testValues = MovieDetailTestUtilities.createMovieDetailValues();

        MovieDetailTestUtilities.validateCursor("testMovieDetailQuery", movieDetailCursor, testValues);

        if(Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Movie Detail Query did not properly set NotificationUri", movieDetailCursor.getNotificationUri(), MovieDetailEntry.CONTENT_URI);
        }
    }

    public void testMovieDetailQueryWithMovieId() {
        MovieDetailTestUtilities.insertMovieDetailvalues(mContext);

        Cursor movieDetailCursor = mContext.getContentResolver().query(MovieDetailEntry.buildMovieDetailUri(TEST_MOVIE_ID), null, null, null, null);
        ContentValues testValues = MovieDetailTestUtilities.createMovieDetailValues();

        MovieDetailTestUtilities.validateCursor("testMovieDetailQueryWithMovieId", movieDetailCursor, testValues);

        if(Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: Movie Detail Query did not properly set NotificationUri", movieDetailCursor.getNotificationUri(), MovieDetailEntry.buildMovieDetailUri(TEST_MOVIE_ID));
        }
    }
}
