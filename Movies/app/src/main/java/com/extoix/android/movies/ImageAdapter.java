package com.extoix.android.movies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * Created by secure on 7/19/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private Integer[] moviePosters = {
            R.drawable.interstellar,
            R.drawable.interstellar,
            R.drawable.interstellar,
            R.drawable.interstellar,
            R.drawable.interstellar,
            R.drawable.interstellar
    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return moviePosters.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if(convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(moviePosters[position]);

        return imageView;
    }

}
