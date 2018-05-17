package com.venturedive.library.viper.core.presenter;

import android.content.Intent;

import com.venturedive.library.viper.core.contract.PosterContract;
import com.venturedive.library.viper.core.interactor.PosterInteractor;
import com.venturedive.library.viper.databinding.ActivityPosterBinding;

public class PosterPresenter implements PosterContract.IPosterPresenter {

    private PosterContract.IPosterView posterView;
    private final PosterContract.IPosterInteractor posterInteractor;

    public PosterPresenter(ActivityPosterBinding binding) {

        this.posterInteractor = new PosterInteractor(binding);
        this.posterInteractor.attachPresenter(this);
    }

    /*************************** PosterContract.IPosterPresenter Method **********************************/

    @Override
    public void attachView(PosterContract.IPosterView view) {
        this.posterView = view;
    }

    @Override
    public void onPosterDataReceived(Intent intent) {
        posterInteractor.showPoster(intent);
    }

    @Override
    public void preparePosterMessage(String str) {
        posterView.showMessage(str);
    }
}
