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
import com.venturedive.library.viper.core.presenter.MainPresenter;

import java.util.List;

public class MainInteractor implements MainContract.IMainInteractor {

    private MainPresenter presenter;
    private Context context;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private List<Movie> movieList;
    private RequestCall requestCall;

    /*************************** MainContract.IMainInteractor Method **********************************/

    @Override
    public void attachPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void fetchMovieData(Context context, RecyclerView recyclerView, ProgressDialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
        this.recyclerView = recyclerView;
        this.progressDialog.setMessage("Loading.....");
        this.presenter.showProgressDialog(progressDialog);

        requestCall = new RequestCall(new MainContract.RequestCallbacks() {
            @Override
            public void onSuccess(List<Movie> movieList) {
                MainInteractor.this.movieList = movieList;
                populateRecyclerView();
                presenter.prepareMessage("Number of movieList received: " + MainInteractor.this.movieList.size());
                MainInteractor.this.presenter.dismissProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(Object t) {
                MainInteractor.this.presenter.dismissProgressDialog(progressDialog);
            }
        });
        requestCall.requestAPI();
    }

    @Override
    public void onPosterClick(String path) {
        presenter.onPosterClick(path);
    }

    /*************************** MainInteractor Method **********************************/

    private void populateRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        MovieAdapter movieAdapter = new MovieAdapter(context, movieList, recyclerView);
        movieAdapter.interactor = this;
        recyclerView.setAdapter(movieAdapter);
    }
}
