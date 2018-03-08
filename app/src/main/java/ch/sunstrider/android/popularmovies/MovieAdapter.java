package ch.sunstrider.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.sunstrider.android.popularmovies.data.MovieContract;
import ch.sunstrider.android.popularmovies.utilities.NetworkUtils;

/**
 * Created by me74 on 01.03.2018.
 */

@SuppressWarnings("DefaultFileTemplate")
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // the desired width of a column in the grid
    private static final int COLUMN_WIDTH = 180;

    private final MovieAdapterOnClickHandler mClickHandler;

    private ContentValues[] mMovieContentValues;

    public MovieAdapter(MovieAdapterOnClickHandler onClickHandler) {
        mClickHandler = onClickHandler;
    }

    // class to calculate the number of columns for a grid with a given column width
    // adapted from https://stackoverflow.com/a/38472370
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / COLUMN_WIDTH);
        return noOfColumns;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // get the layout for the movie list item
        int layoutIdForListItem = R.layout.movie_list_item;
        // get the layout inflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // bool that decides attachment behaviour
        boolean attachToParentNow = false;

        // inflate the view with the above info
        @SuppressWarnings("ConstantConditions") View view = inflater.inflate(layoutIdForListItem, parent, attachToParentNow);

        // return new view holder and pass in the inflated view
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder holder, int position) {

        // get the current movie information
        ContentValues currentMovieValues = mMovieContentValues[position];

        String posterPath = currentMovieValues.getAsString(MovieContract.MovieEntry.COLUMN_POSTER_PATH);

        String posterUrl = NetworkUtils.buildMoviePosterUrl(posterPath).toString();

        // set the poster image to the movie item image view
        if (posterUrl != null) {
            Picasso.with(holder.movieListItemPosterImageView.getContext())
                    .load(posterUrl)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.movieListItemPosterImageView);
        }
    }

    @Override
    public int getItemCount() {
        // check if movie data exists. if not return 0 as item count, else return the length of the array
        if (mMovieContentValues == null) {
            return 0;
        }
        return mMovieContentValues.length;
    }

    // a method that sets the movie data to mMovieData
    void setMovieData(ContentValues[] movieData) {
        mMovieContentValues = movieData;
        notifyDataSetChanged();
    }

    interface MovieAdapterOnClickHandler {
        void onClick(Bundle currentMovieBundle);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        // create an ImageView variable in which the movie poster will be stored
        @BindView(R.id.iv_grid_movie_poster)
        ImageView movieListItemPosterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            // set the click listener to the list item
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ContentValues currentMovie = mMovieContentValues[adapterPosition];

            // make a bundle out of the current movie so it can be passed with the intent
            Bundle movieBundle = new Bundle();
            movieBundle.putString(MovieContract.MovieEntry.COLUMN_TITLE, currentMovie.getAsString(MovieContract.MovieEntry.COLUMN_TITLE));
            movieBundle.putString(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, currentMovie.getAsString(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
            movieBundle.putString(MovieContract.MovieEntry.COLUMN_POSTER_PATH, currentMovie.getAsString(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
            movieBundle.putString(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, currentMovie.getAsString(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
            movieBundle.putString(MovieContract.MovieEntry.COLUMN_OVERVIEW, currentMovie.getAsString(MovieContract.MovieEntry.COLUMN_OVERVIEW));

            mClickHandler.onClick(movieBundle);
        }
    }
}
