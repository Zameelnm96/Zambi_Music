package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicList extends AppCompatActivity implements SongAdapter.ItemClicked {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Song> songs;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        songs = new ArrayList<>();
        fillMusicList();




        recyclerView = findViewById(R.id.recyclerView);
        adapter = new SongAdapter(this,songs);
         layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }
    /*private List<String> musicFileList = new ArrayList<>();*/
    public void fillMusicList(){
        songs.clear();
        addMusicFileFrom(String.valueOf( Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC)));
        addMusicFileFrom(String.valueOf( Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS)));

    }
    public void addMusicFileFrom(String dirPath){
        final File musicDir = new File(dirPath);
        if (!musicDir.exists()){
            musicDir.mkdir();
            return;
        }
        final File[] files = musicDir.listFiles();
        for (File file:files){
            final String path = file.getAbsolutePath();
            if (path.endsWith(".mp3")){
                Song song = new Song(path);
                song.setName(getSongName(path));
                songs.add(song);
            }
        }
    }

    public void playMusicFile(String path){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int index) {
        Song song = songs.get(index);
        playMusicFile(song.getPath());


    }
    public String getSongName(String path){
        String[] temp = path.split("/");
        return temp[temp.length-1];
    }
}
