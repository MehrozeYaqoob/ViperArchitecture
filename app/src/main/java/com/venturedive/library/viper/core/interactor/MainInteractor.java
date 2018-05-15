package com.venturedive.library.viper.core.interactor;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.venturedive.library.viper.api.RequestCall;
import com.venturedive.library.viper.core.adapter.MovieAdapter;
import com.venturedive.library.viper.core.contract.MainContract;
import com.venturedive.library.viper.core.entity.Movie;

import java.util.List;

public class MainInteractor implements MainContract.IMainInteractor {

    private MainContract.IMainPresenter presenter;
    private Context context;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private List<Movie> movieList;
    private RequestCall requestCall;

    @Override
    public void attachPresenter(MainContract.IMainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void fetchMovieData(Context context, RecyclerView recyclerView, ProgressDialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
        this.recyclerView = recyclerView;
        this.progressDialog.setMessage("Loading.....");
        this.progressDialog.show();

        requestCall = new RequestCall(new MainContract.RequestCallbacks() {
            @Override
            public void onSuccess(List<Movie> movieList) {
                MainInteractor.this.movieList = movieList;
                populateRecyclerView();
                presenter.prepareMessage("Number of movieList received: " + MainInteractor.this.movieList.size());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Object t) {
                progressDialog.dismiss();
            }
        });
        requestCall.requestAPI();
    }


    private void populateRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        MovieAdapter movieAdapter = new MovieAdapter(context, movieList, recyclerView);
        movieAdapter.interactor = this;
        recyclerView.setAdapter(movieAdapter);
    }

    @Override
    public void onPosterClick(String path) {
        presenter.onPosterClick(path);
    }
}
