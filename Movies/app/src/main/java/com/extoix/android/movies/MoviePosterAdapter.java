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

    public final static String POSTER_PATH_BASE_URL = "http://image.tmdb.org/t/p";
    public final static String POSTER_PATH_SIZE_W184 = "w184";

    public MoviePosterAdapter(Context context, int resource, int textViewResourceId, List<MovieDetail> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_poster, parent, false);
        }

        ImageView imageView = (ImageView)convertView.findViewById(R.id.movie_poster_imageview);

        MovieDetail movieDetail = getItem(position);
        String posterPath = movieDetail.getPosterPath();
        String posterPathURL = buildPosterPathURL(posterPath);

        Picasso.with(getContext()).load(posterPathURL).into(imageView);

        return imageView;
    }

    private String buildPosterPathURL(String posterPath) {
        StringBuilder moviePosterURLBuilder = new StringBuilder();
        moviePosterURLBuilder.append(POSTER_PATH_BASE_URL);
        moviePosterURLBuilder.append('/');
        moviePosterURLBuilder.append(POSTER_PATH_SIZE_W184);
        moviePosterURLBuilder.append(posterPath);

        return moviePosterURLBuilder.toString();
    }
}
