package com.extoix.android.movies.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.extoix.android.movies.R;
import com.extoix.android.movies.adapter.MoviePosterAdapter;
import com.extoix.android.movies.model.MovieDetail;
import com.extoix.android.movies.retrofit.MovieDetailResult;
import com.extoix.android.movies.retrofit.TheMovieDB;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieFragment extends Fragment {
    private static final String LOG = MovieFragment.class.getSimpleName();
    
    private MoviePosterAdapter mMoviePosterAdapter;
    private ArrayList<MovieDetail> mMovieDetailList;

    public static final String MOVIE_DETAIL_LIST_KEY = "movieDetailList";

    public interface MovieCallback {
        public void onItemSelected(MovieDetail movieDetail);
    }

    public MovieFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        if(isOnline()) {
            updateMoviePosters();
        } else {
            Toast.makeText(getActivity(), "No network connection, please connect to a network and start the application", Toast.LENGTH_LONG).show();
        }
    }

    /*http://developer.android.com/training/basics/network-ops/managing.html*/
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_DETAIL_LIST_KEY, mMovieDetailList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey(MOVIE_DETAIL_LIST_KEY)) {
            mMovieDetailList = new ArrayList<>();
        } else {
            mMovieDetailList = savedInstanceState.getParcelableArrayList(MOVIE_DETAIL_LIST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        GridView moviePosterGridView = (GridView) rootView.findViewById(R.id.movie_poster_gridview);

        mMoviePosterAdapter = new MoviePosterAdapter(
            getActivity(),
            R.layout.movie_poster,
            R.id.movie_poster_imageview,
            mMovieDetailList
        );

        moviePosterGridView.setAdapter(mMoviePosterAdapter);

        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                MovieDetail movieDetail = mMoviePosterAdapter.getItem(position);
                ((MovieCallback) getActivity()).onItemSelected(movieDetail);
            }
        });

        return rootView;
    }

    private void updateMoviePosters() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortBy = sharedPreferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popularity));

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.themoviedb_api_url)).build();
        TheMovieDB theMovieDBService = restAdapter.create(TheMovieDB.class);

        // I put my themoviedb apikey in a string resource file and did not check it in
        // I also added the file which I named themoviedbAPIKey.xml to my .gitignore
        String apiKey = getString(R.string.themoviedb_api_key);

        theMovieDBService.retrieveMovieDetailResult(sortBy, apiKey, new Callback<MovieDetailResult>() {
            @Override
            public void success(MovieDetailResult movieDetailResult, Response response) {

                List<MovieDetail> movieDetailList = movieDetailResult.getResults();

                updatePosterPathURL(movieDetailList);

                mMoviePosterAdapter.clear();
                mMoviePosterAdapter.addAll(movieDetailResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG, "Error with updateMoviePosters during service CallBack", error);
            }
        });
    }

    private void updatePosterPathURL(List<MovieDetail> movieDetailList) {
        for (MovieDetail movieDetail : movieDetailList) {
            String posterPath = movieDetail.getPoster_path();
            String posterPathURL = buildPosterPathURL(posterPath);
            movieDetail.setPosterPathURL(posterPathURL);
        }
    }

    private String buildPosterPathURL(String posterPath) {
        StringBuilder moviePosterURLBuilder = new StringBuilder();
        moviePosterURLBuilder.append(getString(R.string.themoviedb_image_base_url));
        moviePosterURLBuilder.append(posterPath);
        return moviePosterURLBuilder.toString();
    }

}
