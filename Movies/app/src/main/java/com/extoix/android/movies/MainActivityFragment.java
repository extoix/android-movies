package com.extoix.android.movies;

import android.content.Intent;
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
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    MoviePosterAdapter mMoviePosterAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final GridView moviePosterGridView = (GridView) rootView.findViewById(R.id.movie_poster_gridview);

        final RetrieveMovieDetailsTask retrieveMovieDetailsTask = new RetrieveMovieDetailsTask();
        retrieveMovieDetailsTask.execute();

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

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(movieDetail.getTitle());
                stringBuilder.append('\n');
                stringBuilder.append(movieDetail.getReleaseDate());
                stringBuilder.append('\n');
                stringBuilder.append(movieDetail.getPosterPath());
                stringBuilder.append('\n');
                stringBuilder.append(movieDetail.getVoteAverage());
                stringBuilder.append('\n');
                stringBuilder.append(movieDetail.getOverview());
                stringBuilder.append('\n');

                Toast.makeText(getActivity(), stringBuilder.toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), MovieDetailActivity.class).putExtra(Intent.EXTRA_TEXT, movieDetail);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class RetrieveMovieDetailsTask extends AsyncTask<Void, Void, List<MovieDetail>> {
        private final String LOG = RetrieveMovieDetailsTask.class.getSimpleName();

        @Override
        protected List<MovieDetail> doInBackground(Void... params) {

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

            List<MovieDetail> movieDetailList = createMovieDetailList(movieDetailJSONStr);

            return movieDetailList;
        }

        protected List<MovieDetail> createMovieDetailList(String movieDetailJSONStr) {
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
                List<MovieDetail> movieDetailList = new ArrayList();

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

                    movieDetailList.add(movieDetail);
                }

                return movieDetailList;
            } catch (JSONException e) {
                Log.e(LOG, "Error with parsing and creating movie detail list", e);
                return null;  // if there is an error parsing the data
            }
        }

        @Override
        protected void onPostExecute(List<MovieDetail> movieDetailList) {
            if (movieDetailList != null) {
                mMoviePosterAdapter.clear();
                mMoviePosterAdapter.addAll(movieDetailList);
            }
        }
    }
}
