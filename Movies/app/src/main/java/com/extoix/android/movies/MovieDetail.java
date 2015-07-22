package com.extoix.android.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by secure on 7/19/15.
 */
public class MovieDetail implements Parcelable {

    private String title;
    private String releaseDate;
    private String voteAverage;
    private String overview;
    private String posterPath;
    private String posterPathURL;

    public MovieDetail() {
    }

    private MovieDetail(Parcel parcel){
        title = parcel.readString();
        releaseDate = parcel.readString();
        voteAverage = parcel.readString();
        overview = parcel.readString();
        posterPath = parcel.readString();
        posterPathURL = parcel.readString();
    }

    public static final Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel parcel) {
            return new MovieDetail(parcel);
        }

        @Override
        public MovieDetail[] newArray(int i) {
            return new MovieDetail[i];
        }

    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPathURL() {
        return posterPathURL;
    }

    public void setPosterPathURL(String posterPathURL) {
        this.posterPathURL = posterPathURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(voteAverage);
        parcel.writeString(overview);
        parcel.writeString(posterPath);
        parcel.writeString(posterPathURL);
    }

}
