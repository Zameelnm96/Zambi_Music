package com.example.zambimusic.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.zambimusic.repository.SongRepository;
import com.example.zambimusic.roomdb.Song;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private static final String TAG = "AppViewModel";
    private SongRepository songRepository;
    private LiveData<List<Song>> allSongs;
    public AppViewModel(@NonNull Application application) {
        super(application);

        Log.d(TAG, "AndroidViewModel: constructor called");
        songRepository = new SongRepository(application);
        allSongs = songRepository.getAllSongs();
    }

    public LiveData<List<Song>> getAllSongs(){
        return allSongs;
    }

    public void insert(Song... song){
        songRepository.insert(song);
    }


    public void delete(Song song){
        songRepository.delete(song);
    }

    public void deleteAll(){ songRepository.deleteAll();}
}
