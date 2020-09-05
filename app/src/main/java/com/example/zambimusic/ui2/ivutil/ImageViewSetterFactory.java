package com.example.zambimusic.ui2.ivutil;

import android.net.Uri;
import android.widget.ImageView;


import com.example.zambimusic.data.Constants;
import com.squareup.picasso.Picasso;

public class ImageViewSetterFactory {
    public static ImageViewSetter getImageViewSetter(int type){

        if(type == Constants.NOW_PLAYING_IV_LARGE) return new NowPlayingIVSetter();
        else if(type == Constants.NOW_PLAYING_IV_SMALL){
            return new NowPlayingIVSmallSetter();
        }
        else {
            return new ImageViewSetter() {
                @Override
                public void setImageView(ImageView imageView, Uri uri) {
                    Picasso.get().load(uri).into(imageView);
                }
            };
        }
    }
}
