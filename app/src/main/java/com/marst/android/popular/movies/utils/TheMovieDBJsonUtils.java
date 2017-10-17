package com.marst.android.popular.movies.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.marst.android.popular.movies.Movie;

/**
 * Utility functions to handle TheMoviesDB JSON data.
 */
public final class TheMovieDBJsonUtils {

    private static final String TAG = TheMovieDBJsonUtils.class.getSimpleName();

    /* All movies are children of the "results" object */
    private static final String MDB_RESULTS = "results";



    // Movie parameters

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

    public static Movie[] getMoviesFromJson(String jsonString) throws JSONException {

        JSONObject movieJson = new JSONObject(jsonString);
        Movie[] movies;

        if (movieJson.has(MDB_STATUS_CODE)) {

            int errorCode = movieJson.getInt(MDB_STATUS_CODE);
            String statusMsg = null;

            if(movieJson.has(MDB_STATUS_MSG))
                statusMsg = movieJson.getString(MDB_STATUS_CODE);

            Log.d(TAG,"Error code: "+errorCode+ ", status message: "+statusMsg);
        }

        JSONArray movieArray = movieJson.getJSONArray(MDB_RESULTS);
        movies = new Movie[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieJsonObject = movieArray.getJSONObject(i);

            movies[i] = new Movie(movieJsonObject.getString(MDB_ID),
                    movieJsonObject.getString(MDB_VOTE_AVERAGE),
                    movieJsonObject.getString(MDB_POSTER_PATH),
                    movieJsonObject.getString(MDB_ORIGINAL_TITLE),
                    movieJsonObject.getString(MDB_OVERVIEW),
                    movieJsonObject.getString(MDB_RELEASE_DATE));
        }

        return movies;
    }

}
