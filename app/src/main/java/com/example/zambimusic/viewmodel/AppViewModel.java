package com.example.zambimusic.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.zambimusic.repository.SongRepository;
import com.example.zambimusic.roomdb.Song;

import java.util.ArrayList;
import java.util.List;

public abstract class AppViewModel<T> extends AndroidViewModel {

    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract LiveData<List<T>> getAll();
    public abstract LiveData<List<T>> findByName(String name);

    public abstract void deleteModel(T t);
    public abstract void deleteModel(T[] ts);
    public abstract  void deleteAllExcept(Long[] ns);

    public abstract void insertModel(T t);
    public abstract void insertModel(T[] ts);






}
