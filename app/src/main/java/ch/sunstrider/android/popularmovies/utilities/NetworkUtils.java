package ch.sunstrider.android.popularmovies.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import ch.sunstrider.android.popularmovies.BuildConfig;

/**
 * Created by me74 on 22.02.2018.
 */

@SuppressWarnings("DefaultFileTemplate")
public class NetworkUtils {


    // Search path for top rated movies. Needs to be appended after base url & movie path
    public static final String PATH_TOP_RATED =
            "top_rated";

    // Search path for most popular movies. Needs to be appended after base url & movie path
    public static final String PATH_POPULAR =
            "popular";
    // Search path to get a movies reviews. Needs to be appended after base url & movie path & movie ID
    public static final String PATH_REVIEWS =
            "reviews";
    // Search path to get a movies reviews. Needs to be appended after base url & movie path & movie ID
    public static final String PATH_VIDEOS =
            "videos";
    // The base url for any The Movie DB query
    static final String MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/";
    // The movie path. Movie searches need this to be appended to the base path
    static final String PATH_MOVIE =
            "movie";
    // The Base URL to get the movie poster. The poster path in the JSON response for the movie search needs
    // to be appended to this, after the size. It needs to appended as encoded path.
    static final String POSTER_BASE_URL =
            "https://image.tmdb.org/t/p/";
    // The size for the movie poster. Needs to be appended to the base path when building the poster URL
    static final String PATH_POSTER_SIZE_STANDARD =
            "w780";
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
     * Builds the URL used to query The Movie DB.
     *
     * @return The URL which starts the the requested search.
     */
    public static URL buildGetMoviesUrl(String searchOption) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(searchOption)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .appendQueryParameter(PARAM_PAGE, PAGE_SHOWN)
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
     * Builds the URL used to get the movie poster
     *
     * @return The URL which shows the movie poster
     */
    public static URL buildMoviePosterUrl(String posterPath) {
        Uri builtUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendPath(PATH_POSTER_SIZE_STANDARD)
                .appendEncodedPath(posterPath)
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
     * Builds the URL used to get a movies reviews
     *
     * @return The URL which starts the the requested search.
     */
    public static URL buildGetMovieReviewsUrl(String movieId) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(movieId)
                .appendPath(PATH_REVIEWS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
                .appendQueryParameter(PARAM_PAGE, PAGE_SHOWN)
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
     * Builds the URL used to get a movies reviews
     *
     * @return The URL which starts the the requested search.
     */
    public static URL buildGetMovieVideosUrl(String movieId) {
        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendPath(PATH_MOVIE)
                .appendPath(movieId)
                .appendPath(PATH_VIDEOS)
                .appendQueryParameter(PARAM_API_KEY, API_KEY)
                .appendQueryParameter(PARAM_LANGUAGE, LANGUAGE)
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
