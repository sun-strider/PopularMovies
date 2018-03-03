package com.example.android.popularmovies.utilities;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by me74 on 28.02.2018.
 */

public class OpenMovieJsonUtils {

    private static final String LOG_TAG = OpenMovieJsonUtils.class.getName();


    public static List<String> parseMovieDbJson(String json) {

        List<String> mParsedMovieData = new ArrayList<>();

        final String M_DB_RESULTS = "results";
        final String M_DB_TITLE = "original_title";
        final String M_DB_RELEASE_DATE = "release_date";
        final String M_DB_POSTER_PATH = "poster_path";
        final String M_DB_VOTE_AVERAGE = "vote_average";
        final String M_DB_OVERVIEW = "overview";

        JSONObject mBaseJsonResponse;
        JSONArray mResults;
        JSONObject mMovie;
        String title = "";
        String releaseDate = "";
        String posterPath = "";
        String voteAverage = "";
        String overview = "";


        if (TextUtils.isEmpty(json)) {
            return null;
        }

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            mBaseJsonResponse = new JSONObject(json);

            // Get results JSON array
            if (mBaseJsonResponse.has(M_DB_RESULTS)) {
                mResults = mBaseJsonResponse.getJSONArray(M_DB_RESULTS);

                // loop through the results to get the movies
                for (int i = 0; i < mResults.length(); i++) {

                    mMovie = mResults.getJSONObject(i);

                    if (mMovie.has(M_DB_TITLE)) {

                        title = mMovie.getString(M_DB_TITLE);
                    }

                    if (mMovie.has(M_DB_RELEASE_DATE)) {

                        releaseDate = mMovie.getString(M_DB_RELEASE_DATE);
                    }

                    if (mMovie.has(M_DB_POSTER_PATH)) {

                        posterPath = mMovie.getString(M_DB_POSTER_PATH);
                    }

                    if (mMovie.has(M_DB_VOTE_AVERAGE)) {

                        voteAverage = mMovie.getString(M_DB_VOTE_AVERAGE);
                    }

                    if (mMovie.has(M_DB_OVERVIEW)) {

                        overview = mMovie.getString(M_DB_OVERVIEW);
                    }

                    // TODO: store the data either in a string, or create a movie class, instantiate and store the data
                    // TODO: if movie class is created: either make method return a movie instance then transform to string in calling activity

                    mParsedMovieData.add(
                            title + "\n" +
                                    releaseDate + "\n" +
                                    voteAverage + "\n" +
                                    posterPath + "\n" +
                                    overview
                    );
                }

                return mParsedMovieData;
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the JSON results", e);
        }

        // Return parsed movie data array
        return mParsedMovieData;
    }

}
