package com.marst.android.popular.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marst.android.popular.movies.data.Details;
import com.marst.android.popular.movies.data.Movie;
import com.marst.android.popular.movies.services.FetchMoviesTask;
import com.marst.android.popular.movies.services.OnEventListener;
import com.marst.android.popular.movies.utils.MenuHelper;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements MovieAdapter.GridItemClickListener{

    private GridLayoutManager mGridLayoutManager;

    private TextView mMoviesErrorTextView;

    private ProgressBar mProgressBarrIndicator;

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.movies_grid);

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

        new FetchMoviesTask(MainActivity.this,isTopRated, new OnEventListener<List<Movie>>(){

            @Override
            public void onSuccess(List<Movie> movies) {
                mProgressBarrIndicator.setVisibility(View.INVISIBLE);
                if(movies!=null) {
                    showMoviesGridView();

                    mGridLayoutManager = new GridLayoutManager(MainActivity.this,2);

                    //Always returns 20 items per page.
                    mRecyclerView.setHasFixedSize(true);

                    mRecyclerView.setLayoutManager(mGridLayoutManager);

                    MovieAdapter movieAdapter = new
                            MovieAdapter(movies.size(), MainActivity.this,
                            movies, MainActivity.this);

                    mRecyclerView.setAdapter(movieAdapter);

                } else {
                    showErrorMsg(getString(R.string.no_connection));
                }
            }

            @Override
            public void onFailure(Exception e) {
                showErrorMsg(getString(R.string.error_occurred));
            }

            @Override
            public void onSuccessNoMovies() {
                showErrorMsg(getString(R.string.no_movies_returned));
            }

        });
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
    private void showErrorMsg(String errMsg) {

        mMoviesErrorTextView.setText(errMsg);

        mRecyclerView.setVisibility(View.INVISIBLE);

        mMoviesErrorTextView.setVisibility(View.VISIBLE);
    }

    /**
     * This method handles the click event, creating a new Details object
     * and starts a new Details Activity passing an intent that contains extra Details data in it.
     * @param movie The movie is used to get needed data for creating the Details object.
     */
    @Override
    public void onGridItemClick(Movie movie) {
        Details movieDetails = new Details(movie.getTitle(),movie.getPoster_path(),
                Double.toString(movie.getVote_average()),movie.getRelease_date(),movie.getOverview());
        Intent movieDetailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
        movieDetailsIntent.putExtra(getString(R.string.movie_intent), movieDetails);
        startActivity(movieDetailsIntent);
    }
}
