package com.extoix.android.movies.provider;

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


        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error:  The database has not been created correctly", c.moveToFirst());


        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );
        assertTrue("Error:  Your database was created without the movie details table", tableNameHashSet.isEmpty());


        c = db.rawQuery("PRAGMA table_info(" + MovieDetailEntry.TABLE_MOVIE_DETAIL + ")", null);
        assertTrue("Error:  Unable to query the database for table information.", c.moveToFirst());


        final HashSet<String> movieDetailColumnHashSet = new HashSet<String>();
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_ID);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_TITLE);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_RELEASE_DATE);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_VOTE_AVERAGE);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_OVERVIEW);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_POSTER_PATH);
        movieDetailColumnHashSet.add(MovieDetailEntry.COLUMN_POSTER_PATH_URL);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieDetailColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required location entry columns", movieDetailColumnHashSet.isEmpty());
    }
}
