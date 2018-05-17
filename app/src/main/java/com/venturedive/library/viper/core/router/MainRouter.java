package com.venturedive.library.viper.core.router;

import android.content.Context;
import android.content.Intent;

import com.venturedive.library.viper.core.contract.MainContract;
import com.venturedive.library.viper.core.view.PosterActivity;

public class MainRouter implements MainContract.IMainRouter {

    /*************************** MainContract.IMainRouter Method **********************************/

    @Override
    public void showPoster(String path, Context context) {
        Intent intent = new Intent(context, PosterActivity.class);
        intent.putExtra("url", path);
        context.startActivity(intent);
    }
}
