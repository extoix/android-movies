package com.extoix.android.movies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.extoix.android.movies.R;
import com.extoix.android.movies.model.MovieDetail;

public class MovieActivity extends ActionBarActivity implements MovieFragment.MovieCallback {

    static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);


        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, new DetailFragment(), DETAILFRAGMENT_TAG).commit();
            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MovieFragment movieFragment = (MovieFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_movie);
    }

    @Override
    public void onItemSelected(MovieDetail movieDetail) {
        if (mTwoPane) {
            Bundle detailsBundle = new Bundle();
            detailsBundle.putParcelable(DetailFragment.DETAIL, movieDetail);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(detailsBundle);

            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailFragment, DETAILFRAGMENT_TAG).commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class).putExtra(Intent.EXTRA_TEXT, movieDetail);
            startActivity(intent);
        }
    }
}

