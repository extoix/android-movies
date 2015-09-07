package com.extoix.android.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.extoix.android.movies.R;
import com.extoix.android.movies.model.MovieDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviePosterAdapter extends ArrayAdapter<MovieDetail> {

    public MoviePosterAdapter(Context context, int resource, int textViewResourceId, List<MovieDetail> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster, parent, false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_poster_imageview);

        MovieDetail movieDetail = getItem(position);
        String posterPathURL = movieDetail.getPosterPathURL();

        Picasso.with(getContext()).load(posterPathURL).into(imageView);

        return imageView;
    }
}
