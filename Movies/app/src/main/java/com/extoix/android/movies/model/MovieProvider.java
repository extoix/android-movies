package com.extoix.android.movies.model;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    static final int MOVIE = 100;
    static final int MOVIE_WITH_MOVIE_ID = 101;

    static final int TRAILER = 200;
    static final int TRAILER_WITH_MOVIE_ID = 201;

    static final int REVIEW = 300;
    static final int REVIEW_WITH_MOVIE_ID = 301;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.MovieEntry.TABLE_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.MovieEntry.TABLE_MOVIE + "/*", MOVIE_WITH_MOVIE_ID);

        matcher.addURI(authority, MovieContract.TrailerEntry.TABLE_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.TrailerEntry.TABLE_TRAILER + "/*", TRAILER_WITH_MOVIE_ID);

        matcher.addURI(authority, MovieContract.ReviewEntry.TABLE_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.ReviewEntry.TABLE_REVIEW + "/*", REVIEW_WITH_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
