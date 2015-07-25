package com.extoix.android.movies;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface TheMovieDB {
    @GET("/3/discover/movie")
    void retrieveMovieDetailResult(@Query("sort_by") String sortBy, @Query("api_key") String apiKey, Callback<MovieDetailResult> callback);
}
