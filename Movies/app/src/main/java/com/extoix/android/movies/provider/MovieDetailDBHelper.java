package com.extoix.android.movies.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.extoix.android.movies.provider.MovieDetailContract.MovieDetailEntry;

public class MovieDetailDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie_detail.db";

    public MovieDetailDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_DETAIL_TABLE =
                "CREATE TABLE " + MovieDetailEntry.TABLE_MOVIE_DETAIL + " (" +
                MovieDetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                MovieDetailEntry.COLUMN_ID + " TEXT UNIQUE NOT NULL, " +
                MovieDetailEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieDetailEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieDetailEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieDetailEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieDetailEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieDetailEntry.COLUMN_POSTER_PATH_URL + " TEXT NOT NULL, " +

                " UNIQUE (" + MovieDetailEntry.COLUMN_ID + ") ON CONFLICT REPLACE" +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDetailEntry.TABLE_MOVIE_DETAIL);
        onCreate(sqLiteDatabase);
    }
}
