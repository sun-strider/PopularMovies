package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    private void doMovieDbSearch(URL searchUrl) {
        // The network call. Will crash currently, because it is on the main thread.
        /*
        try {
            NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
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

            mUrlDisplayTextView = findViewById(R.id.url_tv);
            mUrlDisplayTextView.setText(theMovieDbSearchURL.toString());
        } else if (clickedItemId == R.id.action_search_rating) {
            URL theMovieDbSearchURL = NetworkUtils.buildUrl(NetworkUtils.SORT_BY_VOTE_AVERAGE_DESC);

            Log.i(LOG_TAG, theMovieDbSearchURL.toString());

            mUrlDisplayTextView = findViewById(R.id.url_tv);
            mUrlDisplayTextView.setText(theMovieDbSearchURL.toString());
        }
        return true;
    }
}
