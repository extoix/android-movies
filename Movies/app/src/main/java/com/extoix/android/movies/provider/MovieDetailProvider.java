package com.extoix.android.movies.provider;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.extoix.android.movies.provider.MovieDetailContract.MovieDetailEntry;

public class MovieDetailProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDetailDbHelper mMovieDetailDbHelper;

    static final int MOVIE_DETAIL = 100;
    static final int MOVIE_DETAIL_WITH_MOVIE_ID = 101;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieDetailContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieDetailEntry.TABLE_MOVIE_DETAIL, MOVIE_DETAIL);
        matcher.addURI(authority, MovieDetailEntry.TABLE_MOVIE_DETAIL + "/*", MOVIE_DETAIL_WITH_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDetailDbHelper = new MovieDetailDbHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {

        switch (sUriMatcher.match(uri)) {
            case MOVIE_DETAIL_WITH_MOVIE_ID: {
                return MovieDetailEntry.CONTENT_ITEM_TYPE;
            }
            case MOVIE_DETAIL: {
                return MovieDetailEntry.CONTENT_DIR_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            // "movie_detail/*"
            case MOVIE_DETAIL_WITH_MOVIE_ID: {
                cursor = mMovieDetailDbHelper.getReadableDatabase().query(
                        MovieDetailEntry.TABLE_MOVIE_DETAIL,
                        projection,
                        MovieDetailEntry.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            }
            // "movie_detail"
            case MOVIE_DETAIL: {
                cursor = mMovieDetailDbHelper.getReadableDatabase().query(
                        MovieDetailEntry.TABLE_MOVIE_DETAIL,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase sqlDb = mMovieDetailDbHelper.getWritableDatabase();

        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case MOVIE_DETAIL: {
                long movieDetailRowId = sqlDb.insert(MovieDetailEntry.TABLE_MOVIE_DETAIL, null, values);
                if ( movieDetailRowId > 0 )
                    returnUri = MovieDetailEntry.buildMovieDetailUri(movieDetailRowId);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mMovieDetailDbHelper.close();
        super.shutdown();
    }
}
