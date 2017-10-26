package com.marst.android.popular.movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.marst.android.popular.movies.data.Movie;
import com.marst.android.popular.movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private int mNumberItems;
    private Context mContext;
    private List<Movie> movieList;

    private static final String TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(int mNumberItems, Context mContext, List<Movie> movieList) {
        this.mNumberItems = mNumberItems;
        this.mContext = mContext;
        this.movieList = movieList;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForMovieItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForMovieItem, parent, shouldAttachToParentImmediately);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie = getItem(position);

        if (NetworkUtils.isNetworkConnectionAvailable(mContext) && movie != null
                && movie.getPosterPath() != null && !"".equals(movie.getPosterPath())) {
            Picasso.with(mContext)
                    .load(NetworkUtils.buildPosterURL(movie.getPosterPath()))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(holder.moviePoster);
        } else {
            Log.d(TAG, mContext.getString(R.string.no_connection));
        }
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            final Movie movie = getItem(clickedPosition);
            Intent movieDetailsIntent = new Intent(mContext, DetailsActivity.class);
            movieDetailsIntent.putExtra(mContext.getString(R.string.movie_intent), movie);
            mContext.startActivity(movieDetailsIntent);
//
//            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    private Movie getItem(int position){
        return movieList.get(position);
    }
}
