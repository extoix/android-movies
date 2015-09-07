package com.extoix.android.movies.model;

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
        locationColumnHashSet.add(MovieContract.MovieEntry.TITLE);
        locationColumnHashSet.add(MovieContract.MovieEntry.RELEASE_DATE);
        locationColumnHashSet.add(MovieContract.MovieEntry.VOTE_AVERAGE);
        locationColumnHashSet.add(MovieContract.MovieEntry.OVERVIEW);
        locationColumnHashSet.add(MovieContract.MovieEntry.POSER_PATH);
        locationColumnHashSet.add(MovieContract.MovieEntry.POSTER_PATH_URL);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required location entry columns", locationColumnHashSet.isEmpty());


        db.close();
    }

}
