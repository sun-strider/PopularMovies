package ch.sunstrider.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by me74 on 03.03.2018.
 */

@SuppressWarnings("DefaultFileTemplate")
public class MovieContract {

    /*
 * The "Content authority" is a name for the entire content provider, similar to the
 * relationship between a domain name and its website. A convenient string to use for the
 * content authority is the package name for the app, which is guaranteed to be unique on the
 * Play Store.
 */
    public static final String CONTENT_AUTHORITY = "ch.sunstrider.android.popularmovies";

    /*
  * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
  * the content provider for Sunshine.
  */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        /* Used internally as the name of our table. */
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "original_title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_IS_FAVORITE = "favorite";

    }

}
