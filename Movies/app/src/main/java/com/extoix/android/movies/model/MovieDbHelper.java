package com.extoix.android.movies.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "
                + MovieContract.MovieEntry.TABLE_NAME
                + " ("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY,"
                + MovieContract.MovieEntry.TITLE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.VOTE_AVERAGE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.POSER_PATH + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.POSTER_PATH_URL + " TEXT NOT NULL, "
                +" );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);

        onCreate(db);
    }
}
