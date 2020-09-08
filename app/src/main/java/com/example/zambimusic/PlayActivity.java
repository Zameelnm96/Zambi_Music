package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
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

import com.example.zambimusic.data.enums.REPEAT;
import com.example.zambimusic.service.PlayService;
import com.example.zambimusic.service.ServiceUtil;
import com.example.zambimusic.ui.main.Button.RepeatButton;
import com.squareup.picasso.Picasso;
import com.example.zambimusic.roomdb.Song;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, PlayService.ServiceCallback {
    private static final String TAG = "PlayActivity";
    Button btnNowPlaying,btnMenu;
    ImageButton btnPlay,btnPrevious,btnNext,btnShuffle,btnRepeat;
    RepeatButton btRepeat;
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
    SharedPreferences sharedPreferences;
    ServiceConnection serviceConnection2 = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isServiceConnection2Set = true;
            isServiceConnectionSet = false;
            PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
            playService = myBinder.getService();
            playService.setServiceCallback(PlayActivity.this);
            songs = playService.getSongs();
            setView(playService.getCurrentSong());

            if (playService.getIsShuffled().getBooleanValue()){
                btnShuffle.setBackgroundResource(R.drawable.ic_shuffle);

            }
            else{
                btnShuffle.setBackgroundResource(R.drawable.ic_not_shuffle);
            }
            if(playService.getIsRepeated().getRepeat() == REPEAT.REPEAT_ALL){

               // btnRepeat.setBackgroundResource(R.drawable.ic_repeat_one);
                Log.d(TAG, "onServiceConnected: repeat all" );
                btRepeat.setRepeatState(REPEAT.REPEAT_ALL);

            }
            else if (playService.getIsRepeated().getRepeat() == REPEAT.REPEAT_ONE){
                //btnRepeat.setBackgroundResource(R.drawable.ic_not_repeat);
                Log.d(TAG, "onServiceConnected: no repeat");
                btRepeat.setRepeatState(REPEAT.REPEAT_ONE);
            }
            else if (playService.getIsRepeated().getRepeat() == REPEAT.NO_REPEAT){
                //btnRepeat.setBackgroundResource(R.drawable.ic_not_repeat);
                Log.d(TAG, "onServiceConnected: no repeat");
                btRepeat.setRepeatState(REPEAT.NO_REPEAT);
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
            songs = playService.getSongs();
            setView(playService.getCurrentSong());
            updateSeekbar();
            playService.setServiceCallback(PlayActivity.this);
            isBounded = true;
            if (playService.getIsShuffled().getBooleanValue()){
                btnShuffle.setBackgroundResource(R.drawable.ic_shuffle);
            }
            else{

                btnShuffle.setBackgroundResource(R.drawable.ic_not_shuffle);
            }
            if(playService.getIsRepeated().getRepeat() == REPEAT.REPEAT_ALL){

               // btnRepeat.setBackgroundResource(R.drawable.ic_repeat_one);
                btRepeat.setRepeatState(REPEAT.REPEAT_ALL);
            }
            else{
                //btnRepeat.setBackgroundResource(R.drawable.ic_not_repeat);
                btRepeat.setRepeatState(REPEAT.NO_REPEAT);
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
        sharedPreferences = this.getSharedPreferences("shared preferences",MODE_PRIVATE);
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
        //btnRepeat = findViewById(R.id.btnRepeatPlayActivity);
        btRepeat = findViewById(R.id.btnPlayPauseBottom);
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
                ServiceUtil.playNext(playService,playService.getMediaPlayer(),playService.getSongs(), playService.getWrapPosition());
            }

            @Override
            public void onSwipeLeft() {
                ServiceUtil.playNext(playService,playService.getMediaPlayer(),playService.getSongs(), playService.getWrapPosition());

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
                ServiceUtil.togglePlay(playService.getMediaPlayer());
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
                ServiceUtil.playNext(playService,playService.getMediaPlayer(),playService.getSongs(), playService.getWrapPosition());


            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ServiceUtil.playPrevious(playService,playService.getMediaPlayer(),playService.getSongs(), playService.getWrapPosition());
            }
        });
        btnShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playService.getIsShuffled().getBooleanValue()) {
                    btnShuffle.setBackgroundResource(R.drawable.ic_shuffle);
                    sharedPreferences.edit().putBoolean("isShuffled",true).apply();
                    ServiceUtil.shuffle(playService.getIsShuffled(),playService.getSongs(),playService.getWrapPosition());
                }
                else {
                    btnShuffle.setBackgroundResource(R.drawable.ic_not_shuffle);
                    sharedPreferences.edit().putBoolean("isShuffled",false).apply();
                    playService.setIsShuffle(false);
                }
                refresh();
            }
        });
      /*  btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(playService.getIsRepeated().getRepeat() == REPEAT.NO_REPEAT){
                   playService.setIsReapeated(REPEAT.REPEAT_ALL);
                   btnRepeat.setBackgroundResource(R.drawable.ic_repeat_one);
               }
               else{
                   playService.setIsReapeated(REPEAT.NO_REPEAT);
                   btnRepeat.setBackgroundResource(R.drawable.ic_not_repeat);
               }

            }
        });*/
        btRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btRepeat.nextState();
                switch (btRepeat.getRepeatState()){
                    case NO_REPEAT:
                       playService.setIsReapeated(REPEAT.NO_REPEAT);
                       sharedPreferences.edit().putString("repeatEnum",REPEAT.NO_REPEAT.toString()).apply();
                        break;
                    case REPEAT_ONE:
                        playService.setIsReapeated(REPEAT.REPEAT_ONE);
                        sharedPreferences.edit().putString("repeatEnum",REPEAT.REPEAT_ONE.toString()).apply();
                        break;
                    case REPEAT_ALL:
                        playService.setIsReapeated(REPEAT.REPEAT_ALL);
                        sharedPreferences.edit().putString("repeatEnum",REPEAT.REPEAT_ALL.toString()).apply();
                        break;
                    default:
                        playService.setIsReapeated(REPEAT.NO_REPEAT);
                        sharedPreferences.edit().putString("repeatEnum",REPEAT.NO_REPEAT.toString()).apply();
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

        else if (intent.getStringExtra("class name").equalsIgnoreCase("FragAllSong")){
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
