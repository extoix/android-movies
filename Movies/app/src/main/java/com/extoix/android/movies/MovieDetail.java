package com.extoix.android.movies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class MovieDetail implements Parcelable {

    private String id;
    private String title;
    private String release_date;
    private String vote_average;
    private String overview;
    private String poster_path;
    private String posterPathURL;

    public MovieDetail() {
    }

    private MovieDetail(Parcel parcel){
        id = parcel.readString();
        title = parcel.readString();
        release_date = parcel.readString();
        vote_average = parcel.readString();
        overview = parcel.readString();
        poster_path = parcel.readString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
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
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(release_date);
        parcel.writeString(vote_average);
        parcel.writeString(overview);
        parcel.writeString(poster_path);
        parcel.writeString(posterPathURL);
    }

}
