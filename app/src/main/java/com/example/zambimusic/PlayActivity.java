package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, PlayService.ServiceCallback {
    Button btnNowPlaying,btnMenu,btnPlay,btnPrevious,btnNext;
    ImageView ivAlbumArt;
    TextView tvName,tvArtist,tvAlbum;
    private SeekBar seekBar;
    ArrayList<Song> songs;
    PlayService playService;
    boolean isBounded;
    int position;
    Intent playIntent;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
            playService = myBinder.getService();
            playService.setList(songs);
            playService.setPosition(position);
            playService.setSong();
            playService.setServiceCallback(PlayActivity.this);
            seekBar.setMax(playService.getMediaPlayer().getDuration());
            final Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread
            PlayActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(playService.getMediaPlayer() != null){
                        int mCurrentPosition = playService.getMediaPlayer() .getCurrentPosition() ;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
            isBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBounded = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        tvName = findViewById(R.id.tvPlayNmae);
        tvArtist = findViewById(R.id.tvPlayArtist);
        tvAlbum = findViewById(R.id.tvPlayAlbum);
        btnNowPlaying = findViewById(R.id.btnNowPlaying);
        btnMenu = findViewById(R.id.btnMenu);
        btnPlay = findViewById(R.id.btnPlay);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        ivAlbumArt = findViewById(R.id.ivAlbumArt);
        seekBar = findViewById(R.id.seekbarPlayActivity);
        seekBar.setOnSeekBarChangeListener(this);
        btnPlay.setBackgroundResource(R.drawable.ic_pause_button);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playService.togglePlay();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playService.playNext();


            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playService.playPrevious();

            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        songs =(ArrayList<Song>) bundle.getSerializable("songs");
        position = intent.getIntExtra("index",0);
        Song song = songs.get(position);
        playIntent = new Intent(this,PlayService.class);
        bindService(playIntent,serviceConnection,Context.BIND_AUTO_CREATE);


        /*Song song = intent.getParcelableExtra("song");*/




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



        setView(song);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void setView(Song song){
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
    int progress;
    MediaPlayer mediaPlayer;
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.progress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        mediaPlayer = playService.getMediaPlayer();
        mediaPlayer.seekTo(progress);

    }

    @Override
    public void callSetView(Song song) {
        setView(song);
    }
}
