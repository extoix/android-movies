package com.extoix.android.movies.ui;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        if(movieDetail != null) {
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
