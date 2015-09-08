package com.extoix.android.movies.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.extoix.android.movies.model.MovieContract.MovieEntry;
import com.extoix.android.movies.model.MovieContract.ReviewEntry;
import com.extoix.android.movies.model.MovieContract.TrailerEntry;

public class MovieProviderTest extends AndroidTestCase {

    private SQLiteDatabase mDb;
    private ContentResolver mContentResolver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        initializeSQLiteDatabase();
        deleteAllRecords();
        mContentResolver = mContext.getContentResolver();
    }

    private void initializeSQLiteDatabase() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        mDb = dbHelper.getWritableDatabase();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mDb.close();
    }

    private void deleteAllRecords() {
        mDb.delete(MovieEntry.TABLE_MOVIE, null, null);
        mDb.delete(TrailerEntry.TABLE_TRAILER, null, null);
        mDb.delete(ReviewEntry.TABLE_REVIEW, null, null);
    }

    public void testGetType() {

        // content://com.extoix.android.movies/movie
        String type = mContentResolver.getType(MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.extoix.android.movies/movie
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE", MovieEntry.CONTENT_DIR_TYPE, type);

        // content://com/extoix.android.movies/movie/76341
        type = mContentResolver.getType(MovieEntry.buildMovie(MovieDbTestUtilities.MOVIE_ID));
        // vnd.android.cursor.dir/com.extoix.android.movies/movie/76341
        assertEquals("Error: the MovieEntry CONTENT_URI with movie id should return MovieEntry.CONTENT_ITEM_TYPE", MovieEntry.CONTENT_ITEM_TYPE, type);
    }

    public void testMovieQuery() {
        ContentValues testValues = MovieDbTestUtilities.createMovieValues();
        long movieRowId = MovieDbTestUtilities.insertMovieValues(mContext);

        Cursor movieCursor = mContentResolver.query(MovieEntry.CONTENT_URI, null, null, null, null);
        MovieDbTestUtilities.validateCursor("testMovieQuery", movieCursor, testValues);
    }

    public void testMoveQueryWithMovieId() {
        MovieDbTestUtilities.insertMultipleMovieTrailerValues(mContext);

        ContentValues testValues = MovieDbTestUtilities.createMovieValues();
        Cursor movieCursor = mContentResolver.query(MovieEntry.buildMovie(MovieDbTestUtilities.MOVIE_ID), null, null, null, null);
        MovieDbTestUtilities.validateCursor("testMovieQuery", movieCursor, testValues);
    }

}
