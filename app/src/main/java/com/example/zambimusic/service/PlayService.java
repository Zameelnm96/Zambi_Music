package com.example.zambimusic.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.zambimusic.enums.REPEAT;
import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.service.singletonvalues.CurrentPlayPosition;
import com.example.zambimusic.service.singletonvalues.IsRepeated;
import com.example.zambimusic.service.singletonvalues.IsShuffled;
import com.example.zambimusic.service.singletonvalues.Position;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private static final String TAG = "PlayService";
    MediaPlayer mediaPlayer;
    ArrayList<Song> songs;
    SharedPreferences sharedPreferences;
    Position position;
    CurrentPlayPosition currentPlayPosition;
    ServiceCallback serviceCallback;
    boolean setOnclickLisner = false;
    private IsShuffled isShuffled;
    private IsRepeated isRepeated;
    private boolean isLoaded = false;
    final Handler handler = new Handler();
    private MyBinder myBinder = new MyBinder();

    public void setServiceCallback(ServiceCallback serviceCallback) {
        this.serviceCallback = serviceCallback;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("testing", "onBind: ");
        return myBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = this.getSharedPreferences("shared preferences",MODE_PRIVATE);
        mediaPlayer = new MediaPlayer();

        position = Position.getInstance();
        currentPlayPosition = CurrentPlayPosition.getInstance();
        isShuffled = IsShuffled.getInstance();
        isRepeated = IsRepeated.getInstance();

        isShuffled .setBooleanValue(sharedPreferences.getBoolean("isShuffled",false));
        isRepeated.setRepeat(REPEAT.toREPEAT(sharedPreferences.getString("repeatEnum",REPEAT.NO_REPEAT.toString())));
        Log.d(TAG, "onCreate: repeat " + isRepeated.getRepeat().toString());
        mediaPlayer.setOnPreparedListener(this);
        if (songs == null && !isLoaded) {
            songs = new ArrayList<>();
            loadSongs();
        }

    }

    public Song getCurrentSong() {
        return songs.get(getPosition());
    }

    public void setList(ArrayList<Song> songs) {
        this.songs = songs;

    }

    public void setPosition(int index) {
        position.setIntValue(index);
    }


    public void setIsShuffle(boolean status) {
        isShuffled.setBooleanValue(status);
        ;
    }

    private boolean getIsShuffleValue() {
        return isShuffled.getBooleanValue();
    }

    public IsShuffled getIsShuffled() {
        return isShuffled;
    }

    public void setIsReapeated(REPEAT repeat) {
        Log.d(TAG, "setIsReapeated: " + repeat.name());
        this.isRepeated .setRepeat(repeat);

    }

    public REPEAT getIsRepeatedValue() {
        return isRepeated.getRepeat();
    }

    public IsRepeated getIsRepeated(){
        return isRepeated;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("testing", "onStartCommand: position is " + getPosition());
        new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(1000);
                        if (mediaPlayer.isPlaying()) {
                            currentPlayPosition.setIntValue(mediaPlayer.getCurrentPosition());
                            ;
                            ServiceUtil.saveCurrentList(PlayService.this, songs, getPosition(), getCurrentPlayPosition());
                            Log.d("testing", "run: current position " + getCurrentPlayPosition());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //REST OF CODE HERE//
                }

            }
        }).start();
        return START_STICKY;
    }

    public void stop() {
        ServiceUtil.stop(mediaPlayer);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("testing", "onUnbind: running");
       /* if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        }*/
        //return super.onUnbind(intent);
        //erviceUtil.saveCurrentList();
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("testing", "onDestroy: currentPlayPosition" + getCurrentPlayPosition());
        //erviceUtil.saveCurrentList();
        super.onDestroy();

    }

    public int getCurrentPlayPosition() {
        return currentPlayPosition.getIntValue();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (!setOnclickLisner) {
            mediaPlayer.setOnCompletionListener(this);
            setOnclickLisner = true;

        }
        if (serviceCallback != null) {
            serviceCallback.callSetView(songs.get(getPosition()));
            serviceCallback.updateSeekbar();
        }
        if (isLoaded) {
            mediaPlayer.seekTo(getCurrentPlayPosition());
            mediaPlayer.pause();
            isLoaded = false;
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if (getPosition() < songs.size() - 1) {
            if(getIsRepeatedValue() == REPEAT.REPEAT_ONE){
                ServiceUtil.setSong(this, mediaPlayer, songs.get(getPosition()));
            }
            else {
                ServiceUtil.playNext(this, mediaPlayer, songs, position);
            }

        }
        else {
            if (getIsRepeatedValue() == REPEAT.REPEAT_ALL) {
                position.setIntValue(0);
                ServiceUtil.setSong(this, mediaPlayer, songs.get(getPosition()));
            }
            else if(getIsRepeatedValue() == REPEAT.REPEAT_ONE){
                ServiceUtil.setSong(this, mediaPlayer, songs.get(getPosition()));
            }
            else {
                currentPlayPosition.setIntValue(0);
            }


        }


    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public class MyBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }


    public int getPosition() {
        return position.getIntValue();
    }

    public Position getWrapPosition() {
        return position;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }


    private void loadSongs() {
        isLoaded = true;
        ServiceUtil.loadLastPlayed(this, mediaPlayer, songs, position, currentPlayPosition);

    }

    public interface ServiceCallback {
        public void callSetView(Song song);

        public void updateSeekbar();

        public void refresh();
    }


}

