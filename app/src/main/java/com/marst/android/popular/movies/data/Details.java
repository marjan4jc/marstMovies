package com.marst.android.popular.movies.data;

public class Details {
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
}
