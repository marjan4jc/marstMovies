package com.marst.android.popular.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marst.android.popular.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private TextView mMovieTitle;
    private ImageView mMoviePosterThumbnail;
    private TextView mMovieUserRating;
    private TextView mMovieReleaseDate;
    private TextView mPlotSynopsis;
    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMoviePosterThumbnail = (ImageView) findViewById(R.id.movie_poster_thumbnail);
        mMovieUserRating = (TextView) findViewById(R.id.user_rating);
        mMovieReleaseDate = (TextView) findViewById(R.id.release_date);
        mPlotSynopsis = (TextView) findViewById(R.id.plot_synopsis);

        Intent movieIntent = getIntent();
        Movie movie = null;

        if (movieIntent.hasExtra("movie")) {
            movie = movieIntent.getExtras().getParcelable("movie");

        }


        if (NetworkUtils.isNetworkConnectionAvailable(DetailsActivity.this)) {
            if( movie!=null && movie.getPosterPath()!=null && !"".equals(movie.getPosterPath())
                    && movie.getOriginalTitle()!=null && !"".equals(movie.getOriginalTitle())
                    && movie.getOverview()!=null && !"".equals(movie.getOverview())
                    && movie.getReleaseDate()!=null && !"".equals(movie.getReleaseDate())
                    && movie.getVoteAverage()!=null && !"".equals(movie.getVoteAverage())) {

                Picasso.with(DetailsActivity.this).load(NetworkUtils.
                        buildPosterURL(movie.getPosterPath())).into(mMoviePosterThumbnail);

                mMovieTitle.setText(movie.getOriginalTitle());
                mPlotSynopsis.setText(movie.getOverview());
                mMovieReleaseDate.setText(movie.getReleaseDate());
                mMovieUserRating.setText(movie.getVoteAverage());

            } else {
                Toast.makeText(DetailsActivity.this,"Details Data is missing",Toast.LENGTH_LONG).show();
                Log.d(TAG,"Details Data is missing");
            }
        } else {
            Log.d(TAG,"No Network Connection Available");
            Intent back = new Intent(DetailsActivity.this,MainActivity.class);
            startActivity(back);
        }
    }
}
