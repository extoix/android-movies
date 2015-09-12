package com.extoix.android.movies.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.extoix.android.movies.R;
import com.extoix.android.movies.model.MovieDetail;

public class MovieDetailFragment extends Fragment {

    static final String MOVIE_DETAIL = "DETAIL";
    private MovieDetail mMovieDetail;


    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }
}
