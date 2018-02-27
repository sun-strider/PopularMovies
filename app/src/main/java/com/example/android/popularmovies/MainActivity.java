package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

        URL theMovieDbSearchURL = NetworkUtils.buildUrl();

        Log.i(LOG_TAG, theMovieDbSearchURL.toString());

        mUrlDisplayTextView = findViewById(R.id.url_tv);
        mUrlDisplayTextView.setText(theMovieDbSearchURL.toString());


        // The network call. Will crash currently, because it is on the main thread.
        /*
        try {
            NetworkUtils.getResponseFromHttpUrl(theMovieDbSearchURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }
}
