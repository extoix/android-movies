package com.extoix.android.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setPosterPath("/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");

        List<MovieDetail> movieDetailList = new ArrayList();
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);

        MoviePosterAdapter moviePosterAdapter = new MoviePosterAdapter(
                getActivity(),
                R.layout.movie_poster,
                R.id.movie_poster_imageview,
                movieDetailList
        );

        GridView moviePosterGridView = (GridView) rootView.findViewById(R.id.movie_poster_gridview);
        moviePosterGridView.setAdapter(moviePosterAdapter);

        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
