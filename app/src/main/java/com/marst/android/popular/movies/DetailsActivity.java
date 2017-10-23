package com.marst.android.popular.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marst.android.popular.movies.data.Details;
import com.marst.android.popular.movies.databinding.ActivityDetailsBinding;
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

        ActivityDetailsBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_details);

//        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMoviePosterThumbnail = (ImageView) findViewById(R.id.movie_poster_thumbnail);
//        mMovieUserRating = (TextView) findViewById(R.id.user_rating);
//        mMovieReleaseDate = (TextView) findViewById(R.id.release_date);
//        mPlotSynopsis = (TextView) findViewById(R.id.plot_synopsis);

        Intent movieIntent = getIntent();
        Movie movie = null;

        if (movieIntent.hasExtra(getString(R.string.movie_intent))) {
            movie = movieIntent.getExtras().getParcelable(getString(R.string.movie_intent));

        }


        if (NetworkUtils.isNetworkConnectionAvailable(DetailsActivity.this)) {
            if( movie!=null && movie.getPosterPath()!=null && !"".equals(movie.getPosterPath())
                    && movie.getOriginalTitle()!=null && !"".equals(movie.getOriginalTitle())
                    && movie.getOverview()!=null && !"".equals(movie.getOverview())
                    && movie.getReleaseDate()!=null && !"".equals(movie.getReleaseDate())
                    && movie.getVoteAverage()!=null && !"".equals(movie.getVoteAverage())) {

                Details details = new Details(movie.getOriginalTitle(),movie.getPosterPath(),
                        movie.getVoteCount(),movie.getReleaseDate(),movie.getOverview());
                Picasso.with(DetailsActivity.this).load(NetworkUtils.
                        buildPosterURL(details.getMoviePosterThumbnail())).into(mMoviePosterThumbnail);

                binding.setDetails(details);

//                mMovieTitle.setText(movie.getOriginalTitle());
//                mPlotSynopsis.setText(movie.getOverview());
//                mMovieReleaseDate.setText(movie.getReleaseDate());
//                mMovieUserRating.setText(movie.getVoteAverage());

            } else {
                Toast.makeText(DetailsActivity.this,getString(R.string.no_details_data),Toast.LENGTH_LONG).show();
                Log.d(TAG,getString(R.string.no_details_data));
            }
        } else {
            Log.d(TAG,getString(R.string.no_connection));
            Intent back = new Intent(DetailsActivity.this,MainActivity.class);
            startActivity(back);
        }
    }
}
