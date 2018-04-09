package ch.sunstrider.android.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.sunstrider.android.popularmovies.data.MovieContract;
import ch.sunstrider.android.popularmovies.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    // Binding of Views
    @BindView(R.id.iv_detail_movie_poster)
    ImageView mPosterImageView;
    @BindView(R.id.tv_title)
    TextView mTitleTextView;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDateTextView;
    @BindView(R.id.tv_vote_average)
    TextView mVoteTextView;
    @BindView(R.id.tv_overview)
    TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

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

            Log.v(LOG_TAG, posterPath);
            Log.v(LOG_TAG, posterUrl);

            // set the poster image to the movie item image view
            if (posterPath != null) {
                Picasso.with(this)
                        .load(posterUrl)
                        .error(R.mipmap.ic_launcher)
                        .into(mPosterImageView);
            }
        }
    }
}