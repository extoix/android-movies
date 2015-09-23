package com.extoix.android.movies.provider;

import android.provider.BaseColumns;

public class MovieDetailContract {

    public static final class MovieDetailEntry implements BaseColumns {

        public static final String TABLE_MOVIE_DETAIL = "movie_detail";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "String release_date";
        public static final String COLUMN_VOTE_AVERAGE = "String vote_average";
        public static final String COLUMN_OVERVIEW = "String overview";
        public static final String COLUMN_POSTER_PATH = "String poster_path";
        public static final String COLUMN_POSTER_PATH_URL = "String posterPathURL";
    }
}
