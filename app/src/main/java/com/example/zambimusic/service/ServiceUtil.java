package com.example.zambimusic.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.service.singletonvalues.CurrentPlayPosition;
import com.example.zambimusic.service.singletonvalues.IsShuffled;
import com.example.zambimusic.service.singletonvalues.Position;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import static android.content.Context.MODE_PRIVATE;

public abstract class ServiceUtil {
    private static final String TAG = "ServiceUtil";
    public static  void saveCurrentList(Context context, ArrayList<Song> songs, int position, int currentPlayPosition) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
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

    public static void stop(MediaPlayer mediaPlayer){
        if (mediaPlayer != null){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
        }
    }

    public static void addToQueue(Song from , ArrayList<Song> to) {
        to.add(from);
    }

    public static void addToQueue(ArrayList<Song> from, ArrayList<Song> to) {
        to.addAll(from);
    }

    public static Song getSong(ArrayList<Song> songs, int position) {
        return songs.get(position);
    }

    public static void addToPlayNext(Song from, ArrayList<Song> to, int position) {
        to.add(position + 1, from);
    }

    public static void addToPlayNext(ArrayList<Song> from,ArrayList<Song> to, int position) {
        int i = position + 1;
        for (Song song : from) {
            to.add(i++, song);
        }
    }

    public static void shuffle(IsShuffled isShuffled, ArrayList<Song> songs, Position position) {

        isShuffled.setBooleanValue(true);
        Song song = songs.get(position.getIntValue());
        Collections.shuffle(songs);
        songs.remove(songs.indexOf(song));
        songs.add(0, song);
        position.setIntValue(0);
    }


    public static int getPreviousSongPosition(int songListSize, int position ) {
        position -= 1;
        if (position < 0) {

            position += songListSize;
        }
        return position;

    }

    public static int getNextSongPosition(int songListSize, int position ) {
        position += 1;
        if (position > songListSize) {

            position = 0;
        }
        return position;

    }

    public static void togglePlay(MediaPlayer mediaPlayer){
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }
    }

    public static void playNext(PlayService service, MediaPlayer mediaPlayer, ArrayList<Song> songs, Position position ) {
        mediaPlayer.reset();

        position .setIntValue(ServiceUtil.getNextSongPosition(songs.size(),position.getIntValue())); ;

        try {
            mediaPlayer.setDataSource(service, songs.get(position.getIntValue()).getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.prepareAsync();
    }

    public static void playPrevious(PlayService service, MediaPlayer mediaPlayer, ArrayList<Song> songs, Position position) {
        if (mediaPlayer.getCurrentPosition() > 3000) {
            mediaPlayer.seekTo(0);
        }
        else {
            mediaPlayer.reset();
            position .setIntValue( ServiceUtil.getPreviousSongPosition(songs.size(),position.getIntValue()));

            try {
                mediaPlayer.setDataSource(service, songs.get(position.getIntValue()).getUri());
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();

        }
    }

    public static void loadLastPlayed(Context context, MediaPlayer mediaPlayer, ArrayList<Song> songs, Position position , CurrentPlayPosition currentPlayPosition){
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("songs", null);
        Type type = new TypeToken<ArrayList<Song>>() {
        }.getType();
        ArrayList<Song> songsTemp = gson.fromJson(json, type);
        songs.clear();
        if(songsTemp == null){
            return;
        }
        songs.addAll(songsTemp);
        position.setIntValue(sharedPreferences.getInt("position", 0));

        if (songs!=null) {
            try {
                mediaPlayer.setDataSource(context, songs.get(position.getIntValue()).getUri());
            } catch (IOException e) {
                e.printStackTrace();
            }

            currentPlayPosition .setIntValue(sharedPreferences.getInt("currentPosition", 0));
            Log.d("testing", "loadSongs: " + currentPlayPosition.getIntValue());
            mediaPlayer.prepareAsync();

        }
    }

    public static void setSong(Context context, MediaPlayer mediaPlayer,Song song ){
        mediaPlayer.reset();

        Uri uri = null;

        if (song != null) {
            uri = song.getUri();
            Log.d(TAG, "setSong: uri " + uri);
            try {
                mediaPlayer.setDataSource(context, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
        }

    }
}
