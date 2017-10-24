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

import com.marst.android.popular.movies.data.Movie;
import com.marst.android.popular.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Activity activityContext, List<Movie> movies) {
        super(activityContext, 0 ,movies);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Movie movie = getItem(position);

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.movie_item, parent, false);
            holder = new ViewHolder();
            holder.moviePoster = convertView.findViewById(R.id.movie_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (NetworkUtils.isNetworkConnectionAvailable(getContext()) && movie != null
                && movie.getPosterPath() != null && !"".equals(movie.getPosterPath())) {
            Picasso.with(getContext()).load(NetworkUtils.buildPosterURL(movie.getPosterPath())).into(holder.moviePoster);
        } else {
            Log.d(TAG, getContext().getString(R.string.no_connection));
        }
        holder.moviePoster.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent movieDetailsIntent = new Intent(getContext(), DetailsActivity.class);
                movieDetailsIntent.putExtra(getContext().getString(R.string.movie_intent), movie);
                getContext().startActivity(movieDetailsIntent);

            }
        });

        return convertView;
    }

    static class ViewHolder{
        private ImageView moviePoster;
    }
}
