package com.example.zambimusic;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.zambimusic.roomdb.model.Audio;
import com.example.zambimusic.ui.main.Button.CustomToggleImageButton;
import com.example.zambimusic.ui2.adapter.NowPlayingMainAdapter;


import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

public class NowPlayingActivity2 extends AppCompatActivity implements View.OnClickListener, NowPlayingMainAdapter.OnItemClickListener, SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "NowPlayinActivity";
    SeekBar seekBar;
    TextView tvProgress;
    TextView tvTotalTime;
    CustomToggleImageButton customToggleImageButton;


    App app;
    NowPlayingMainAdapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playin);

        app =  (App) getApplication();
        adapter = new NowPlayingMainAdapter(getApplication());
        adapter.setOnItemClickListener(this);
        recyclerView = NowPlayingActivity2.this.findViewById(R.id.now_playing_recycler_view_main);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(NowPlayingActivity2.this, LinearLayoutManager.HORIZONTAL, false);

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d(TAG, "onScrollStateChanged: " + newState);
                //playthe next song when newstate = 2
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        app.getCurrentPlaylist().observe(this, new Observer<ArrayList<Audio>>() {
            @Override
            public void onChanged(ArrayList<Audio> audio) {
                Log.d(TAG, "onChanged: current value");
                if(audio != null){
                    adapter.setList(audio);
                    if(recyclerView!=null){
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
        });


      app.getCurrentPositionLive().observe(this, new Observer<Integer>() {
          @Override
          public void onChanged(Integer integer) {
              Log.d(TAG, "onChanged: integer  tt ");
              currentPosition = integer;
              linearLayoutManager.scrollToPositionWithOffset(integer, 0);
              recyclerView.setLayoutManager(linearLayoutManager);
          }
      });

        seekBar = findViewById(R.id.seekBarPlayActivity);
        seekBar.setOnSeekBarChangeListener(this);
        tvProgress = findViewById(R.id.tvProgress);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        customToggleImageButton = findViewById(R.id.playPuase);
        customToggleImageButton.setOnClickListener(this);
        // set progress to 40%
        Log.d(TAG, "onCreate: seek set progress");
        seekBar.setProgress(50);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Animatoo.animateSlideDown(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    public void initRecylerview(){
        RecyclerView recyclerView = findViewById(R.id.now_playing_recycler_view_main);
        if(recyclerView == null){

            Log.d(TAG, "onCreate: recycler view is null");
            return;
        }


        AlphaInAnimationAdapter alphaInAnimationAdapter = new AlphaInAnimationAdapter(adapter);
        alphaInAnimationAdapter.setDuration(1000);
        alphaInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        alphaInAnimationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onMusicListClicked() {
        Log.d(TAG, "onMusicListClicked: ");
    }

    @Override
    public void onMoreOptionClicked() {
        Log.d(TAG, "onMoreOptionClicked: ");
    }

    @Override
    public void onItemClickListener(int position) {
        Log.d(TAG, "onItemClickListener: ");
    }

    
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String timeString = Util.convertSecondsToHMmSs(progress);
        if(fromUser)

            Log.d(TAG, "onProgressChanged: from user" + timeString   );
        else Log.d(TAG, "onProgressChanged: not from user progress" + timeString  ); // here seek th media player
        tvProgress.setText(timeString);
        // here set the text view
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStartTrackingTouch: ");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStopTrackingTouch: ");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.playPuase:
                customToggleImageButton.nextState();
                break;
            case R.id.next:
                break;
            case R.id.previos:
                break;
        }
        customToggleImageButton.nextState();
    }
}