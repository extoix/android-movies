package com.extoix.android.movies.retrofit;

import com.extoix.android.movies.model.MovieDetail;

import java.util.ArrayList;

public class MovieDetailResult {

    ArrayList<MovieDetail> results;

    public ArrayList<MovieDetail> getResults() {
        return results;
    }

    public void setResults(ArrayList<MovieDetail> results) {
        this.results = results;
    }

}
