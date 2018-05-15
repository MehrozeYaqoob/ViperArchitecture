package com.venturedive.library.viper.core.entity;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PosterImage  {
    private String imageUrl;

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String imageUrl) {

        Picasso.with(view.getContext())
                .load("https://image.tmdb.org/t/p/w600_and_h900_bestv2" + imageUrl)
                .into(view);
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
