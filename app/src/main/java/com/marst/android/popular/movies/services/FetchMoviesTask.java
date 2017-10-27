package com.marst.android.popular.movies.services;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.marst.android.popular.movies.R;
import com.marst.android.popular.movies.data.MovieOld;
import com.marst.android.popular.movies.data.MoviesResponse;
import com.marst.android.popular.movies.utils.NetworkUtils;
import com.marst.android.popular.movies.utils.TheMovieDBJsonUtils;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.marst.android.popular.movies.utils.NetworkUtils.isNetworkConnectionAvailable;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class FetchMoviesTask extends AsyncTask<URL,Void,MovieOld[]> {

    private OnEventListener<MovieOld[]> mCallBack;
    private Context mContext;
    public Exception mException;

    public FetchMoviesTask(Context mContext, OnEventListener<MovieOld[]> mCallBack) {
        this.mCallBack = mCallBack;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressBar progressBarIndicator = ((Activity)mContext).findViewById(R.id.pb_loading_indicator);
        progressBarIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected MovieOld[] doInBackground(URL... params) {

        if ( params.length == 0 ){
            return null;
        }
        URL moviesUrl = params[0];

        try {
            //If there i a network connection try to fetch data
            if(isNetworkConnectionAvailable(mContext)) {
                String jsonString = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
                return TheMovieDBJsonUtils.getMoviesFromJson(jsonString,mContext);
            } else {
                return null;
            }
        } catch (Exception e) {
            mException = e;
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieOld[] movies) {

        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(movies);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }

    protected void FetchMovies(boolean isTopRated, Context context){

        NetworkUtils.MovieApiInterface movieApiService =
                            NetworkUtils.getClient().create(NetworkUtils.MovieApiInterface.class);

        if(isNetworkConnectionAvailable(context)) {
            Call<MoviesResponse> callTheMovieDB;
            if(isTopRated) {
                callTheMovieDB = movieApiService.getTopRatedMovies(NetworkUtils.getApiKey());
            } else {
                callTheMovieDB = movieApiService.getPopularMovies(NetworkUtils.getApiKey());
            }
            callTheMovieDB.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if (mCallBack != null) {

                            mCallBack.onSuccess(response.body().getResults());

                    }
                    response.body().getResults();
                    Log.d(TAG,"" + movies.size());
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.e(TAG,t.getMessage());
                    mCallBack.onFailure(new Exception(t));
                }
            });
        } else  {

        }
    }
}
