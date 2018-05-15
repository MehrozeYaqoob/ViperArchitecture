package com.venturedive.library.viper.core.contract;

import android.content.Intent;

public interface PosterContract {

    interface IPosterView{
        void showMessage(String message);
    }

    interface IPosterPresenter{
        void attachView(PosterContract.IPosterView view);
        void onPosterDataReceived(Intent intent);
        void preparePosterMessage(String message);
    }

    interface IPosterInteractor {
        void attachPresenter(PosterContract.IPosterPresenter posterPresenter);
        void showPoster(Intent intent);
    }
}
