package com.extoix.android.movies;

/**
 * Created by secure on 7/19/15.
 */
public class MovieDetail {
    public final static String POSTER_PATH_BASE_URL = "http://image.tmdb.org/t/p";
    public final static String POSTER_PATH_SIZE_W184 = "w184";

    private String posterPath;

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
