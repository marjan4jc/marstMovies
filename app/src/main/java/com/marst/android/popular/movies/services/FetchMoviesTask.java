package com.marst.android.popular.movies.services;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import com.marst.android.popular.movies.R;
import com.marst.android.popular.movies.data.Movie;
import com.marst.android.popular.movies.utils.NetworkUtils;
import com.marst.android.popular.movies.utils.TheMovieDBJsonUtils;

import java.net.URL;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class FetchMoviesTask extends AsyncTask<URL,Void,Movie[]> {

    private OnEventListener<Movie[]> mCallBack;
    private Context mContext;
    public Exception mException;

    public FetchMoviesTask(Context mContext, OnEventListener<Movie[]> mCallBack) {
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
    protected Movie[] doInBackground(URL... params) {

        if ( params.length == 0 ){
            return null;
        }
        URL moviesUrl = params[0];

        try {
            //If there i a network connection try to fetch data
            if(NetworkUtils.isNetworkConnectionAvailable(mContext)) {
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
    protected void onPostExecute(Movie[] movies) {

        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(movies);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}
