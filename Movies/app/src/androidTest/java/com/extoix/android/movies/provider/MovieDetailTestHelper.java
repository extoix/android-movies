package com.extoix.android.movies.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.extoix.android.movies.provider.MovieDetailContract;
import com.extoix.android.movies.provider.MovieDetailDbHelper;

import java.util.Map;
import java.util.Set;

public class MovieDetailTestHelper extends AndroidTestCase {
    static ContentValues createMovieDetailValues() {
        ContentValues testValues = new ContentValues();

        testValues.put(MovieDetailContract.MovieDetailEntry.COLUMN_ID, "76341");
        testValues.put(MovieDetailContract.MovieDetailEntry.COLUMN_TITLE, "Mad Max: Fury Road");
        testValues.put(MovieDetailContract.MovieDetailEntry.COLUMN_RELEASE_DATE, "2015-05-15");
        testValues.put(MovieDetailContract.MovieDetailEntry.COLUMN_VOTE_AVERAGE, "7.6");
        testValues.put(MovieDetailContract.MovieDetailEntry.COLUMN_OVERVIEW, "An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.");
        testValues.put(MovieDetailContract.MovieDetailEntry.COLUMN_POSTER_PATH, "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");
        testValues.put(MovieDetailContract.MovieDetailEntry.COLUMN_POSTER_PATH_URL, "http://image.tmdb.org/t/p/w184/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");

        return testValues;
    }

    static long insertMovieDetailvalues(Context context) {
        MovieDetailDbHelper dbHelper = new MovieDetailDbHelper(context);
        SQLiteDatabase sqlDb = dbHelper.getWritableDatabase();
        ContentValues testValues = createMovieDetailValues();

        long movieDetailRowId = sqlDb.insert(MovieDetailContract.MovieDetailEntry.TABLE_MOVIE_DETAIL, null, testValues);

        assertTrue("Error: Failure to insert Movie Detail Values", movieDetailRowId != -1);

        sqlDb.close();
        dbHelper.close();
        return movieDetailRowId;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for(Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, index == -1);

            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value '" + expectedValue + "'. " + error, expectedValue, valueCursor.getString(index));
        }
    }

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }
}
