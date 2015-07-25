package com.extoix.android.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            MovieDetail movieDetail = (MovieDetail)intent.getParcelableExtra(Intent.EXTRA_TEXT);

            ((TextView)rootView.findViewById(R.id.movie_detail_title)).setText(movieDetail.getTitle());

            ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_detail_poster);
            Picasso.with(getActivity()).load(movieDetail.getPosterPathURL()).into(imageView);

            ((TextView)rootView.findViewById(R.id.movie_detail_releaseDate)).setText(movieDetail.getRelease_date());
            ((TextView)rootView.findViewById(R.id.movie_detail_voteAverage)).setText(movieDetail.getVote_average());
            ((TextView)rootView.findViewById(R.id.movie_detail_overview)).setText(movieDetail.getOverview());
        }

        return rootView;
    }
}
