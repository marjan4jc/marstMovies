package com.marst.android.popular.movies;

import android.content.Context;
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

    private final int mNumberItems;
    private final Context mContext;
    private final List<Movie> movieList;
    private final GridItemClickListener mGridItemListener;

    private static final String TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(int mNumberItems, Context mContext, List<Movie> movies, GridItemClickListener mGtidItemListener) {
        this.mNumberItems = mNumberItems;
        this.mContext = mContext;
        this.movieList = movies;
        this.mGridItemListener = mGtidItemListener;
    }

    /**
     * The interface that recives onClick messages
     */
    public interface GridItemClickListener{
        void onGridItemClick(Movie movie);
    }

    /**
     * This method get called when a new ViewHolder is created. When the RecyclerView is laid out,
     * there will be created enough ViewHolders to fill the screen and allow fro scroling
     * @param parent The ViewGroup that these ViewHolders are contained within
     * @param viewType If the RecyclerView has more than one type of item, the viewType int is used
     *                 to provide a different layout.(This Project till now doesn't have more then
     *                 one type.)
     * @return A new MovieViewHolder that holds the View for each grid item.
     */
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


    /**
     * This method is called by the RecyclerView to display the data at the specified
     * position. The contents of the ViewHolder is updated in this method, using the position
     * argument.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data list.
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie movie = getItem(position);

        if (NetworkUtils.isNetworkConnectionAvailable(mContext)) {
            if( movie != null && movie.getPosterPath() != null
                    && !"".equals(movie.getPosterPath())) {
                Picasso.with(mContext)
                        .load(NetworkUtils.buildPosterURL(movie.getPosterPath()))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(holder.moviePoster);
            } else {
                Log.d(TAG, mContext.getString(R.string.movie_missing_data));
            }
        } else {
            Log.d(TAG, mContext.getString(R.string.no_connection));
        }
    }

    /**
     * Returns the number of items to display.
     *
     * @return The number of items available.
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }


    /**
     * Cache of the children views for a grid item.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_image);
            itemView.setOnClickListener(this);
        }

        /**
         * Whenever a user clicks on an item in the grid this method is called.
         * @param v The View that was clicked.
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            final Movie movie = getItem(clickedPosition);
            mGridItemListener.onGridItemClick(movie);

        }
    }

    private Movie getItem(int position){
        return movieList.get(position);
    }
}
