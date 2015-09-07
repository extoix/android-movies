package com.extoix.android.movies.model;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.extoix.android.movies.model.MovieContract.MovieEntry;
import com.extoix.android.movies.model.MovieContract.TrailerEntry;
import com.extoix.android.movies.model.MovieContract.ReviewEntry;

public class MovieProviderUriMatcherTest extends AndroidTestCase {

    private static final String MOVIE_ID= "76341";  //"WITNESS ME!!!

    UriMatcher testMatcher;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testMatcher = MovieProvider.buildUriMatcher();
    }

    // content://com.extoix.android.movies/movie"
    private static final Uri TEST_MOVIE_DIR = MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_MOVIE_ID_DIR = MovieEntry.buildMovie(MOVIE_ID);

    public void testBuildUriMatcher_movie() {
        assertEquals("Error: The MOVIE URI was matched incorrectly.", testMatcher.match(TEST_MOVIE_DIR), MovieProvider.MOVIE);
        assertEquals("Error: The MOVIE WITH MOVIE ID URI was matched incorrectly.",testMatcher.match(TEST_MOVIE_WITH_MOVIE_ID_DIR), MovieProvider.MOVIE_WITH_MOVIE_ID);
    }

    // content://com.extoix.android.movies/trailer
    private static final Uri TEST_TRAILER_DIR = TrailerEntry.CONTENT_URI;
    private static final Uri TEST_TRAILER_WITH_MOVIE_ID_DIR = TrailerEntry.buildTrailer(MOVIE_ID);

    public void testBuildUriMatcher_trailer() {
        assertEquals("Error: The TRAILER URI was matched incorrectly.", testMatcher.match(TEST_TRAILER_DIR), MovieProvider.TRAILER);
        assertEquals("Error: The TRAILER WITH MOVIE ID URI was matched incorrectly.",testMatcher.match(TEST_TRAILER_WITH_MOVIE_ID_DIR), MovieProvider.TRAILER_WITH_MOVIE_ID);
    }

    // content://com.extoix.android.movies/review
    private static final Uri TEST_REVIEW_DIR = ReviewEntry.CONTENT_URI;
    private static final Uri TEST_REVIEW_WITH_MOVIE_ID_DIR = ReviewEntry.buildReview(MOVIE_ID);

    public void testBuildUriMatcher_review() {
        assertEquals("Error: The TRAILER URI was matched incorrectly.", testMatcher.match(TEST_REVIEW_DIR), MovieProvider.REVIEW);
        assertEquals("Error: The TRAILER WITH MOVIE ID URI was matched incorrectly.",testMatcher.match(TEST_REVIEW_WITH_MOVIE_ID_DIR), MovieProvider.REVIEW_WITH_MOVIE_ID);
    }

}
