package com.extoix.android.movies.model;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.extoix.android.movies.model.MovieContract.MovieEntry;
import com.extoix.android.movies.model.MovieContract.ReviewEntry;
import com.extoix.android.movies.model.MovieContract.TrailerEntry;

public class MovieProviderTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    private void deleteAllRecords() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(MovieEntry.TABLE_MOVIE, null, null);
        db.delete(TrailerEntry.TABLE_TRAILER, null, null);
        db.delete(ReviewEntry.TABLE_REVIEW, null, null);

        db.close();
    }

    public void testGetType() {

        ContentResolver contentResolver = mContext.getContentResolver();

        // content://com.extoix.android.movies/movie
        String type = contentResolver.getType(MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.extoix.android.movies/movie
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE", MovieEntry.CONTENT_DIR_TYPE, type);

        // content://com/extoix.android.movies/movie/76341
        String testMovieId = "76341";
        type = contentResolver.getType(MovieEntry.buildMovie(testMovieId));
        // vnd.android.cursor.dir/com.extoix.android.movies/movie/76341
        assertEquals("Error: the MovieEntry CONTENT_URI with movie id should return MovieEntry.CONTENT_ITEM_TYPE", MovieEntry.CONTENT_ITEM_TYPE, type);
    }
}
