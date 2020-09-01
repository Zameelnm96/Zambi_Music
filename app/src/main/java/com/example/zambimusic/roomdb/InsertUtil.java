package com.example.zambimusic.roomdb;

import android.content.Context;
import android.provider.MediaStore;

import com.example.zambimusic.Util;
import com.example.zambimusic.roomdb.dao.SongDao;
import com.example.zambimusic.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class InsertUtil {
    public static void updateMusicList(AppViewModel viewModel, Context context){

        List<Song> songs = Util.getAllMp3(MediaStore.MediaColumns.TITLE, context);
        Song[] songsArr = new Song[songs.size()];
        songsArr = songs.toArray(songsArr);
        viewModel.deleteAllExcept(Util.getAllIDs((ArrayList<Song>) songs));
        viewModel.insertModel(songsArr);
    }

    public static void updateMusicList(SongDao songDao , Context context){

    }
}
