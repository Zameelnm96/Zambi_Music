package com.example.zambimusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;
    ArrayList<Song> songs;
    int position;
    ServiceCallback serviceCallback;
    boolean setOnclickLisner = false;

    public interface ServiceCallback{
        public void callSetView(Song song);
        public void updateSeekbar();
        public void refresh();
    }

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
    public boolean onUnbind(Intent intent) {
        Log.d("testing", "onUnbind: running");
       /* if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        }*/
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);


    }

    public Song getCurrentSong(){
        return songs.get(position);
    }
    public void setList(ArrayList<Song> songs){
        this.songs = songs;

    }
    public void setPosition(int index){
        position = index;
    }


    public void setSong(){


        mediaPlayer.reset();
        Song song = null;
        Uri uri = null;
            if (songs!=null){
                Log.d("testing", "setSong:" +songs.get(position).getName());
                song = songs.get(position);
                Log.d("testing", "setSong: ID"+ song.getId());
            }
            if (song != null){
                uri = song.getUri();
                Log.d("testing", "setSong: uri " + uri);
            }
            try {
                mediaPlayer.setDataSource(this, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
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
    public void playNext(){
        mediaPlayer.reset();
        if(++position >= songs.size()){
            position=0;
            try {
                mediaPlayer.setDataSource(this, songs.get(position).getUri());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //serviceCallback.callSetView(songs.get(position));
            mediaPlayer.prepareAsync();
            return;
        }
        try {
            mediaPlayer.setDataSource(this, songs.get(position).getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*serviceCallback.callSetView(songs.get(position));*/
        mediaPlayer.prepareAsync();


    }
    public void playPrevious(){
        if (mediaPlayer.getDuration()<5000){
            mediaPlayer.seekTo(0);
        }
        else{
            mediaPlayer.reset();
            if (--position<0){

                position += songs.size();
                try {
                    mediaPlayer.setDataSource(this, songs.get(position).getUri());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //serviceCallback.callSetView(songs.get(position));
                mediaPlayer.prepareAsync();
                return;

            }
            try {
                mediaPlayer.setDataSource(this, songs.get(position).getUri());
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*serviceCallback.callSetView(songs.get(position));*/
            mediaPlayer.prepareAsync();


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
        serviceCallback.callSetView(songs.get(position));
        serviceCallback.updateSeekbar();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

            if (position < songs.size() - 1) {
                playNext();
            } else {
                position = 0;
            }


    }
    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    class MyBinder  extends Binder{
        public PlayService getService(){
            return PlayService.this;
        }
    }
    private MyBinder myBinder = new MyBinder();
    public Song getSong(){
        return songs.get(position);
    }
}

