package com.extoix.android.movies.model;

import android.net.Uri;
import android.test.AndroidTestCase;

public class MovieContractTest extends AndroidTestCase {

    private static final String MOVIE_ID = "76341";

    public void testBuildMovie() {
        Uri movieUri = MovieContract.MovieEntry.buildMovie(MOVIE_ID);
        assertNotNull("Error: Null Uri returned.", movieUri);
        assertEquals("Error: Movie not properly appended to the end of the Uri", MOVIE_ID, movieUri.getLastPathSegment());
        assertEquals("Error: Movie Uri doesn't match our expected result", movieUri.toString(),"content://com.extoix.android.movies/movie/76341");
    }

    public void testBuildMovieTrailer() {
        Uri trailerUri = MovieContract.TrailerEntry.buildTrailer(MOVIE_ID);
        assertNotNull("Error: Null Uri returned.", trailerUri);
        assertEquals("Error: Movie Trailer not properly appended to the end of the Uri", MOVIE_ID, trailerUri.getLastPathSegment());
        assertEquals("Error: Movie Trailer Uri doesn't match our expected result", trailerUri.toString(),"content://com.extoix.android.movies/trailer/76341");
    }

    public void testBuildMovieReview() {
        Uri reviewUri = MovieContract.ReviewEntry.buildReview(MOVIE_ID);
        assertNotNull("Error: Null Uri returned.", reviewUri);
        assertEquals("Error: Movie Review not properly appended to the end of the Uri", MOVIE_ID, reviewUri.getLastPathSegment());
        assertEquals("Error: Movie Review Uri doesn't match our expected result", reviewUri.toString(),"content://com.extoix.android.movies/review/76341");
    }
}
