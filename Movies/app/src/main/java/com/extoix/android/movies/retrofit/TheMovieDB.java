package com.extoix.android.movies.retrofit;

import com.extoix.android.movies.model.MovieDetail;
import com.extoix.android.movies.model.MovieDetailResult;
import com.extoix.android.movies.model.ReviewDetailResult;
import com.extoix.android.movies.model.TrailerDetailResult;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface TheMovieDB {
    @GET("/3/discover/movie")
    void retrieveMovieDetailResult(@Query("sort_by") String sortBy, @Query("api_key") String apiKey, Callback<MovieDetailResult> callback);

    @GET("/3/movie/{movieId}")
    void retrieveMovieDetail(@Path("movieId") String movieId, @Query("api_key") String apiKey, Callback<MovieDetail> callback);

    @GET("/3/movie/{movieId}/videos")
    void retrieveMovieTrailerResult(@Path("movieId") String movieId, @Query("api_key") String apiKey, Callback<TrailerDetailResult> callback);

    @GET("/3/movie/{movieId}/reviews")
    void retrieveMovieReviewResult(@Path("movieId") String movieId, @Query("api_key") String apiKey, Callback<ReviewDetailResult> callback);
}
