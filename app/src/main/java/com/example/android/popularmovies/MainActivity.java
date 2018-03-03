package com.example.android.popularmovies;

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

import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.OpenMovieJsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    // the desired width of a column in the grid
    private static final int COLUMN_WIDTH = 180;

    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    private TextView mUrlDisplayTextView;

    private TextView mErrorMessageTextView;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrlDisplayTextView = findViewById(R.id.url_tv);

        // get e reference to the RecyclerView
        mRecyclerView = findViewById(R.id.rv_movie);

        // TextView to display an error message. will be hidden if there are no errors
        mErrorMessageTextView = findViewById(R.id.tv_error_message_display);

        // loading progress bar
        mProgressBar = findViewById(R.id.pb_loading_indicator);

        //calculate the number of columns with a desired column width
        int nrOfColumns = MovieAdapter.calculateNoOfColumns(this, COLUMN_WIDTH);

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
        mMovieAdapter = new MovieAdapter();

        // the adapter is set on the recycler view
        mRecyclerView.setAdapter(mMovieAdapter);
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
            URL theMovieDbSearchURL = NetworkUtils.buildUrl(NetworkUtils.SORT_BY_POPULARITY_DESC);

            Log.i(LOG_TAG, theMovieDbSearchURL.toString());

            mUrlDisplayTextView.setText(theMovieDbSearchURL.toString());

            // set the adapter to null before doing the search again
            mMovieAdapter.setMovieData(null);

            new MovieDbQueeryTask().execute(theMovieDbSearchURL);

        } else if (clickedItemId == R.id.action_search_rating) {
            URL theMovieDbSearchURL = NetworkUtils.buildUrl(NetworkUtils.SORT_BY_VOTE_AVERAGE_DESC);

            Log.i(LOG_TAG, theMovieDbSearchURL.toString());

            mUrlDisplayTextView.setText(theMovieDbSearchURL.toString());

            // set the adapter to null before doing the search again
            mMovieAdapter.setMovieData(null);

            new MovieDbQueeryTask().execute(theMovieDbSearchURL);
        }
        return true;
    }

    private class MovieDbQueeryTask extends AsyncTask<URL, Void, List<String>> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<String> doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String movieDbSearchResults = null;

            // do the http request and store the JSON result in a string
            movieDbSearchResults = doMovieDbSearch(searchUrl);

            // parse the JSON string and return a string arraylist
            List<String> parsedMovieData = new ArrayList<String>();
            parsedMovieData = OpenMovieJsonUtils.parseMovieDbJson(movieDbSearchResults);

            return parsedMovieData;
        }

        @Override
        protected void onPostExecute(List<String> movieData) {

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
