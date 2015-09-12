package com.extoix.android.movies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.extoix.android.movies.R;
import com.extoix.android.movies.model.MovieDetail;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {

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

        final String movieDetailId = movieDetail.getId();

        if(movieDetail != null) {
            ((TextView)rootView.findViewById(R.id.movie_detail_title)).setText(movieDetail.getTitle());

            ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_detail_poster);
            Picasso.with(getActivity()).load(movieDetail.getPosterPathURL()).into(imageView);

            ((TextView)rootView.findViewById(R.id.movie_detail_releaseDate)).setText(movieDetail.getRelease_date());
            ((TextView)rootView.findViewById(R.id.movie_detail_voteAverage)).setText(movieDetail.getVote_average());
            createFavoriteCheckbox(rootView, movieDetail, movieDetailId);
            ((TextView)rootView.findViewById(R.id.movie_detail_overview)).setText(movieDetail.getOverview());
        }

        return rootView;
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
