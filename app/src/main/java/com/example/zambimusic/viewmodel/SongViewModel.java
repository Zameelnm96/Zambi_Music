package com.example.zambimusic.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.zambimusic.repository.SongRepository;
import com.example.zambimusic.roomdb.Song;

import java.util.List;

public class SongViewModel extends AppViewModel<Song> {
    private static final String TAG = "AppViewModel";
    private SongRepository songRepository;
    private LiveData<List<Song>> allSongs;
    public SongViewModel(@NonNull Application application) {
        super(application);

        Log.d(TAG, "AndroidViewModel: constructor called");
        songRepository = new SongRepository(application);
        allSongs = songRepository.getAll();
    }

    @Override
    public LiveData<List<Song>> getAll() {
        return allSongs;
    }

    @Override
    public LiveData<List<Song>> findByName(String name) {
        return null;
    }

    @Override
    public void deleteModel(Song song) {
        songRepository.deleteModel(song);
    }

    @Override
    public void deleteModel(Song[] songs) {
        songRepository.deleteModel(songs);
    }

    @Override
    public  void deleteAllExcept(Long[] ns) {
       // songRepository.deleteAllExcept(ns);
    }

    @Override
    public void insertModel(Song song) {
        songRepository.insertModels(song);
    }

    @Override
    public void insertModel(Song[] songs) {
        songRepository.insertModels(songs);
    }



}
