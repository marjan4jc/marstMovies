package com.marst.android.popular.movies;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.Arrays;

import com.marst.android.popular.movies.utils.TheMovieDBJsonUtils;
import com.marst.android.popular.movies.utils.MenuHelper;
import com.marst.android.popular.movies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private GridView mMoviesGridView;

    private TextView mMoviesErrorTextView;

    private ProgressBar mProgressBarrIndicator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesGridView = (GridView) findViewById(R.id.movies_grid);

        mMoviesErrorTextView = (TextView) findViewById(R.id.movies_error_message_display);

        mProgressBarrIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData(MenuHelper.isTopRated);
    }


    /**
     * This method will get the top rated or most popular movies
     * in a new background thread. The most popular is the default.
     *
     * @param isTopRated  If isTopRated true, the top movies will be fetched
     *                    anf most popular otherwise.
     */
    private void loadMovieData(boolean isTopRated) {
        URL url;
        if(!isTopRated) {
            url = NetworkUtils.buildPopularMoviesURL(MainActivity.this);
        } else {
            url = NetworkUtils.buildTopRatedMoviesURL(MainActivity.this);
        }
        new FetchMoviesTask().execute(url);
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class FetchMoviesTask extends AsyncTask<URL,Void,Movie[]>  {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBarrIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(URL... params) {

            if ( params.length == 0 ){
                return null;
            }
            URL moviesUrl = params[0];

            try {
                //If there i a network connection try to fetch data
                if(NetworkUtils.isNetworkConnectionAvailable(MainActivity.this)) {
                    String jsonString = NetworkUtils.getResponseFromHttpUrl(moviesUrl);
                    return TheMovieDBJsonUtils.getMoviesFromJson(jsonString,MainActivity.this);
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mProgressBarrIndicator.setVisibility(View.INVISIBLE);
            if(movies!=null) {
                showMoviesGridView();

                MovieAdapter movieAdapter = new
                        MovieAdapter(MainActivity.this, Arrays.asList(movies));
                mMoviesGridView.setAdapter(movieAdapter);

            } else {
                showErrorMsg();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();

        switch (itemID) {
            case R.id.most_popular:
                MenuHelper.isTopRated = false;
                loadMovieData(MenuHelper.isTopRated);
                break;
            case R.id.top_rated:
                MenuHelper.isTopRated  = true;
                loadMovieData(MenuHelper.isTopRated);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will make movies grid visible and will
     * hide the error message.
     */
    private void showMoviesGridView() {

        mMoviesErrorTextView.setVisibility(View.INVISIBLE);

        mMoviesGridView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the
     * movies grid.
     */
    private void showErrorMsg() {

        mMoviesGridView.setVisibility(View.INVISIBLE);

        mMoviesErrorTextView.setVisibility(View.VISIBLE);
    }
}
