package com.marst.android.popular.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.marst.android.popular.movies.data.Details;
import com.marst.android.popular.movies.databinding.ActivityDetailsBinding;
import com.marst.android.popular.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActivityDetailsBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_details);


        ImageView mMoviePosterThumbnail = (ImageView) findViewById(R.id.movie_poster_thumbnail);

        Intent movieIntent = getIntent();
        Details details = null;

        if (movieIntent.hasExtra(getString(R.string.movie_intent))) {
            details = movieIntent.getExtras().getParcelable(getString(R.string.movie_intent));

        }


        if (NetworkUtils.isNetworkConnectionAvailable(DetailsActivity.this)) {
            if( details !=null && details.hasAllProperties()) {

                details = new Details(details.getMovieTitle(), details.getMoviePosterThumbnail(),
                        details.getMovieUserRating(), details.getMovieReleaseDate(), details.getPlotSynopsis());

                Picasso.with(DetailsActivity.this).load(NetworkUtils
                        .buildPosterURL(details.getMoviePosterThumbnail()))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(mMoviePosterThumbnail);

                binding.setDetails(details);

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
