package com.marst.android.popular.movies.utils;

import android.content.Context;
import android.util.Log;

import com.marst.android.popular.movies.data.MovieOld;
import com.marst.android.popular.movies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle TheMoviesDB JSON data.
 */
public final class TheMovieDBJsonUtils {

    private static final String TAG = TheMovieDBJsonUtils.class.getSimpleName();

    /* All movies are children of the "results" object */
    private static final String MDB_RESULTS = "results";



    // MovieOld parameters

    private static final String MDB_VOTE_COUNT = "vote_count";

    private static final String MDB_ID = "id";

    private static final String MDB_VIDEO = "video";

    private static final String MDB_VOTE_AVERAGE = "vote_average";

    private static final String MDB_TITLE = "title";

    private static final String MDB_POPULARITY = "popularity";

    private static final String MDB_POSTER_PATH = "poster_path";

    private static final String MDB_ORIGINAL_LANGUAGE = "original_language";

    private static final String MDB_ORIGINAL_TITLE = "original_title";

    /* Each genre Id is an element of the genre_ids array */
    private static final String MDB_GENRE_IDS = "genre_ids";

    private static final String MDB_BACKDROP_PATH = "backdrop_path";

    private static final String MDB_ADULT = "adult";

    private static final String MDB_OVERVIEW = "overview";

    private static final String MDB_RELEASE_DATE = "release_date";

    // Movies parameters  -- END

    private static final String MDB_STATUS_CODE = "status_code";
    private static final String MDB_STATUS_MSG = "status_message";
    private static final String MDB_SUCCESS = "success";

//    private static final Resources resources = Resources.getSystem();
    /**
     * This method parses JSON from a web response and
     * returns an Array of Movies.
     *
     * @param jsonString The JSON response from the server
     * @return Array of Movies with some movie details
     * @throws JSONException If JSON data can not be properly parsed
     */

    public static MovieOld[] getMoviesFromJson(String jsonString, Context context) throws JSONException {

        JSONObject movieJson = new JSONObject(jsonString);
        MovieOld[] movies;

        if (movieJson.has(MDB_STATUS_CODE)) {

            int errorCode = movieJson.getInt(MDB_STATUS_CODE);
            String statusMsg = null;

            if(movieJson.has(MDB_STATUS_MSG))
                statusMsg = movieJson.getString(MDB_STATUS_CODE);

            Log.d(TAG, context.getResources().getString(R.string.err_code) +errorCode+ context.getResources().getString(R.string.status_msg)+statusMsg);
        }

        JSONArray movieArray = movieJson.getJSONArray(MDB_RESULTS);
        movies = new MovieOld[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieJsonObject = movieArray.getJSONObject(i);

            movies[i] = new MovieOld(movieJsonObject.getString(MDB_ID),
                    movieJsonObject.getString(MDB_VOTE_AVERAGE),
                    movieJsonObject.getString(MDB_POSTER_PATH),
                    movieJsonObject.getString(MDB_ORIGINAL_TITLE),
                    movieJsonObject.getString(MDB_OVERVIEW),
                    movieJsonObject.getString(MDB_RELEASE_DATE));
        }

        return movies;
    }

}
