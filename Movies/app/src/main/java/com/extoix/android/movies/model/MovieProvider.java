package com.extoix.android.movies.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.extoix.android.movies.model.MovieContract.MovieEntry;
import com.extoix.android.movies.model.MovieContract.TrailerEntry;
import com.extoix.android.movies.model.MovieContract.ReviewEntry;

public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_MOVIE_ID = 101;
    static final int MOVIETRAILER_WITH_MOVIE_ID = 150;

    static final int TRAILER = 200;
    static final int TRAILER_WITH_MOVIE_ID = 201;

    static final int REVIEW = 300;
    static final int REVIEW_WITH_MOVIE_ID = 301;

    private static final SQLiteQueryBuilder sMovieTrailerByMovieIdQueryBuilder;

    static {
        sMovieTrailerByMovieIdQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //trailer INNER JOIN movie ON trailer.movie_key = movie._id
        sMovieTrailerByMovieIdQueryBuilder.setTables(TrailerEntry.TABLE_TRAILER
                + " INNER JOIN " + MovieEntry.TABLE_MOVIE
                + " ON " + TrailerEntry.TABLE_TRAILER + "." + TrailerEntry.MOVIE_KEY
                + " = " + MovieEntry.TABLE_MOVIE + "." + MovieEntry._ID);
    }

    //movie.id = ?
    private static final String sMovieIdSelection = MovieEntry.TABLE_MOVIE + "." + MovieEntry.ID + " = ? ";

    private Cursor getMovieTrailerByMovieId(Uri uri, String[] projection, String sortOrder) {
        String movieId = TrailerEntry.getMovieIdFromUri(uri);
        String[] selectionArgs = new String[]{movieId};

        Cursor movieTrailerCursor = sMovieTrailerByMovieIdQueryBuilder.query(
                mMovieDbHelper.getReadableDatabase(),
                projection,
                sMovieIdSelection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return movieTrailerCursor;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieEntry.TABLE_MOVIE, MOVIE);
        matcher.addURI(authority, MovieEntry.TABLE_MOVIE + "/*", MOVIE_WITH_MOVIE_ID);

        matcher.addURI(authority, TrailerEntry.TABLE_TRAILER, TRAILER);
        matcher.addURI(authority, TrailerEntry.TABLE_TRAILER + "/*", TRAILER_WITH_MOVIE_ID);

        matcher.addURI(authority, ReviewEntry.TABLE_REVIEW, REVIEW);
        matcher.addURI(authority, ReviewEntry.TABLE_REVIEW + "/*", REVIEW_WITH_MOVIE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());

        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:{
                return MovieEntry.CONTENT_DIR_TYPE;
            }
            case MOVIE_WITH_MOVIE_ID:{
                return MovieEntry.CONTENT_ITEM_TYPE;
            }
            case MOVIETRAILER_WITH_MOVIE_ID: {
                return TrailerEntry.CONTENT_ITEM_TYPE;
            }
            case TRAILER:{
                return TrailerEntry.CONTENT_DIR_TYPE;
            }
            case TRAILER_WITH_MOVIE_ID:{
                return TrailerEntry.CONTENT_ITEM_TYPE;
            }
            case REVIEW:{
                return ReviewEntry.CONTENT_DIR_TYPE;
            }
            case REVIEW_WITH_MOVIE_ID:{
                return ReviewEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch(sUriMatcher.match(uri)){
            case MOVIE:{
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_MOVIE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            }
            case MOVIE_WITH_MOVIE_ID:{
                cursor = mMovieDbHelper.getReadableDatabase().query(
                        MovieEntry.TABLE_MOVIE,
                        projection,
                        MovieEntry.ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
            }
            case MOVIETRAILER_WITH_MOVIE_ID: {
                cursor = getMovieTrailerByMovieId(uri, projection, sortOrder);
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
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
