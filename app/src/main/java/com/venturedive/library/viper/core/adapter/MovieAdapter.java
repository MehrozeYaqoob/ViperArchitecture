package com.venturedive.library.viper.core.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.venturedive.library.viper.R;
import com.venturedive.library.viper.core.contract.MainContract;
import com.venturedive.library.viper.core.entity.Movie;

import java.util.List;

public class MovieAdapter extends GroupListAdapter<MovieAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movieList;

    public MovieAdapter(Context activity, List<Movie> movieList, RecyclerView parent) {
        super(activity, movieList.size(), parent);
        this.movieList = movieList;
        this.context = activity;
        animateSummaryView = false;

    }

    @Override
    public void onBindExpandableViewHolder(ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.movieTitle.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        holder.description.setText(movie.getOverview());
        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w600_and_h900_bestv2" + movie.getPosterPath())
                .into(holder.posterImage);

    }

    @Override
    public void onItemExpanded(ViewHolder view) {
        openArrow(view.arrow);
    }

    @Override
    public void onItemCollapsed(ViewHolder view) {
        closeArrow(view.arrow);
    }

    private void openArrow(View view) {
        view.animate().setDuration(150).rotation(90);
    }

    private void closeArrow(View view) {
        view.animate().setDuration(150).rotation(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(parent, R.layout.parent_layout, R.layout.child_layout, R.id.tv_movieTitle, R.id.tv_movieTitle);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public MainContract.IMainInteractor interactor;

    private void onChildViewSelected(ViewHolder viewHolder, int position) {
        Movie movie = movieList.get(position);
        viewHolder.posterImage.setOnClickListener(v -> interactor.onPosterClick(movie.getPosterPath()));
    }

    class ViewHolder extends GroupListAdapter.ViewHolder {

        TextView movieTitle, releaseDate, description;
        ImageView posterImage;
        ImageView arrow;

        ViewHolder(ViewGroup parent, int groupView, int childView, int idIndicatorView, int idSummaryView) {
            super(parent, groupView, childView, idIndicatorView, idSummaryView);
            movieTitle = itemView.findViewById(R.id.tv_movieTitle);
            releaseDate = itemView.findViewById(R.id.tv_releaseDate);
            description = itemView.findViewById(R.id.tv_description);
            posterImage = itemView.findViewById(R.id.iv_poster);
            arrow = itemView.findViewById(R.id.iv_arrow);
            childHolderView.setOnClickListener(v -> onChildViewSelected(this, ViewHolder.this.getAdapterPosition()));
        }
    }
}

