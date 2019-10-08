package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PlayActivity extends AppCompatActivity {
    Button btnNowPlaying,btnMenu,btnPlay,btnPrevious,btnNext;
    ImageView ivAlbumArt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        btnNowPlaying = findViewById(R.id.btnNowPlaying);
        btnMenu = findViewById(R.id.btnMenu);
        btnPlay = findViewById(R.id.btnPlay);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        Spannable buttonLabel = new SpannableString(" ");
        buttonLabel.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_play_button,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnPlay.setText(buttonLabel);
        buttonLabel.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_left_chevron,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnPrevious.setText(buttonLabel);
        buttonLabel.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_right_chevron,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnNext.setText(buttonLabel);
        ivAlbumArt = findViewById(R.id.ivAlbumArt);
        Picasso.get().load(R.drawable.album).fit().into(ivAlbumArt);


    }
}
