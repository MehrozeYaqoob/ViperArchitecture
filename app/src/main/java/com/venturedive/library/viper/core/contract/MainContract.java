package com.venturedive.library.viper.core.contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.venturedive.library.viper.core.entity.Movie;

import java.util.List;

public interface MainContract {

    interface IMainView {
        void showMessage(String str);
        Context getContext();
    }
    interface IMainPresenter {
        void attachView(IMainView view);
        void loadData(Context context, RecyclerView recyclerView, ProgressDialog progressDialog);
        void onPosterClick(String path);
        void prepareMessage(String str);
    }
    interface IMainInteractor {
        void attachPresenter(IMainPresenter presenter);
        void fetchMovieData(Context context, RecyclerView recyclerView, ProgressDialog progressDialog);
        void onPosterClick(String path);
    }
    interface IMainRouter {
        void showPoster(String path, Context context);
    }
    interface RequestCallbacks {
        void onSuccess(List<Movie> movieList);
        void onFailure(Object exception);
    }
}
