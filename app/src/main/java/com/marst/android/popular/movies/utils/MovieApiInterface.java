package com.marst.android.popular.movies.utils;

import com.marst.android.popular.movies.data.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.marst.android.popular.movies.utils.UtilsConstants.KEY_PARAM;
import static com.marst.android.popular.movies.utils.UtilsConstants.POPULAR_MOVIES;
import static com.marst.android.popular.movies.utils.UtilsConstants.TOP_RATED_MOVIES;

public interface MovieApiInterface {


    @GET(TOP_RATED_MOVIES)
    Call<MoviesResponse> getTopRatedMovies(@Query(KEY_PARAM) String apiKey);

    @GET(POPULAR_MOVIES)
    Call<MoviesResponse> getPopularMovies(@Query(KEY_PARAM) String apiKey);
}
