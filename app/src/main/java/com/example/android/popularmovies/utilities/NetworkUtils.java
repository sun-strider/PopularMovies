package com.example.android.popularmovies.utilities;

/**
 * Created by me74 on 22.02.2018.
 */

public class NetworkUtils {

    final static String MOVIE_BASE_URL =
            "https://api.themoviedb.org/3/discover/movie";

    final static String MOVIE_DISCOVER_PATH =
            "discover/movie";

    final static String PARAM_QUERY = "q";

    /*
     * The sort field. By popularity or vote average
     */
    final static String PARAM_SORT = "sort_by";
    final static String sortBy_Popularity_Desc = "popularity.desc";
    final static String sortBy_Vote_Desc = "vote_average.desc";

}
