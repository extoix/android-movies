package com.extoix.android.movies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.extoix.android.movies.R;
import com.extoix.android.movies.model.MovieDetail;
import com.extoix.android.movies.model.ReviewDetail;
import com.extoix.android.movies.model.ReviewDetailResult;
import com.extoix.android.movies.model.TrailerDetail;
import com.extoix.android.movies.model.TrailerDetailResult;
import com.extoix.android.movies.service.TheMovieDB;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailFragment extends Fragment {
    private static final String LOG = DetailFragment.class.getSimpleName();

    static final String DETAIL = "DETAIL";

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        MovieDetail movieDetail = null;

        Bundle detailBundle = getArguments();
        if (detailBundle != null) {
            movieDetail = detailBundle.getParcelable(DetailFragment.DETAIL);
        }

        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            movieDetail = (MovieDetail) intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }

        if(movieDetail == null) {
            rootView = inflater.inflate(R.layout.fragment_detail_start, container, false);
        } else {
            createDetailSection(rootView, movieDetail);
            createTrailerSection(inflater, container, rootView, movieDetail);
            createReviewSection(inflater, container, rootView, movieDetail);
        }

        return rootView;
    }

    private void createReviewSection(final LayoutInflater inflater, final ViewGroup container, final View rootView, MovieDetail movieDetail) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.themoviedb_api_url)).build();
        TheMovieDB theMovieDBService = restAdapter.create(TheMovieDB.class);

        String apiKey = getString(R.string.themoviedb_api_key);

        String movieId = movieDetail.getId();

        theMovieDBService.retrieveMovieReviewResult(movieId, apiKey, new Callback<ReviewDetailResult>() {

            @Override
            public void success(ReviewDetailResult reviewDetailResult, Response response) {
                List<ReviewDetail> reviewDetailList = reviewDetailResult.getResults();

                for (ReviewDetail reviewDetail : reviewDetailList) {
                    String reviewAuthor = reviewDetail.getAuthor();
                    String reviewContent = reviewDetail.getContent();

                    View listItemReview = inflater.inflate(R.layout.list_item_review, container, false);
                    ((TextView)listItemReview.findViewById(R.id.review_author)).setText(reviewAuthor);
                    ((TextView)listItemReview.findViewById(R.id.review_content)).setText(reviewContent);

                    LinearLayout reviewListView = (LinearLayout) rootView.findViewById(R.id.list_item_review);
                    reviewListView.addView(listItemReview);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG, "Error with createReviewSection during service Callback<ReviewDetailResult>", error);
            }
        });
    }

    private void createTrailerSection(final LayoutInflater inflater, final ViewGroup container, final View rootView, MovieDetail movieDetail) {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getString(R.string.themoviedb_api_url)).build();
        TheMovieDB theMovieDBService = restAdapter.create(TheMovieDB.class);

        String apiKey = getString(R.string.themoviedb_api_key);

        String movieId = movieDetail.getId();

        theMovieDBService.retrieveMovieTrailerResult(movieId, apiKey, new Callback<TrailerDetailResult>() {

            @Override
            public void success(TrailerDetailResult trailerDetailResult, Response response) {
                List<TrailerDetail> trailerDetailList = trailerDetailResult.getResults();

                for (TrailerDetail trailerDetail : trailerDetailList) {
                    final String trailerKey = trailerDetail.getKey();
                    String trailerName = trailerDetail.getName();

                    View listItemTrailer = inflater.inflate(R.layout.list_item_trailer, container, false);
                    ((TextView)listItemTrailer.findViewById(R.id.trailer_name)).setText(trailerName);

                    CheckBox trailerPlayCheckbox = (CheckBox) listItemTrailer.findViewById(R.id.trailer_play);
                    trailerPlayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Uri videoLocation = Uri.parse(getString(R.string.youtube_base_url) + trailerKey);

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(videoLocation);

                            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                startActivity(intent);
                            } else {
                                Log.e(LOG, "Error with openVideo when activity did not resolve.");
                            }
                        }
                    });

                    LinearLayout trailerListView = (LinearLayout) rootView.findViewById(R.id.list_item_trailer);
                    trailerListView.addView(listItemTrailer);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG, "Error with createTrailerSection during service Callback<TrailerDetailResult>", error);
            }
        });
    }

    private void createDetailSection(View rootView, MovieDetail movieDetail) {

        if(movieDetail != null) {
            String movieDetailId = movieDetail.getId();

            ((TextView)rootView.findViewById(R.id.movie_detail_title)).setText(movieDetail.getTitle());

            ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_detail_poster);
            Picasso.with(getActivity()).load(movieDetail.getPosterPathURL()).into(imageView);

            ((TextView)rootView.findViewById(R.id.movie_detail_releaseDate)).setText(movieDetail.getRelease_date());
            ((TextView)rootView.findViewById(R.id.movie_detail_voteAverage)).setText(movieDetail.getVote_average());
            createFavoriteCheckbox(rootView, movieDetail, movieDetailId);
            ((TextView)rootView.findViewById(R.id.movie_detail_overview)).setText(movieDetail.getOverview());
        }
    }

    private void createFavoriteCheckbox(View rootView, MovieDetail movieDetail, final String movieDetailId) {
        CheckBox favoriteCheckbox = ((CheckBox)rootView.findViewById(R.id.movie_detail_favorite));

        final String movieId = movieDetail.getId();
        SharedPreferences favoriteSharedPreferences = getActivity().getSharedPreferences(getString(R.string.favorite_pref_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = favoriteSharedPreferences.edit();
        String retrievedMovieDetailId = favoriteSharedPreferences.getString(movieId, null);

        if(retrievedMovieDetailId != null) {
            favoriteCheckbox.setChecked(true);
        }

        favoriteCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences favoriteSharedPreferences = getActivity().getSharedPreferences(getString(R.string.favorite_pref_key), Context.MODE_PRIVATE);

                String retrievedMovieDetailId = favoriteSharedPreferences.getString(movieDetailId, null);

                SharedPreferences.Editor editor = favoriteSharedPreferences.edit();
                if(isChecked && retrievedMovieDetailId == null ) {
                    editor.putString(movieDetailId, movieDetailId).commit();
                } else {
                    editor.remove(movieDetailId).commit();
                }
            }
        });
    }

}
