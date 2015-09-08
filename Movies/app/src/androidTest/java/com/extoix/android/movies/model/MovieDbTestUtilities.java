package com.extoix.android.movies.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.extoix.android.movies.model.MovieContract.MovieEntry;
import com.extoix.android.movies.model.MovieContract.TrailerEntry;
import com.extoix.android.movies.model.MovieContract.ReviewEntry;

import java.util.Map;
import java.util.Set;

public class MovieDbTestUtilities extends AndroidTestCase {
    static final String MOVIE_ID = "76341";  //"WITNESS ME!!!
    static final String MOVIE_ID2 = "211672";

    static ContentValues createMovieValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieEntry.ID, MOVIE_ID);
        testValues.put(MovieEntry.TITLE, "Mad Max: Fury Road");
        testValues.put(MovieEntry.RELEASE_DATE, "2015-05-15");
        testValues.put(MovieEntry.VOTE_AVERAGE, "7.7");
        testValues.put(MovieEntry.OVERVIEW, "An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.");
        testValues.put(MovieEntry.POSTER_PATH, "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");
        testValues.put(MovieEntry.POSTER_PATH_URL, "http://image.tmdb.org/t/p/w184/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");

        return testValues;
    }

    static ContentValues createMovieValues2() {
        ContentValues testValues = new ContentValues();
        testValues.put(MovieEntry.ID, MOVIE_ID2);
        testValues.put(MovieEntry.TITLE, "Minions");
        testValues.put(MovieEntry.RELEASE_DATE, "2015-06-25");
        testValues.put(MovieEntry.VOTE_AVERAGE, "6.8");
        testValues.put(MovieEntry.OVERVIEW, "Minions Stuart, Kevin and Bob are recruited by Scarlet Overkill, a super-villain who, alongside her inventor husband Herb, hatches a plot to take over the world.");
        testValues.put(MovieEntry.POSTER_PATH, "/s5uMY8ooGRZOL0oe4sIvnlTsYQO.jpg");
        testValues.put(MovieEntry.POSTER_PATH_URL, "http://image.tmdb.org/t/p/w184/s5uMY8ooGRZOL0oe4sIvnlTsYQO.jpg");

        return testValues;
    }

    static ContentValues createTrailerValues(long movieRowId) {
        ContentValues testValues = new ContentValues();
        testValues.put(TrailerEntry.MOVIE_KEY, movieRowId);
        testValues.put(TrailerEntry.ID, "548ce4e292514122ed002d99");
        testValues.put(TrailerEntry.KEY, "YWNWi-ZWL3c");
        testValues.put(TrailerEntry.NAME, "Official Trailer #1");

        return testValues;
    }

    static ContentValues createReviewValues(long movieRowId) {
        ContentValues testValues = new ContentValues();
        testValues.put(ReviewEntry.MOVIE_KEY, movieRowId);
        testValues.put(ReviewEntry.ID, "55660928c3a3687ad7001db1");
        testValues.put(ReviewEntry.AUTHOR, "Phileas Fogg");
        testValues.put(ReviewEntry.CONTENT, "Fabulous action movie. Lots of interesting characters. They don't make many movies like this. The whole movie from start to finish was entertaining I'm looking forward to seeing it again. I definitely recommend seeing it.");

        return testValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);

            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value '" + expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static long insertMovieValues(Context context) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = createMovieValues();

        long movieRowId = db.insert(MovieEntry.TABLE_MOVIE, null, testValues);
        assertTrue("Error: Failure to insert Movie Values", movieRowId != -1);

        db.close();
        return movieRowId;
    }

    static void insertMultipleMovieTrailerValues(Context context) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movieValues = createMovieValues();
        long movieRowId = db.insert(MovieEntry.TABLE_MOVIE, null, movieValues);
        assertTrue("Error: Failure to insert Movie Values", movieRowId != -1);

        ContentValues trailerValues = createTrailerValues(movieRowId);
        long trailerRowId = db.insert(TrailerEntry.TABLE_TRAILER, null, trailerValues);
        assertTrue("Error: Failure to insert Trailer Values", trailerRowId != -1);

        ContentValues movieValues2 = createMovieValues2();
        long movieRowId2 = db.insert(MovieEntry.TABLE_MOVIE, null, movieValues2);
        assertTrue("Error: Failure to insert Movie Values", movieRowId2 != -1);

        db.close();
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

}
