package com.extoix.android.movies.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.extoix.android.movies.model.MovieContract.MovieEntry;
import com.extoix.android.movies.model.MovieContract.TrailerEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "
                + MovieEntry.TABLE_NAME
                + " ("
                + MovieEntry._ID + " INTEGER PRIMARY KEY, "
                + MovieEntry.ID + " TEXT UNIQUE NOT NULL, "
                + MovieEntry.TITLE + " TEXT NOT NULL, "
                + MovieEntry.RELEASE_DATE + " TEXT NOT NULL, "
                + MovieEntry.VOTE_AVERAGE + " TEXT NOT NULL, "
                + MovieEntry.OVERVIEW + " TEXT NOT NULL, "
                + MovieEntry.POSTER_PATH + " TEXT NOT NULL, "
                + MovieEntry.POSTER_PATH_URL + " TEXT NOT NULL"
                +" );";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE "
                + TrailerEntry.TABLE_NAME
                + " ("
                + TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TrailerEntry.MOVIE_KEY + " INTEGER NOT NULL, "
                + TrailerEntry.KEY + " TEXT NOT NULL, "
                + TrailerEntry.NAME + " TEXT NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + TrailerEntry.MOVIE_KEY+ ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + TrailerEntry.KEY + ", " +
                TrailerEntry.MOVIE_KEY + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);

        onCreate(db);
    }
}