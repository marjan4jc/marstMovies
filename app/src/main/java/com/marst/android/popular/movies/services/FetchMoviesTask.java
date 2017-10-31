package com.marst.android.popular.movies.services;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.marst.android.popular.movies.R;
import com.marst.android.popular.movies.data.Movie;
import com.marst.android.popular.movies.data.MoviesResponse;
import com.marst.android.popular.movies.utils.MovieApiInterface;
import com.marst.android.popular.movies.utils.NetworkUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.marst.android.popular.movies.utils.NetworkUtils.isNetworkConnectionAvailable;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class FetchMoviesTask  {

    private final OnEventListener<List<Movie>> mCallBack;
    private final Context mContext;
    private final String TAG = FetchMoviesTask.class.getSimpleName();

    public FetchMoviesTask(Context mContext,boolean isTopRated, OnEventListener<List<Movie>> mCallBack) {
        this.mCallBack = mCallBack;
        this.mContext = mContext;
        fetchMovies(isTopRated);
    }

    /**
     *
     * @param isTopRated
     */
    private void fetchMovies(boolean isTopRated) {

        MovieApiInterface movieApiService =
                            NetworkUtils.getClient().create(MovieApiInterface.class);

        if(isNetworkConnectionAvailable(mContext)) {
            Call<MoviesResponse> callTheMovieDB;
            ProgressBar progressBarIndicator = ((Activity)mContext).findViewById(R.id.pb_loading_indicator);
            progressBarIndicator.setVisibility(View.VISIBLE);
            if(isTopRated) {
                callTheMovieDB = movieApiService.getTopRatedMovies(NetworkUtils.getApiKey());
            } else {
                callTheMovieDB = movieApiService.getPopularMovies(NetworkUtils.getApiKey());
            }
            callTheMovieDB.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    if (mCallBack != null) {
                        List<Movie> movies;

                        if(response.body().getResults()!=null) {
                            movies = response.body().getResults();
                            if (movies!=null) {
                                Log.d(TAG, mContext.getString(R.string.number_movies) + movies.size());
                                mCallBack.onSuccess(movies);
                            } else {
                                Log.d(TAG,mContext.getString(R.string.no_movies_returned));
                                mCallBack.onSuccessNoMovies();
                            }
                        }

                    }
                }
                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.e(TAG,t.getMessage());
                    mCallBack.onFailure(new Exception(t));
                }
            });
        } else  {
            mCallBack.onSuccess(null);
        }
    }
}
