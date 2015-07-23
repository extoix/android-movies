package com.extoix.android.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private MoviePosterAdapter mMoviePosterAdapter;
    private ArrayList<MovieDetail> mMovieDetailList;
    public static final String MOVIE_DETAIL_LIST_KEY = "movieDetailList";

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        if(isOnline()) {
            updateMovies();
        } else {
            Toast.makeText(getActivity(), "No network connection, please connect to a network and start the application", Toast.LENGTH_LONG).show();
        }
    }

    /*http://developer.android.com/training/basics/network-ops/managing.html*/
    //todo look at the rest of the document to handle preference activity
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void updateMovies() {
        if(mMovieDetailList == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrderPreference = preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popularity));

            RetrieveMovieDetailsTask retrieveMovieDetailsTask = new RetrieveMovieDetailsTask();
            retrieveMovieDetailsTask.execute(sortOrderPreference);
        } else {
            mMoviePosterAdapter.addAll(mMovieDetailList);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_DETAIL_LIST_KEY, mMovieDetailList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(MOVIE_DETAIL_LIST_KEY)) {
            mMovieDetailList = savedInstanceState.getParcelableArrayList(MOVIE_DETAIL_LIST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView moviePosterGridView = (GridView) rootView.findViewById(R.id.movie_poster_gridview);

        mMoviePosterAdapter = new MoviePosterAdapter(
            getActivity(),
            R.layout.movie_poster,
            R.id.movie_poster_imageview,
            new ArrayList()
        );

        moviePosterGridView.setAdapter(mMoviePosterAdapter);

        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                MovieDetail movieDetail = mMoviePosterAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class).putExtra(Intent.EXTRA_TEXT, movieDetail);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class RetrieveMovieDetailsTask extends AsyncTask<String, Void, ArrayList<MovieDetail>> {
        private final String LOG = RetrieveMovieDetailsTask.class.getSimpleName();

        public final static String POSTER_PATH_BASE_URL = "http://image.tmdb.org/t/p";
        public final static String POSTER_PATH_SIZE_W184 = "w184";

        @Override
        protected ArrayList<MovieDetail> doInBackground(String... params) {

            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            String movieDetailJSONStr = null;

            try {
                // http://api.themoviedb.org for api information
                // add your own api key
                final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                // I put my themoviedb apikey in a string resource file and did not check it in
                // I also added the file which I named themoviedbAPIKey.xml to my .gitignore
                String apiKey = getString(R.string.themoviedb_api_key);

                Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream == null) {
                    return null;  // if the inputStream is empty there is nothing that can be done so exit
                }

                StringBuffer stringBuffer = new StringBuffer();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuffer.append(bufferedReader.readLine());
                if (stringBuffer.length() == 0) {
                    return null;  // if the stringBuffer contains nothing then there is no point in parsing so exit
                }

                movieDetailJSONStr = stringBuffer.toString();

            } catch(IOException e) {
                Log.e(LOG, "Error retrieving movie detail data", e);
                return null;  // if there was a failure in fetching data then exit
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (final IOException e) {
                        Log.e(LOG, "Error closing stream", e);
                    }
                }
            }

            ArrayList<MovieDetail> movieDetailList = createMovieDetailList(movieDetailJSONStr);

            return movieDetailList;
        }

        protected ArrayList<MovieDetail> createMovieDetailList(String movieDetailJSONStr) {
            try {
                final String NODE_RESULTS = "results";
                final String NODE_TITLE = "title";
                final String NODE_RELEASE_DATE = "release_date";
                final String NODE_VOTE_AVERAGE = "vote_average";
                final String NODE_OVERVIEW = "overview";
                final String NODE_POSTER_PATH = "poster_path";

                JSONObject movieDetailJSON = new JSONObject(movieDetailJSONStr);
                JSONArray resultsArray = movieDetailJSON.getJSONArray(NODE_RESULTS);

                int resultsCount = resultsArray.length();
                ArrayList<MovieDetail> movieDetailList = new ArrayList();

                for(int i = 0; i < resultsCount; i++) {
                    JSONObject result = resultsArray.getJSONObject(i);
                    String title = result.getString(NODE_TITLE);
                    String releaseDate = result.getString(NODE_RELEASE_DATE);
                    String voteAverage = result.getString(NODE_VOTE_AVERAGE);
                    String overview = result.getString(NODE_OVERVIEW);
                    String posterPath = result.getString(NODE_POSTER_PATH);

                    MovieDetail movieDetail = new MovieDetail();
                    movieDetail.setTitle(title);
                    movieDetail.setReleaseDate(releaseDate);
                    movieDetail.setVoteAverage(voteAverage);
                    movieDetail.setOverview(overview);
                    movieDetail.setPosterPath(posterPath);

                    String posterPathURL = buildPosterPathURL(posterPath);
                    movieDetail.setPosterPathURL(posterPathURL);

                    movieDetailList.add(movieDetail);
                }

                return movieDetailList;
            } catch (JSONException e) {
                Log.e(LOG, "Error with parsing and creating movie detail list", e);
                return null;  // if there is an error parsing the data
            }
        }

        private String buildPosterPathURL(String posterPath) {
            StringBuilder moviePosterURLBuilder = new StringBuilder();
            moviePosterURLBuilder.append(POSTER_PATH_BASE_URL);
            moviePosterURLBuilder.append('/');
            moviePosterURLBuilder.append(POSTER_PATH_SIZE_W184);
            moviePosterURLBuilder.append(posterPath);

            return moviePosterURLBuilder.toString();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieDetail> movieDetailList) {
            if (movieDetailList != null) {
                mMoviePosterAdapter.clear();
                mMoviePosterAdapter.addAll(movieDetailList);
                mMovieDetailList = movieDetailList;
            }
        }
    }
}
