package com.extoix.android.movies.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import com.extoix.android.movies.model.MovieContract.MovieEntry;
import com.extoix.android.movies.model.MovieContract.TrailerEntry;

import java.util.Map;
import java.util.Set;

public class MovieDbTestUtilities extends AndroidTestCase {
    static final String MOVIE_ID= "76341";  //"WITNESS ME!!!

    static ContentValues createMovieTableTestValues() {
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

    static ContentValues createTrailerValues(long movieRowId) {
        ContentValues testValues = new ContentValues();
        testValues.put(TrailerEntry.MOVIE_KEY, movieRowId);
        testValues.put(TrailerEntry.KEY, "YWNWi-ZWL3c");
        testValues.put(TrailerEntry.NAME, "Official Trailer #1");

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
}
