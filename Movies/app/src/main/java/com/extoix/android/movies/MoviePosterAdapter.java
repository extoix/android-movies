package com.extoix.android.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by secure on 7/19/15.
 */
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
        String moviePosterURL = movieDetail.getPosterPath();

        Picasso.with(getContext()).load(moviePosterURL).into(imageView);

        return imageView;
    }
}
