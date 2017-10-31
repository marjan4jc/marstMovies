package com.marst.android.popular.movies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.marst.android.popular.movies.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.marst.android.popular.movies.utils.UtilsConstants.MOVIES_BASE_URL;
import static com.marst.android.popular.movies.utils.UtilsConstants.POSTER_BASE_URL;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static Retrofit retrofitClient;

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


    /**
     * This method builds and returns a Retrofit client, build to access the movie base URL
     * and uses GSON Converter Factory.
     *
     * @return Retrofit client
     */
    public static Retrofit getClient() {
        if (retrofitClient==null) {
            retrofitClient = new Retrofit.Builder()
                    .baseUrl(MOVIES_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitClient;
    }
}
