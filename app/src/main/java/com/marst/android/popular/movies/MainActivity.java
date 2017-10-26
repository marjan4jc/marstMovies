package com.marst.android.popular.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marst.android.popular.movies.data.Movie;
import com.marst.android.popular.movies.services.FetchMoviesTask;
import com.marst.android.popular.movies.services.OnEventListener;
import com.marst.android.popular.movies.utils.MenuHelper;
import com.marst.android.popular.movies.utils.NetworkUtils;

import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private GridView mMoviesGridView;

    private GridLayoutManager mGridLayoutManager;

    private TextView mMoviesErrorTextView;

    private ProgressBar mProgressBarrIndicator;

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movies_grid);

//        mMoviesGridView = (GridView) findViewById(R.id.movies_grid);

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

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(MainActivity.this, new OnEventListener<Movie[]>(){

            @Override
            public void onSuccess(Movie[] movies) {
                mProgressBarrIndicator.setVisibility(View.INVISIBLE);
                if(movies!=null) {
                    showMoviesGridView();

                    mGridLayoutManager = new GridLayoutManager(MainActivity.this,2);

                    //Always returns 20 items
                    mRecyclerView.setHasFixedSize(true);

                    mRecyclerView.setLayoutManager(mGridLayoutManager);

                    MovieAdapter movieAdapter = new
                            MovieAdapter(movies.length, MainActivity.this, Arrays.asList(movies));

                    mRecyclerView.setAdapter(movieAdapter);

                } else {
                    showErrorMsg();
                }

            }

            @Override
            public void onFailure(Exception e) {
                showErrorMsg();
            }
        });
        fetchMoviesTask.execute(url);
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

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the
     * movies grid.
     */
    private void showErrorMsg() {

        mRecyclerView.setVisibility(View.INVISIBLE);

        mMoviesErrorTextView.setVisibility(View.VISIBLE);
    }
//
//    @Override
//    public void onListItemClick(int clickedItemIndex) {
//        Intent movieDetailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
//        movieDetailsIntent.putExtra(getString(R.string.movie_intent), movie);
//        startActivity(movieDetailsIntent);
//
//    }
}
