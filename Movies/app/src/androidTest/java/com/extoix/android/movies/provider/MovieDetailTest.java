package com.extoix.android.movies.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import com.extoix.android.movies.provider.MovieDetailContract.MovieDetailEntry;

public class MovieDetailTest extends AndroidTestCase {

    private SQLiteDatabase mDb;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mContext.deleteDatabase(MovieDetailDbHelper.DATABASE_NAME);

        mDb = new MovieDetailDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, mDb.isOpen());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        mDb.close();
    }

    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieDetailEntry.TABLE_MOVIE_DETAIL);

        SQLiteDatabase db = new MovieDetailDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());


        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error:  The database has not been created correctly", cursor.moveToFirst());


        do {
            tableNameHashSet.remove(cursor.getString(0));
        } while( cursor.moveToNext() );
        assertTrue("Error:  Your database was created without the movie details table", tableNameHashSet.isEmpty());


        cursor = db.rawQuery("PRAGMA table_info(" + MovieDetailEntry.TABLE_MOVIE_DETAIL + ")", null);
        assertTrue("Error:  Unable to query the database for table information.", cursor.moveToFirst());


        final HashSet<String> movieDetailColumnHashSet = new HashSet<String>();
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_ID);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_TITLE);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_RELEASE_DATE);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_VOTE_AVERAGE);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_OVERVIEW);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_POSTER_PATH);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_POSTER_PATH_URL);

        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            movieDetailColumnHashSet.remove(columnName);
        } while(cursor.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required location entry columns", movieDetailColumnHashSet.isEmpty());

        cursor.close();
    }

    public void testMovieDetailTable() {

        MovieDetailDbHelper movieDetailDbHelper = new MovieDetailDbHelper(mContext);
        SQLiteDatabase db = movieDetailDbHelper.getWritableDatabase();

        ContentValues testValues = MovieDetailTestHelper.createMovieDetailValues();

        long movieDetailRowId = db.insert(MovieDetailEntry.TABLE_MOVIE_DETAIL, null, testValues);
        assertTrue(movieDetailRowId != -1);


        Cursor cursor = db.query(MovieDetailEntry.TABLE_MOVIE_DETAIL, null, null, null, null, null, null);
        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());


        MovieDetailTestHelper.validateCurrentRecord("Error: Movie Detail Query Validation Failed", cursor, testValues);
        assertFalse("Error: More than one record returned from location query", cursor.moveToNext());

        cursor.close();
    }
}
