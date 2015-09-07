package com.extoix.android.movies.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.extoix.android.movies.model.MovieContract.TrailerEntry;

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
        assertTrue("Error: Your database was created without all entry tables", tableNameHashSet.isEmpty());


        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());


        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.TITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.RELEASE_DATE);
        movieColumnHashSet.add(MovieContract.MovieEntry.VOTE_AVERAGE);
        movieColumnHashSet.add(MovieContract.MovieEntry.OVERVIEW);
        movieColumnHashSet.add(MovieContract.MovieEntry.POSTER_PATH);
        movieColumnHashSet.add(MovieContract.MovieEntry.POSTER_PATH_URL);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());
        assertTrue("Error: The database doesn't contain all of the required movie entry columns", movieColumnHashSet.isEmpty());


        db.close();
    }

    public void testMovieTable() {
        insertMovie();
    }

    public long insertMovie() {

        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = MovieDbTestUtilities.createMovieTableTestValues();
        long movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        assertTrue(movieRowId != -1);


        Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue("Error: No Records returned from movie query", cursor.moveToFirst());


        MovieDbTestUtilities.validateCurrentRecord("Error: Movie Query Validation Failed", cursor, testValues);
        assertFalse("Error: More than one record returned from movie query", cursor.moveToNext());


        cursor.close();
        db.close();
        return movieRowId;
    }

    public void testTrailerTable() {
        long movieRowId = insertMovie();
        assertFalse("Error: Movie Not Inserted Correctly", movieRowId == -1L);


        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues trailerValues = MovieDbTestUtilities.createTrailerValues(movieRowId);
        long trailerRowId = db.insert(TrailerEntry.TABLE_NAME, null, trailerValues);
        assertTrue(trailerRowId != -1);


        Cursor trailerCursor = db.query(TrailerEntry.TABLE_NAME, null, null, null, null, null, null);
        assertTrue( "Error: No Records returned from trailer query", trailerCursor.moveToFirst() );


        MovieDbTestUtilities.validateCurrentRecord("testInsertReadDb trailerEntry failed to validate", trailerCursor, trailerValues);
        assertFalse("Error: More than one record returned from trailer query", trailerCursor.moveToNext());


        trailerCursor.close();
        dbHelper.close();
    }
}
