package com.venturedive.library.viper.core.interactor;

import android.content.Intent;

import com.venturedive.library.viper.core.contract.PosterContract;
import com.venturedive.library.viper.core.entity.PosterImage;
import com.venturedive.library.viper.databinding.ActivityPosterBinding;

public class PosterInteractor implements PosterContract.IPosterInteractor {

    private PosterContract.IPosterPresenter posterPresenter;
    private final PosterImage posterImage;

    public PosterInteractor(ActivityPosterBinding posterBinding) {
        this.posterImage = new PosterImage();
        posterBinding.setPoster(posterImage);
    }

    @Override
    public void attachPresenter(PosterContract.IPosterPresenter posterPresenter) {
        this.posterPresenter = posterPresenter;
    }

    @Override
    public void showPoster(Intent intent) {
        posterImage.setImageUrl(intent.getStringExtra("url"));
        posterPresenter.preparePosterMessage("Poster");

    }
}
