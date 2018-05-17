package com.venturedive.library.viper.core.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.venturedive.library.viper.R;
import com.venturedive.library.viper.core.contract.MainContract;
import com.venturedive.library.viper.core.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.IMainView {

    private MainPresenter mainPresenter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    /*************************** AppCompatActivity Method **********************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        progressDialog = new ProgressDialog(this);

        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        mainPresenter.loadData(this,recyclerView,progressDialog);

    }

    /****************************** MainContract.IMainView Method *****************************/

    public Context getContext(){
        return this;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(ProgressDialog progressDialog) {
        progressDialog.show();
    }

    @Override
    public void dismissDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }

}
