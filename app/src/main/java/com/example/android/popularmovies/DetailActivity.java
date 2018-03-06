package com.example.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    // TODO: restyle details activity to show all info in separate views.

    /**
     * TODO: before this, a different approach to store the movie data needs to be implemented,
     * as the individual info of the movie needs to be retrieved
     **/

    private ImageView mPosterImageView;

    private TextView mTitleTextView;

    private TextView mReleaseDateTextView;

    private TextView mVoteTextView;

    private TextView mOverviewTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPosterImageView = findViewById(R.id.iv_detail_movie_poster);

        mTitleTextView = findViewById(R.id.tv_title);

        mReleaseDateTextView = findViewById(R.id.tv_release_date);

        mVoteTextView = findViewById(R.id.tv_vote_average);

        mOverviewTextView = findViewById(R.id.tv_overview);

        Intent intentThatCalled = getIntent();

        if (intentThatCalled.hasExtra("movieBundle")) {

            Bundle movieBundle = intentThatCalled.getBundleExtra("movieBundle");

            String posterPath = movieBundle.getString(MovieContract.MovieEntry.COLUMN_POSTER_PATH);

            String title = movieBundle.getString(MovieContract.MovieEntry.COLUMN_TITLE);
            String releaseDate = movieBundle.getString(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            String voteAverage = movieBundle.getString(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
            String overview = movieBundle.getString(MovieContract.MovieEntry.COLUMN_OVERVIEW);

            mTitleTextView.setText(title);
            mReleaseDateTextView.setText(releaseDate);
            mVoteTextView.setText(voteAverage);
            mOverviewTextView.setText(overview);

            String posterUrl = NetworkUtils.buildMoviePosterUrl(posterPath).toString();

            // set the poster image to the movie item image view
            if (posterUrl != null) {
                Picasso.with(this)
                        .load(posterUrl)
                        .into(mPosterImageView);
            }
        }
    }
}
