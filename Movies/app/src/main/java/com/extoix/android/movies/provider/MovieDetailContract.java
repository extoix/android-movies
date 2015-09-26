package com.extoix.android.movies.provider;

import android.provider.BaseColumns;

public class MovieDetailContract {

    public static final class MovieDetailEntry implements BaseColumns {

        public static final String TABLE_MOVIE_DETAIL = "movie_detail";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_POSTER_PATH_URL = "posterPathURL";
    }
}
