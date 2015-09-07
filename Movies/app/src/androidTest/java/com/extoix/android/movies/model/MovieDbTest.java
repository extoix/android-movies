package com.extoix.android.movies.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class MovieDbTest extends AndroidTestCase {

    public void setUp() {
        deleteTheDatabase();
    }

    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    public void testCreateMovieDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);

        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());


        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());


        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );
        assertTrue("Error: Your database was created without both the location entry and weather entry tables", tableNameHashSet.isEmpty());


        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());


        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(MovieContract.MovieEntry._ID);
        locationColumnHashSet.add(MovieContract.MovieEntry.ID);
        locationColumnHashSet.add(MovieContract.MovieEntry.TITLE);
        locationColumnHashSet.add(MovieContract.MovieEntry.RELEASE_DATE);
        locationColumnHashSet.add(MovieContract.MovieEntry.VOTE_AVERAGE);
        locationColumnHashSet.add(MovieContract.MovieEntry.OVERVIEW);
        locationColumnHashSet.add(MovieContract.MovieEntry.POSTER_PATH);
        locationColumnHashSet.add(MovieContract.MovieEntry.POSTER_PATH_URL);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required location entry columns", locationColumnHashSet.isEmpty());


        db.close();
    }

    public void testMovieTable() {
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = MovieDbTestUtilities.createMovieTableTestValues();
        long movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        assertTrue(movieRowId != -1);


        Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());


        MovieDbTestUtilities.validateCurrentRecord("Error: Location Query Validation Failed", cursor, testValues);
        assertFalse("Error: More than one record returned from location query", cursor.moveToNext());


        cursor.close();
        db.close();
    }
}
