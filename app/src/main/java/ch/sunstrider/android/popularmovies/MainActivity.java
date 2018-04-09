package ch.sunstrider.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.sunstrider.android.popularmovies.utilities.NetworkUtils;
import ch.sunstrider.android.popularmovies.utilities.OpenMovieJsonUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    // Key for the url
    static final String QUERY_URL_KEY = "query";
    // Key for the query result
    static final String JSON_RESULT_KEY = "result";
    // Tag name for the activity
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static URL theMovieDbSearchURL = null;

    private static String movieDbSearchResults = null;

    @BindView(R.id.rv_movie)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_error_message_display)
    TextView mErrorMessageTextView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //calculate the number of columns with a desired column width
        int nrOfColumns = MovieAdapter.calculateNoOfColumns(this);

        // create a new layout manager. at this point, it will be a linear layout manager, vertical orientation
        // DONE: change to grid layout manager when linear is working properly
        GridLayoutManager layoutManager = new GridLayoutManager(this, nrOfColumns);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);

        // the layout manager is set on the recycler view
        mRecyclerView.setLayoutManager(layoutManager);

        // set that all items in the list will have fixed size
        mRecyclerView.setHasFixedSize(true);

        // a new adapter will be created and stored in mMovieAdapter
        mMovieAdapter = new MovieAdapter(this);

        // the adapter is set on the recycler view
        mRecyclerView.setAdapter(mMovieAdapter);

        // set the adapter to null before doing the search again
        mMovieAdapter.setMovieData(null);

        new MovieDbQueeryTask().execute(savedInstanceState);
    }


    private String doMovieDbSearch(URL searchUrl) {
        try {
            return NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showMovieDataView() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);

        // the recycler view is shown instead of the the fixed text view
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        // the recycler view is hidden
        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedItemId = item.getItemId();
        if (clickedItemId == R.id.action_search_popular) {
            theMovieDbSearchURL = NetworkUtils.buildGetMoviesUrl(NetworkUtils.PATH_POPULAR);

            Log.i(LOG_TAG, theMovieDbSearchURL.toString());

            // set the adapter to null before doing the search again
            mMovieAdapter.setMovieData(null);

            new MovieDbQueeryTask().execute((Bundle) null);

        } else if (clickedItemId == R.id.action_search_rating) {
            theMovieDbSearchURL = NetworkUtils.buildGetMoviesUrl(NetworkUtils.PATH_TOP_RATED);

            Log.i(LOG_TAG, theMovieDbSearchURL.toString());

            // set the adapter to null before doing the search again
            mMovieAdapter.setMovieData(null);

            new MovieDbQueeryTask().execute((Bundle) null);
        }
        return true;
    }

    @Override
    public void onClick(Bundle currentMovieBundle) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movieBundle", currentMovieBundle);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // put the query URL in the bundle
        outState.putString(QUERY_URL_KEY, theMovieDbSearchURL.toString());

        // put the JSON search result in the bundle
        outState.putString(JSON_RESULT_KEY, movieDbSearchResults);
    }

    private class MovieDbQueeryTask extends AsyncTask<Bundle, Void, ContentValues[]> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ContentValues[] doInBackground(Bundle... bundles) {

            if (theMovieDbSearchURL == null) {
                theMovieDbSearchURL = NetworkUtils.buildGetMoviesUrl(NetworkUtils.PATH_POPULAR);
            }

            Bundle savedState = bundles[0];

            if (savedState != null) {

                if (savedState.containsKey(JSON_RESULT_KEY)) {
                    return OpenMovieJsonUtils.parseMovieDbJson(savedState.getString(JSON_RESULT_KEY));
                }

                if (savedState.containsKey(QUERY_URL_KEY)) {
                    try {
                        theMovieDbSearchURL = new URL(savedState.getString(QUERY_URL_KEY));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }

            // do the http request and store the JSON result in a string
            movieDbSearchResults = doMovieDbSearch(theMovieDbSearchURL);

            // parse the JSON string and return a content values array
            return OpenMovieJsonUtils.parseMovieDbJson(movieDbSearchResults);
        }

        @Override
        protected void onPostExecute(ContentValues[] movieData) {

            mProgressBar.setVisibility(View.INVISIBLE);

            if (movieData != null) {
                mMovieAdapter.setMovieData(movieData);
                showMovieDataView();
            } else {
                showErrorMessage();
            }
        }
    }
}
