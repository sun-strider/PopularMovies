package ch.sunstrider.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.sunstrider.android.popularmovies.data.MovieContract;
import ch.sunstrider.android.popularmovies.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private static final String PARAM_RESULTS = "results";
    private static final String PARAM_KEY = "key";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_AUTHOR = "author";
    private static final String PARAM_CONTENT = "content";

    private static final String TRAILER_BASE_URL = "https://www.youtube.com/watch?v=";

    String[] mVideoKeys;
    String[] mVideoNames;

    String[] mReviewAuthors;
    String[] mReviewContents;
    @BindView(R.id.layout_trailers)
    LinearLayout mTrailersList;
    @BindView(R.id.tv_no_trailers)
    TextView mNoVideosTextView;

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
    @BindView(R.id.iv_star_button)
    ImageView mStarButton;
    @BindView(R.id.tv_review_count)
    TextView mReviewCountTextView;
    @BindView(R.id.tv_authors)
    TextView mAuthorsTextView;
    @BindView(R.id.tv_review)
    TextView mReviewTextView;
    @BindView(R.id.bt_next_review)
    ImageButton mNextReviewButton;
    private String mMovieId = "";
    private int reviewCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent intentThatCalled = getIntent();

        if (intentThatCalled.hasExtra("movieBundle")) {

            Bundle movieBundle = intentThatCalled.getBundleExtra("movieBundle");

            mMovieId = movieBundle.getString(MovieContract.MovieEntry.COLUMN_MOVIE_ID);

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

        new FetchVideosTask().execute();
        new FetchReviewsTask().execute();
    }

    public void extractVideoData(String videoResponse) {

        try {
            JSONObject jsonTrailerObject = new JSONObject(videoResponse);
            JSONArray trailerResults = jsonTrailerObject.getJSONArray(PARAM_RESULTS);
            mVideoKeys = new String[trailerResults.length()];
            mVideoNames = new String[trailerResults.length()];

            for (int i = 0; i < trailerResults.length(); i++) {
                mVideoKeys[i] = trailerResults.getJSONObject(i).optString(PARAM_KEY);
                mVideoNames[i] = trailerResults.getJSONObject(i).optString(PARAM_NAME);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadVideosUI() {

        Log.v(LOG_TAG, "Inside loadVideosUI");

        if (mVideoKeys.length == 0) {
            mNoVideosTextView.setVisibility(View.VISIBLE);

        } else {
            for (int i = 0; i < mVideoKeys.length; i++) {
                Button videoItem = new Button(this);
                videoItem.setText(mVideoNames[i]);
                videoItem.setPadding(0, 30, 0, 30);
                videoItem.setTextSize(14);
                final String trailerUrl = TRAILER_BASE_URL + mVideoKeys[i];

                videoItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri youtubeLink = Uri.parse(trailerUrl);
                        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, youtubeLink);
                        if (youtubeIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(youtubeIntent);
                        }
                    }
                });
                mTrailersList.addView(videoItem);
            }
        }
    }

    public void extractReviews(String reviewsResponse) {

        try {
            JSONObject jsonTrailerObject = new JSONObject(reviewsResponse);
            JSONArray reviewResults = jsonTrailerObject.getJSONArray(PARAM_RESULTS);
            mReviewAuthors = new String[reviewResults.length()];
            mReviewContents = new String[reviewResults.length()];

            for (int i = 0; i < reviewResults.length(); i++) {
                mReviewAuthors[i] = reviewResults.getJSONObject(i).optString(PARAM_AUTHOR);
                mReviewContents[i] = reviewResults.getJSONObject(i).optString(PARAM_CONTENT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadReviewsUI() {

        Log.v(LOG_TAG, "Inside loadReviewsUI");

        if (mReviewContents.length == 0) {

            mReviewTextView.setVisibility(View.GONE);
            mNextReviewButton.setVisibility(View.GONE);
            mAuthorsTextView.setVisibility(View.GONE);

            mReviewCountTextView.setText(R.string.no_reviews);

        } else {
            if (mReviewContents.length == 1) {
                mNextReviewButton.setVisibility(View.GONE);
            }
            String reviewCount = "Review Nr. " + (reviewCounter + 1) + " of " + mReviewContents.length + " by:";
            mReviewCountTextView.setText(reviewCount);
            String authorName = mReviewAuthors[reviewCounter];
            mAuthorsTextView.setText(authorName);
            mReviewTextView.setText(mReviewContents[reviewCounter]);
            mNextReviewButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (reviewCounter < (mReviewContents.length - 1)) {
                        reviewCounter++;
                    } else {
                        reviewCounter = 0;
                    }

                    loadReviewsUI();
                }
            });
        }
    }

    public class FetchVideosTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL videosUrl = NetworkUtils.buildGetMovieVideosUrl(mMovieId);
                return NetworkUtils.getResponseFromHttpUrl(videosUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            extractVideoData(result);
            loadVideosUI();
        }
    }

    public class FetchReviewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL videosUrl = NetworkUtils.buildGetMovieReviewsUrl(mMovieId);
                return NetworkUtils.getResponseFromHttpUrl(videosUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            extractReviews(result);
            loadReviewsUI();
        }
    }
}
