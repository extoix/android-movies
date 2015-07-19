package com.extoix.android.movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

        GridView moviePosterGridView = (GridView) rootView.findViewById(R.id.movie_poster_gridview);


        RetrieveMovieDetailsTask retrieveMovieDetailsTask = new RetrieveMovieDetailsTask();
        retrieveMovieDetailsTask.execute();


        MovieDetail movieDetail = new MovieDetail();
        movieDetail.setPosterPath("/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");

        List<MovieDetail> movieDetailList = new ArrayList();
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);
        movieDetailList.add(movieDetail);

        MoviePosterAdapter moviePosterAdapter = new MoviePosterAdapter(getActivity(),
                R.layout.movie_poster,
                R.id.movie_poster_imageview,
                movieDetailList
        );

        moviePosterGridView.setAdapter(moviePosterAdapter);

        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    public class RetrieveMovieDetailsTask extends AsyncTask<Void, Void, Void> {
        private final String LOG = RetrieveMovieDetailsTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {

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
                        .appendQueryParameter(SORT_BY_PARAM, "popularity.desc")
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

            return null;  // this will only happen if there was an error getting or parsing the data
        }
    }
}
