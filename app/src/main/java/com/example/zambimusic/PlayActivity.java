package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {
    Button btnNowPlaying,btnMenu,btnPlay,btnPrevious,btnNext;
    ImageView ivAlbumArt;
    TextView tvName,tvArtist,tvAlbum;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();

        Song song = intent.getParcelableExtra("song");


        tvName = findViewById(R.id.tvPlayNmae);
        tvArtist = findViewById(R.id.tvPlayArtist);
        tvAlbum = findViewById(R.id.tvPlayAlbum);
        btnNowPlaying = findViewById(R.id.btnNowPlaying);
        btnMenu = findViewById(R.id.btnMenu);
        btnPlay = findViewById(R.id.btnPlay);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);

        /*Spannable buttonLabel = new SpannableString(" ");
        buttonLabel.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_play_button,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnPlay.setText(buttonLabel);
        buttonLabel.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_left_chevron,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnPrevious.setText(buttonLabel);
        buttonLabel.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_right_chevron,
                ImageSpan.ALIGN_BOTTOM), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        btnNext.setText(buttonLabel);*/
        ivAlbumArt = findViewById(R.id.ivAlbumArt);
        btnPlay.setBackgroundResource(R.drawable.ic_pause_button);


        Picasso.get().load(song.getUriAlbumArt(song.getAlbumId())).error(R.drawable.album).fit().into(ivAlbumArt);

        if(song.getName()!=null){
            tvName.setText(song.getName());
        }
        else{
            tvName.setText("Unknown");
        }

        if(song.getArtists()!=null){
            tvArtist.setText(song.getArtists());
        }
        else{
            tvArtist.setText("Unknown");
        }
        if(song.getAlbum()!=null){
            tvAlbum.setText("Album- "+song.getAlbum());
        }
        else{
            tvAlbum.setText("Unknown");
        }


    }
}
