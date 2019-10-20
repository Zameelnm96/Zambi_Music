package com.example.zambimusic;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

public class PlayService extends Service implements MediaPlayer.OnPreparedListener,MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    MediaPlayer mediaPlayer;
    ArrayList<Song> songs;

    int position;
    int currentPlayPosition;
    ServiceCallback serviceCallback;
    boolean setOnclickLisner = false;
    private boolean isShuffled = false;
    private boolean isRepeated;
    private boolean isLoaded = false;
    final Handler handler = new Handler();


    public interface ServiceCallback {
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
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        if (songs == null && !isLoaded ){
            loadSongs();
        }



    }

    public Song getCurrentSong() {
        return songs.get(position);
    }

    public void setList(ArrayList<Song> songs) {
        this.songs = songs;


    }

    public void setPosition(int index) {
        position = index;
    }


    public void setSong() {


        mediaPlayer.reset();
        Song song = null;
        Uri uri = null;
        if (songs != null) {
            Log.d("testing", "setSong:" + songs.get(position).getName());
            song = songs.get(position);
            Log.d("testing", "setSong: ID" + song.getId());
        }
        if (song != null) {
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


    public void togglePlay() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    public void playNext() {
        mediaPlayer.reset();
        if (++position >= songs.size()) {
            position = 0;
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

    public void playPrevious() {
        if (mediaPlayer.getCurrentPosition() > 3000) {
            mediaPlayer.seekTo(0);
        } else {
            mediaPlayer.reset();
            if (--position < 0) {

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

    public void setIsShuffle(boolean status) {
        isShuffled = status;
    }

    public boolean getIsShuffle() {
        return isShuffled;
    }

    public void shuffle() {
        isShuffled = true;
        Song song = getSong();
        Collections.shuffle(songs);
        songs.remove(songs.indexOf(song));
        songs.add(0, song);
        position = 0;

    }

    public void setIsReapeated(boolean isRepeated) {
        this.isRepeated = isRepeated;

    }

    public boolean getIsepeated() {
        return isRepeated;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("testing", "onStartCommand: position is " + position);
        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true)
                {
                    try {
                        Thread.sleep(1000);
                        if(mediaPlayer.isPlaying()){
                            currentPlayPosition = mediaPlayer.getCurrentPosition();
                            saveCurrentList();
                            Log.d("testing", "run: current position " + currentPlayPosition );
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
        //saveCurrentList();
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
    @Override
    public void onDestroy() {
        Log.d("testing", "onDestroy: currentPlayPosition"+ currentPlayPosition);
        //saveCurrentList();
        super.onDestroy();

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
            serviceCallback.callSetView(songs.get(position));
            serviceCallback.updateSeekbar();
        }
        if(isLoaded){
            mediaPlayer.seekTo(currentPlayPosition);
            mediaPlayer.pause();
            isLoaded =false;
        }

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if (position < songs.size() - 1) {
            playNext();
        } else {
            position = 0;
            if (isRepeated) {
                setSong();
            }
        }


    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    class MyBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    private MyBinder myBinder = new MyBinder();

    public Song getSong() {
        return songs.get(position);
    }

    public void addToPlayNext(Song song) {
        songs.add(position + 1, song);
    }

    public void addToPlaynext(ArrayList<Song> songs) {
        int i = position + 1;
        for (Song song : songs) {
            this.songs.add(i++, song);
        }
    }

    public void addToQueue(Song song) {
        songs.add(song);
    }

    public void addToqueue(ArrayList<Song> songs) {
        this.songs.addAll(songs);
    }

    public int getPosition() {
        return position;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    private void saveCurrentList() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(songs);
        editor.putString("songs", json);
        editor.putInt("position", position);
        //currentPlayPosition=mediaPlayer.getCurrentPosition();
        Log.d("testing", "saveCurrentList: " + currentPlayPosition);
        editor.putInt("currentPosition",currentPlayPosition);
        editor.apply();
    }

    private void loadSongs() {
        isLoaded = true;
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("songs", null);
        Type type = new TypeToken<ArrayList<Song>>() {
        }.getType();
        songs = gson.fromJson(json, type);
        position = sharedPreferences.getInt("position", 0);

        if (songs!=null) {
            try {
                mediaPlayer.setDataSource(this, songs.get(position).getUri());
            } catch (IOException e) {
                e.printStackTrace();
            }

            currentPlayPosition = sharedPreferences.getInt("currentPosition", 0);
            Log.d("testing", "loadSongs: " + currentPlayPosition);
            mediaPlayer.prepareAsync();

        }

    }

}