package com.marst.android.popular.movies.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Details implements Parcelable {
    private final String movieTitle;
    private final String moviePosterThumbnail;
    private final String movieUserRating;
    private final String movieReleaseDate;
    private final String plotSynopsis;

    public Details(String movieTitle, String moviePosterThumbnail, String movieUserRating, String movieReleaseDate, String plotSynopsis) {
        this.movieTitle = movieTitle;
        this.moviePosterThumbnail = moviePosterThumbnail;
        this.movieUserRating = movieUserRating;
        this.movieReleaseDate = movieReleaseDate;
        this.plotSynopsis = plotSynopsis;
    }

    protected Details(Parcel in) {
        movieTitle = in.readString();
        moviePosterThumbnail = in.readString();
        movieUserRating = in.readString();
        movieReleaseDate = in.readString();
        plotSynopsis = in.readString();
    }

    public static final Creator<Details> CREATOR = new Creator<Details>() {
        @Override
        public Details createFromParcel(Parcel in) {
            return new Details(in);
        }

        @Override
        public Details[] newArray(int size) {
            return new Details[size];
        }
    };

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMoviePosterThumbnail() {
        return moviePosterThumbnail;
    }

    public String getMovieUserRating() {
        return movieUserRating;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(plotSynopsis);
        dest.writeString(movieUserRating);
        dest.writeString(movieReleaseDate);
        dest.writeString(movieTitle);
    }
}
