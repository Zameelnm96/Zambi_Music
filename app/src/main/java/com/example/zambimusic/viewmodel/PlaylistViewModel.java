package com.example.zambimusic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.zambimusic.repository.PlaylistRepository;
import com.example.zambimusic.roomdb.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistViewModel extends AppViewModel<Playlist> {

    PlaylistRepository playlistRepository;
    LiveData<List<Playlist>> allPlaylist;
    public PlaylistViewModel(@NonNull Application application) {
        super(application);
        playlistRepository = new PlaylistRepository(application);
        allPlaylist = playlistRepository.getAll();
    }

    @Override
    public LiveData<List<Playlist>> getAll() {
        return allPlaylist;
    }

    @Override
    public LiveData<List<Playlist>> findByName(String name) {
        return playlistRepository.filterByName(name);
    }

    @Override
    public void deleteModel(Playlist playlist) {
        playlistRepository.delete(playlist);
    }

    @Override
    public void deleteModel(Playlist[] playlists) {
        playlistRepository.delete(playlists);
    }

    @Override
    public void deleteAllExcept(Long[] ns) {

    }

    @Override
    public void insertModel(Playlist playlist) {
        playlistRepository.insertAll(playlist);
    }

    @Override
    public void insertModel(Playlist[] playlists) {
        playlistRepository.insertAll(playlists);
    }
}
