package com.marst.android.popular.movies.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.marst.android.popular.movies.BuildConfig;
import com.marst.android.popular.movies.R;
import com.marst.android.popular.movies.data.Movie;
import com.marst.android.popular.movies.data.MoviesResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String POSTER_BASE_URL="http://image.tmdb.org/t/p/";

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/";

    private static final String POPULAR_MOVIES= "movie/popular";

    private static final String TOP_RATED_MOVIES= "movie/top_rated";

    private static final String POPULAR_MOVIES_URL="http://api.themoviedb.org/3/movie/popular";

    private static final String TOP_RATED_MOVIES_URL="http://api.themoviedb.org/3/movie/top_rated";

    private static final String KEY_PARAM="api_key";

    private static Retrofit retrofitClient;

    private static List<Movie> movies = null;

     /*
        Phone size
     */
    private static final String W_92 = "w92";
    private static final String W_154 = "w154";
    private static final String W_185 = "w185";
    private static final String W_342 = "w342";
    private static final String W_500 = "w500";
    private static final String W_780 = "w780";
    private static final String W_original = "original";

    private static final String DEFAULT_SIZE = W_185;

    /**
     * Retrieves the api.key from a local.properties file
     */
    public static final String getApiKey(){
        return BuildConfig.KEY;
    }

    /**
     * Converts given Uri to URL.
     *
     * @param uri Uri that will be converted to URL
     * @return The URL that will be used for the requests
     */
    private static final URL convertUri2URL(Uri uri, Context context) {
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG,Resources.getSystem().getString(R.string.malformed_url),e);
        }

        Log.v(TAG, context.getResources().getString(R.string.built_uri)+ url);

        return url;
    }

    /**
     * Builds the URL used to talk to the movie db service using a poster path returned from
     * the service.
     *
     * @param posterPath The poster path from where the movie poster will be fetch.
     * @return The URL String to use for fetching movie poster from themoviedb.
     */
    public static String buildPosterURL(String posterPath) {
        return POSTER_BASE_URL+"/"+W_185+"/"+posterPath;
    }

    /**
     * Builds the Url used to talk to themoviedb service for retrieving the popular movies.
     *
     * @return The URL to use to query themoviedb service for popular movies.
     */
    public static URL buildPopularMoviesURL(Context context) {
        Uri buildUri = Uri.parse(POPULAR_MOVIES_URL).buildUpon().
                appendQueryParameter(KEY_PARAM,getApiKey()).
                build();

        return convertUri2URL(buildUri,context);
    }

    /**
     * Builds the Url used to talk to themoviedb service for retrieving the top rated movies.
     *
     * @return The Url to use to query themoviedb service for the top rated movies.
     */
    public static URL buildTopRatedMoviesURL(Context context){
        Uri buildUri = Uri.parse(TOP_RATED_MOVIES_URL).buildUpon().
                appendQueryParameter(KEY_PARAM,getApiKey()).
                build();

        return convertUri2URL(buildUri,context);
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                Log.v(TAG,Resources.getSystem().getString(R.string.no_url_response) + url.toString());
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method is checking if there is a network connection.
     * NOTE : Network connection does not by default means internet access.
     * @param context The Activity context
     * @return True if there is a network connectivity, false otherwise
     */
    public static boolean isNetworkConnectionAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    public static Retrofit getClient() {
        if (retrofitClient==null) {
            retrofitClient = new Retrofit.Builder()
                    .baseUrl(MOVIES_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitClient;
    }

    public interface MovieApiInterface {

        @GET(TOP_RATED_MOVIES)
        Call<MoviesResponse> getTopRatedMovies(@Query(KEY_PARAM) String apiKey);

        @GET(POPULAR_MOVIES)
        Call<MoviesResponse> getPopularMovies(@Query(KEY_PARAM) String apiKey);

    }
}
