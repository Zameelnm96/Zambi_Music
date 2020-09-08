package com.example.zambimusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import com.example.zambimusic.data.Constants;
import com.example.zambimusic.data.enums.PlaybackStatus;
import com.example.zambimusic.data.enums.TWO_STATE;
import com.example.zambimusic.roomdb.model.Audio;
import com.example.zambimusic.ui.main.Button.CustomToggleImageButton;
import com.example.zambimusic.ui2.SectionsPagerAdapter;
import com.example.zambimusic.ui2.adapter.NowPlayingAdapter;
import com.example.zambimusic.ui2.service.MediaPlayerService;
import com.example.zambimusic.ui2.service.StorageUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity3 extends AppCompatActivity implements NowPlayingAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity3";
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.valdioveliu.valdio.audioplayer.PlayNewAudio";


    MediaPlayerService mediaPlayerService;
    boolean isBounded = false;

    App app;
    int currentPosition;
    private NowPlayingAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        final StorageUtil storageUtil = new StorageUtil(this);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        app = (App) getApplication();
        Log.d(TAG, "onCreate: " + app.getCurrentPlaylist().toString());

        recyclerView = findViewById(R.id.now_playing_song);
        recyclerView.setHasFixedSize(true);

        adapter = new NowPlayingAdapter(getApplication());
        adapter.setOnItemClickListener(this);

        linearLayoutManager = new LinearLayoutManager(MainActivity3.this, LinearLayoutManager.HORIZONTAL, false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: " + newState);
                //playthe next song when newstate = 2
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                 Log.d ("VisibleItem", String.valueOf(firstVisibleItem));

            }
        });




        app.getCurrentPlaylist().observe(this, new Observer<ArrayList<Audio>>() {
            @Override
            public void onChanged(ArrayList<Audio> audio) {
                Log.d(TAG, "onChanged: current playlist   size is " + audio.size());


                if (audio.size() > 0) {
                    adapter.setList(audio);
                    if (recyclerView == null) {

                        Log.d(TAG, "onCreate: recycler view is null");

                    } else {
                        recyclerView.setAdapter(adapter);
                        findViewById(R.id.now_playing_song).setVisibility(View.VISIBLE);


                    }


                } else {
                    findViewById(R.id.now_playing_song).setVisibility(View.GONE);
                }

            }
        });

        app.getCurrentPositionLive().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                currentPosition = integer;
                linearLayoutManager.scrollToPositionWithOffset(currentPosition, 0);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
        });


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isBounded) {
            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBounded) {
            this.unbindService(serviceConnection);
        }

    }


    @Override
    public void onPlayButtonClicked(int position, TWO_STATE state) {
        Log.d(TAG, "onPlayButtonClicked: ");

        Intent intent = new Intent(MediaPlayerService.ACTION_COMMAND);
        switch (state){
           case STATE1:
               intent.putExtra(Constants.COMMAND, MediaPlayerService.ACTION_PAUSE);
               break;
           case STATE2:
               intent.putExtra(Constants.COMMAND, MediaPlayerService.ACTION_RESUME);
               break;

       }
       this.sendBroadcast(intent);


    }

    @Override
    public void onItemClickListener(int position) {
        startActivity(new Intent(MainActivity3.this, NowPlayingActivity2.class));
        Animatoo.animateSlideUp(this);
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mediaPlayerService = (MediaPlayerService) binder.getService();

            setButtonState(mediaPlayerService, (CustomToggleImageButton) adapter.getCustomToggleImageButton());
            isBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBounded = false;
        }
    };

    private void setButtonState(MediaPlayerService mediaPlayerService, CustomToggleImageButton customToggleImageButton){
        if(customToggleImageButton != null) {
            if (mediaPlayerService.getMediaPlayerStatus() == PlaybackStatus.NOT_DEFINED) {
                //no media on que
                customToggleImageButton.setBtn_state(TWO_STATE.STATE1);
            } else if (mediaPlayerService.getMediaPlayerStatus() == PlaybackStatus.PAUSED) {
                //play the media
                customToggleImageButton.setBtn_state(TWO_STATE.STATE1);

            } else if (mediaPlayerService.getMediaPlayerStatus() == PlaybackStatus.PLAYING) {
                //pause th media
                customToggleImageButton.setBtn_state(TWO_STATE.STATE2);

            }
        }
    }

}