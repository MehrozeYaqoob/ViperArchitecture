package com.venturedive.library.viper.api;

import com.venturedive.library.viper.core.contract.MainContract;
import com.venturedive.library.viper.core.entity.MoviesResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class RequestCall {

    private final MainContract.RequestCallbacks requestCallbacks;

    public RequestCall(MainContract.RequestCallbacks requestCallbacks) {
        this.requestCallbacks = requestCallbacks;
    }

    public void requestAPI() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call = apiService.getNowPlayingMovies(Config.apiKey);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, retrofit2.Response<MoviesResponse> response) {
                int statusCode = response.code();
                if (statusCode == Config.RESPONSE_OK) {
                    requestCallbacks.onSuccess(response.body().getResults());
                } else {
                    requestCallbacks.onFailure("On Failure - " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable exception) {
                // Log error here since request failed
                requestCallbacks.onFailure(exception.toString());
            }
        });
    }
}