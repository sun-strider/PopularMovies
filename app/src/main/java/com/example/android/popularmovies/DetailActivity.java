package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;

public class DetailActivity extends AppCompatActivity {
    // TODO: restyle details activity to show all info in separate views.

    /**
     * TODO: before this, a different approach to store the movie data needs to be implemented,
     * as the individual info of the movie needs to be retrieved
     **/

    private TextView mDetailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDetailTextView = findViewById(R.id.tv_display);

        Intent intentThatCalled = getIntent();

        if (intentThatCalled.hasExtra("movieBundle")) {

            Bundle movieBundle = intentThatCalled.getBundleExtra("movieBundle");
            String title = movieBundle.getString(MovieContract.MovieEntry.COLUMN_TITLE);

            mDetailTextView.setText(title);
        }
    }
}
