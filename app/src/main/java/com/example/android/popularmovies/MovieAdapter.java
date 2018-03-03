package com.example.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by me74 on 01.03.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    final private MovieAdapterOnClickHandler mClickHandler;
    private List<String> mMovieData;

    public MovieAdapter(MovieAdapterOnClickHandler onClickHandler) {
        mClickHandler = onClickHandler;
    }

    // class to calculate the number of columns for a grid with a given column width
    // adapted from https://stackoverflow.com/a/38472370
    public static int calculateNoOfColumns(Context context, int maxWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / maxWidth);
        return noOfColumns;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // get the layout for the movie list item
        int layoutIdForListItem = R.layout.movie_list_item;
        // get the layout inflater
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // bool that decides attachment behaviour
        boolean attachToParentNow = false;

        // inflate the view with the above info
        View view = inflater.inflate(layoutIdForListItem, parent, attachToParentNow);

        // return new view holder and pass in the inflated view
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {

        // get the current movie information
        String currentMovie = mMovieData.get(position);

        // get the movie item text view from the holder and set the information of the current movie
        holder.movieListItemTextView.setText(currentMovie);
    }

    @Override
    public int getItemCount() {
        // check if movie data exists. if not return 0 as item count, else return the length of the array
        if (mMovieData == null) {
            return 0;
        }
        return mMovieData.size();
    }

    // a method that sets the movie data to mMovieData
    void setMovieData(List<String> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    interface MovieAdapterOnClickHandler {
        void onClick(String currentMovie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // create a TextView variable in which the movie data will be stored
        final TextView movieListItemTextView;

        public MovieViewHolder(View itemView) {
            super(itemView);

            // in the constructor of the view holder, the movie item view is set
            movieListItemTextView = itemView.findViewById(R.id.tv_movie_data);

            // set the click listener to the list item
            movieListItemTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String currentMovie = mMovieData.get(adapterPosition);
            mClickHandler.onClick(currentMovie);
        }
    }
}
