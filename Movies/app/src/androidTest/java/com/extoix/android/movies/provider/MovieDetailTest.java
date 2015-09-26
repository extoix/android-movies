package com.extoix.android.movies.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.extoix.android.movies.provider.MovieDetailContract.MovieDetailEntry;

public class MovieDetailTest extends AndroidTestCase {

    private SQLiteDatabase mDb;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mContext.deleteDatabase(MovieDetailDBHelper.DATABASE_NAME);

        mDb = new MovieDetailDBHelper(this.mContext).getWritableDatabase();
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

        SQLiteDatabase db = new MovieDetailDBHelper(this.mContext).getWritableDatabase();
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

        MovieDetailDBHelper movieDetailDBHelper = new MovieDetailDBHelper(mContext);
        SQLiteDatabase db = movieDetailDBHelper.getWritableDatabase();

        ContentValues testValues = createMovieDetailValues();

        long movieDetailRowId = db.insert(MovieDetailEntry.TABLE_MOVIE_DETAIL, null, testValues);
        assertTrue(movieDetailRowId != -1);


        Cursor cursor = db.query(MovieDetailEntry.TABLE_MOVIE_DETAIL, null, null, null, null, null, null);
        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());


        validateCurrentRecord("Error: Movie Detail Query Validation Failed", cursor, testValues);
        assertFalse("Error: More than one record returned from location query", cursor.moveToNext());

        cursor.close();
    }

    static ContentValues createMovieDetailValues() {
        ContentValues testValues = new ContentValues();

        testValues.put(MovieDetailEntry.COLUMN_ID, "76341");
        testValues.put(MovieDetailEntry.COLUMN_TITLE, "Mad Max: Fury Road");
        testValues.put(MovieDetailEntry.COLUMN_RELEASE_DATE, "2015-05-15");
        testValues.put(MovieDetailEntry.COLUMN_VOTE_AVERAGE, "7.6");
        testValues.put(MovieDetailEntry.COLUMN_OVERVIEW, "An apocalyptic story set in the furthest reaches of our planet, in a stark desert landscape where humanity is broken, and most everyone is crazed fighting for the necessities of life. Within this world exist two rebels on the run who just might be able to restore order. There's Max, a man of action and a man of few words, who seeks peace of mind following the loss of his wife and child in the aftermath of the chaos. And Furiosa, a woman of action and a woman who believes her path to survival may be achieved if she can make it across the desert back to her childhood homeland.");
        testValues.put(MovieDetailEntry.COLUMN_POSTER_PATH, "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");
        testValues.put(MovieDetailEntry.COLUMN_POSTER_PATH_URL, "http://image.tmdb.org/t/p/w184/kqjL17yufvn9OVLyXYpvtyrFfak.jpg");

        return testValues;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int index = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, index == -1);

            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value '" + expectedValue + "'. " + error, expectedValue, valueCursor.getString(index));
        }
    }
}
