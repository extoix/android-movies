package com.extoix.android.movies.model;

import android.net.Uri;
import android.test.AndroidTestCase;

public class MovieContractTest extends AndroidTestCase {

    private static final String MOVIE_ID = "76341";

    public void testBuildMovieTrailer() {
        Uri locationUri = MovieContract.TrailerEntry.buildMovieTrailer(MOVIE_ID);
        assertNotNull("Error: Null Uri returned.", locationUri);
        assertEquals("Error: Movie Trailer not properly appended to the end of the Uri", MOVIE_ID, locationUri.getLastPathSegment());
        assertEquals("Error: Movie Trailer Uri doesn't match our expected result", locationUri.toString(),"content://com.extoix.android.movies/trailer/76341");
    }

    public void testBuildMovieReview() {
        Uri locationUri = MovieContract.ReviewEntry.buildMovieReview(MOVIE_ID);
        assertNotNull("Error: Null Uri returned.", locationUri);
        assertEquals("Error: Movie Review not properly appended to the end of the Uri", MOVIE_ID, locationUri.getLastPathSegment());
        assertEquals("Error: Movie Review Uri doesn't match our expected result", locationUri.toString(),"content://com.extoix.android.movies/review/76341");
    }
}
