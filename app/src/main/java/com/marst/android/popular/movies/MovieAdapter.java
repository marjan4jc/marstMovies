package com.marst.android.popular.movies;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import com.marst.android.popular.movies.utils.NetworkUtils;

class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Activity activityContext, List<Movie> movies) {
        super(activityContext, 0 ,movies);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.movie_item, parent, false);
        }

        ImageView moviePosterImg = convertView.findViewById(R.id.movie_image);

        if(NetworkUtils.isNetworkConnectionAvailable(getContext()) && movie!=null
                && movie.getPosterPath()!=null && !"".equals(movie.getPosterPath())) {
            Picasso.with(getContext()).load(NetworkUtils.buildPosterURL(movie.getPosterPath())).into(moviePosterImg);
        } else {
            Log.d(TAG, getContext().getString(R.string.no_connection));
        }
        moviePosterImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent movieDetailsIntent = new Intent(getContext(),DetailsActivity.class);
                movieDetailsIntent.putExtra(getContext().getString(R.string.movie_intent),movie);
                getContext().startActivity(movieDetailsIntent);

            }
        });

        return convertView;
    }
}
