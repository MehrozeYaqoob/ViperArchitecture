package com.venturedive.library.viper.core.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.venturedive.library.viper.R;
import com.venturedive.library.viper.core.contract.PosterContract;
import com.venturedive.library.viper.core.presenter.PosterPresenter;
import com.venturedive.library.viper.databinding.ActivityPosterBinding;

public class PosterActivity extends AppCompatActivity implements PosterContract.IPosterView{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPosterBinding activityPosterBinding = DataBindingUtil.setContentView(this, R.layout.activity_poster);

        PosterPresenter posterPresenter = new PosterPresenter(activityPosterBinding);
        posterPresenter.attachView(this);
        posterPresenter.onPosterDataReceived(getIntent());
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
