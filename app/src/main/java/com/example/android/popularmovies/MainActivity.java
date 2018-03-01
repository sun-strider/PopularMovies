package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;

    private TextView mErrorMessageTextView;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUrlDisplayTextView = findViewById(R.id.url_tv);
        mSearchResultsTextView = findViewById(R.id.json_tv);

        mErrorMessageTextView = findViewById(R.id.tv_error_message_display);

        mProgressBar = findViewById(R.id.pb_loading_indicator);
    }


    private String doMovieDbSearch(URL searchUrl) {
        try {
            return NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showJsonDataView() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
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

            new MovieDbQueeryTask().execute(theMovieDbSearchURL);

        } else if (clickedItemId == R.id.action_search_rating) {
            URL theMovieDbSearchURL = NetworkUtils.buildUrl(NetworkUtils.SORT_BY_VOTE_AVERAGE_DESC);

            Log.i(LOG_TAG, theMovieDbSearchURL.toString());

            mUrlDisplayTextView.setText(theMovieDbSearchURL.toString());

            new MovieDbQueeryTask().execute(theMovieDbSearchURL);
        }
        return true;
    }

    private class MovieDbQueeryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String movieDbSearchResults = null;

            // do the http request and store the JSON result in a string
            movieDbSearchResults = doMovieDbSearch(searchUrl);

            // parse the JSON string and return a string arraylist
            List<String> parsedMovieData = new ArrayList<String>();
            parsedMovieData = OpenMovieJsonUtils.parseMovieDbJson(movieDbSearchResults);

            String moviesString = "";

            for (int i = 0; i < parsedMovieData.size(); i++) {
                moviesString = moviesString + parsedMovieData.get(i);
            }

            return moviesString;
        }

        @Override
        protected void onPostExecute(String s) {

            mProgressBar.setVisibility(View.INVISIBLE);

            if (s != null && s != "") {
                mSearchResultsTextView.setText(s);
                showJsonDataView();
            } else {
                showErrorMessage();
            }
        }
    }
}
