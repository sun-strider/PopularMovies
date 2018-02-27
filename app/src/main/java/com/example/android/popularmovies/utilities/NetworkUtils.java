package com.example.android.popularmovies.utilities;

import android.net.Uri;

import com.example.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by me74 on 22.02.2018.
 */

public class NetworkUtils {

    public static final String SORT_BY_POPULARITY_DESC = "popularity.desc";
    public static final String SORT_BY_VOTE_AVERAGE_DESC = "vote_average.desc";

    static final String MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/";

    static final String DISCOVER_PATH =
            "discover";

    static final String MOVIE_PATH =
            "movie";

    /*
    * The sort field.
    * Default: results are sorted by popularity if no field is specified.
    */
    private static final String PARAM_SORT = "sort_by";
    /*
    * The API field. The key will be taken from build.gradle, which in turn looks up the key from settings.gradle
    * Either put the key here, or in your settings.gradle file (recommended) like this:
    * API_KEY = "your api key goes here"
    */
    private static final String PARAM_API_KEY = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;


    /*
    * The language field.
    * Default: US-en results are shown
    */
    private static final String PARAM_LANGUAGE = "language";
    private static final String LANGUAGE = "en-US";


    /*
    * The page field.
    * Default: 1 page of results are shown.
    * If this parameter is not in the build, the URL call will show return the first page as default
    */
    private static final String PARAM_PAGE = "page";
    private static final String PAGE_SHOWN = "1";


    /**
     * Builds the URL used to query GitHub.
     *
     * @return The URL to use to query the GitHub.
     */
    public static URL buildUrl(String searchOption) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(DISCOVER_PATH)
                .appendPath(MOVIE_PATH)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .appendQueryParameter(PARAM_SORT, searchOption)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
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
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
