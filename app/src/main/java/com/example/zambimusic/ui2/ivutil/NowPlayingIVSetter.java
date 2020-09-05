package com.example.zambimusic.ui2.ivutil;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

public class NowPlayingIVSetter implements ImageViewSetter {
    @Override
    public void setImageView(ImageView imageView, Uri uri) {
        ArrayList<Transformation> transformations = new ArrayList<>();
        transformations.add(new GrayscaleTransformation());// refer here https://github.com/wasabeef/picasso-transformations/blob/master/example/src/main/java/jp/wasabeef/example/picasso/MainAdapter.java
        Picasso.get().load(uri).transform(transformations).centerCrop().fit().into(imageView);
    }


}
