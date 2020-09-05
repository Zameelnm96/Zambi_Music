package com.example.zambimusic.ui2.ivutil;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class NowPlayingIVSmallSetter implements ImageViewSetter  {
    @Override
    public void setImageView(ImageView imageView, Uri uri) {
        Picasso.get().load(uri).centerCrop().fit().into(imageView);
    }
}
