package com.extoix.android.movies.model;

import android.provider.BaseColumns;

public class MovieContract {

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final String TITLE = "title";
        public static final String RELEASE_DATE = "release_date";
        public static final String VOTE_AVERAGE = "vote_average";
        public static final String OVERVIEW = "overview";
        public static final String POSTER_PATH = "poster_path";
        public static final String POSTER_PATH_URL = "poster_path_url";
    }

    public static final class TrailerEntry implements BaseColumns {
        public static final String TABLE_NAME = "trailer";

        public static final String MOVIE_ID = "movie_id";

        public static final String KEY = "key";
        public static final String NAME = "name";
    }

    public static final class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "review";

        public static final String MOVIE_ID = "movie_id";

        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
    }

}
