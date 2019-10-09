package com.example.zambimusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;

public class PlayService extends Service {

    ArrayList<Song> songs;
    int position;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("testing", "onBind: ");
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("testing", "onUnbind: running");
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        }
        return super.onUnbind(intent);
    }

    MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();



    }


    public void setList(ArrayList<Song> songs){
        this.songs = songs;

    }
    public void setPosition(int index){
        position = index;
    }

    public void setSong(){
        Log.d("testing", "setSong:" +songs.get(position).getName());

        Song song = null;
        Uri uri = null;
        if (songs!=null){
            song = songs.get(position);
        }
        if (song != null){
            uri = song.getUri();
        }
        try {
            mediaPlayer.setDataSource(getBaseContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void playSong(){
        mediaPlayer.start();
    }

    public void togglePlay(){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
            else {
                mediaPlayer.start();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("testing", "onStartCommand: position is " +position );
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("testing", "onDestroy: Playservice");
    }

    class MyBinder  extends Binder{
        public PlayService getService(){
            return PlayService.this;
        }
    }
    private MyBinder myBinder = new MyBinder();

}

