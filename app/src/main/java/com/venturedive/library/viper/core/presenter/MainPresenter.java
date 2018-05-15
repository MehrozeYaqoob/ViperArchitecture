package com.venturedive.library.viper.core.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.venturedive.library.viper.core.contract.MainContract;
import com.venturedive.library.viper.core.interactor.MainInteractor;
import com.venturedive.library.viper.core.router.MainRouter;

public class MainPresenter implements MainContract.IMainPresenter {

    private MainContract.IMainView iMainView;
    private final MainContract.IMainInteractor iMainInteractor;
    private final MainContract.IMainRouter iMainRouter;

    public MainPresenter() {
        iMainInteractor = new MainInteractor();
        iMainInteractor.attachPresenter(this);
        iMainRouter = new MainRouter();

    }

    @Override
    public void attachView(MainContract.IMainView view) {
        this.iMainView = view;
    }

    @Override
    public void loadData(Context context, RecyclerView recyclerView, ProgressDialog progressDialog) {
        iMainInteractor.fetchMovieData(context, recyclerView,progressDialog);
    }

    public void prepareMessage(String data) {
        iMainView.showMessage(data);
    }

    @Override
    public void onPosterClick(String path) {
        iMainRouter.showPoster(path, iMainView.getContext());
    }
}
