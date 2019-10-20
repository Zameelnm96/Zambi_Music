package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, PlayService.ServiceCallback {
    Button btnNowPlaying,btnMenu;
    ImageButton btnPlay,btnPrevious,btnNext,btnShuffle,btnRepeat;
    ImageView ivAlbumArt;
    TextView tvName,tvArtist,tvAlbum,tvTime,tvDuration;
    private SeekBar seekBar;
    ArrayList<Song> songs;
    PlayService playService;
    boolean isBounded;
    int position;
    Intent playIntent;
    Handler handler =new Handler();
    boolean isServiceConnection2Set,isServiceConnectionSet = false;
    ServiceConnection serviceConnection2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceConnection2Set = true;
            isServiceConnectionSet = false;
            PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
            playService = myBinder.getService();
            playService.setServiceCallback(PlayActivity.this);
            songs = playService.getSongs();
            setView(playService.getSong());

            if (playService.getIsShuffle()){
                btnShuffle.setBackgroundResource(R.drawable.ic_shuffle);
            }
            else{
                btnShuffle.setBackgroundResource(R.drawable.ic_not_shuffle);
            }
            if(playService.getIsepeated()){

                btnRepeat.setBackgroundResource(R.drawable.ic_repeat);
            }
            else{
                btnRepeat.setBackgroundResource(R.drawable.ic_not_repeat);
            }
            if (playService.getMediaPlayer().isPlaying()){
                btnPlay.setBackgroundResource(R.drawable.ic_pause_button);
            }
            else {
                btnPlay.setBackgroundResource(R.drawable.ic_play_button);
            }
            updateSeekbar();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnection2Set = false;
        }
    };
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceConnectionSet = true;
            isServiceConnection2Set = false;
            PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
            playService = myBinder.getService();
            /*playService.setPosition(position);
            playService.setSong();*/
            setView(playService.getSong());
            updateSeekbar();
            playService.setServiceCallback(PlayActivity.this);
            isBounded = true;
            if (playService.getIsShuffle()){
                btnShuffle.setBackgroundResource(R.drawable.ic_shuffle);
            }
            else{

                btnShuffle.setBackgroundResource(R.drawable.ic_not_shuffle);
            }
            if(playService.getIsepeated()){

                btnRepeat.setBackgroundResource(R.drawable.ic_repeat);
            }
            else{
                btnRepeat.setBackgroundResource(R.drawable.ic_not_repeat);
            }
            if (playService.getMediaPlayer().isPlaying()){
                btnPlay.setBackgroundResource(R.drawable.ic_pause_button);
            }
            else {
                btnPlay.setBackgroundResource(R.drawable.ic_play_button);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnectionSet = false;
            isBounded = false;
        }
    };



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        tvName = findViewById(R.id.tvPlayNamePlayActivity);
        tvArtist = findViewById(R.id.tvArtistPlayActivity);
        tvAlbum = findViewById(R.id.tvAlbumPlayActivity);
        btnNowPlaying = findViewById(R.id.btnNowPlayingPlayActivity);
        btnMenu = findViewById(R.id.btnMenuPlayActivity);
        btnPlay = findViewById(R.id.btnPlayPlayActivity);
        btnPrevious = findViewById(R.id.btnPreviousPlayActivity);
        btnNext = findViewById(R.id.btnNextPlayActivity);
        ivAlbumArt = findViewById(R.id.ivAlbumArtPlayActivity);
        seekBar = findViewById(R.id.seekBarPlayActivity);
        btnShuffle = findViewById(R.id.btnShufflePlayActivity);
        btnRepeat = findViewById(R.id.btnRepeatPlayActivity);
        seekBar.setOnSeekBarChangeListener(this);
        btnPlay.setBackgroundResource(R.drawable.ic_pause_button);
        btnNowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayActivity.this,NowPlayingActivity.class));
            }
        });
        ivAlbumArt.setOnTouchListener(new OnSwipeTouchListener() {
            @Override
            public void onSwipeRight() {
                playService.playPrevious();
            }

            @Override
            public void onSwipeLeft() {
                playService.playNext();

            }

            @Override
            public void onSwipeTop() {

            }

            @Override
            public void onSwipeBottom() {
                refresh();
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playService.togglePlay();
                if (playService.getMediaPlayer().isPlaying()){
                    btnPlay.setBackgroundResource(R.drawable.ic_pause_button);
                }
                else {
                    btnPlay.setBackgroundResource(R.drawable.ic_play_button);
                }
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
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playService.getIsShuffle()) {
                    btnShuffle.setBackgroundResource(R.drawable.ic_shuffle);
                    playService.shuffle();
                }
                else {
                    btnShuffle.setBackgroundResource(R.drawable.ic_not_shuffle);
                    playService.setIsShuffle(false);
                }
                refresh();
            }
        });
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!playService.getIsepeated()){
                   playService.setIsReapeated(true);
                   btnRepeat.setBackgroundResource(R.drawable.ic_repeat);
               }
               else{
                   playService.setIsReapeated(false);
                   btnRepeat.setBackgroundResource(R.drawable.ic_not_repeat);
               }

            }
        });

        Intent intent = getIntent();

       if (intent.getStringExtra("class name").equalsIgnoreCase("MusicList")){
           Log.d("testing", "onCreate: music list class");
           Bundle bundle = intent.getExtras();
           songs =(ArrayList<Song>) bundle.getSerializable("songs");
           position = intent.getIntExtra("index",0);
           Song song = songs.get(position);
           //setView(song);
           playIntent = new Intent(this,PlayService.class);
           bindService(playIntent,serviceConnection,Context.BIND_AUTO_CREATE);
       }
       else if (intent.getStringExtra("class name").equalsIgnoreCase("AlbumActivity")){
           Bundle bundle = intent.getExtras();
           songs =(ArrayList<Song>) bundle.getSerializable("songs");
           position = intent.getIntExtra("index",0);
           Song song = songs.get(position);
          // setView(song);
           playIntent = new Intent(this,PlayService.class);
           bindService(playIntent,serviceConnection,Context.BIND_AUTO_CREATE);
       }
       else {
           playIntent = new Intent(this, PlayService.class);
           bindService(playIntent, serviceConnection2, Context.BIND_AUTO_CREATE);
       }
       /*else if (intent.getStringExtra("class name").equalsIgnoreCase("MainActivity")){
           playIntent = new Intent(this,PlayService.class);
           bindService(playIntent,serviceConnection2,Context.BIND_AUTO_CREATE);
       }*/





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





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        if (mediaPlayer==null)
            mediaPlayer = playService.getMediaPlayer();
        seekBar.setProgress(progress);
        mediaPlayer.seekTo(progress*1000);

    }

    @Override
    public void callSetView(Song song) {

        setView(song);


    }

    @Override
    public void updateSeekbar() {
        if(mediaPlayer==null){
            mediaPlayer = playService.getMediaPlayer();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int currentPosition = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition/1000);
                handler.postDelayed(this,1000);
            }
        });


    }

    @Override
    public void refresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( isServiceConnection2Set){
            unbindService(serviceConnection2);
        }
        if ( isServiceConnectionSet){
            unbindService(serviceConnection);

        }

    }


}
