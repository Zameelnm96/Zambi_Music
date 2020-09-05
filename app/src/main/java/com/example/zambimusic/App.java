package com.example.zambimusic;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.example.zambimusic.observer.UriObserver;
import com.example.zambimusic.roomdb.InsertUtil;
import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.viewmodel.AppViewModel;
import com.example.zambimusic.viewmodel.SongViewModel;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App extends Application implements UriObserver.OnChangeListener  {
    AppViewModel appViewModel;
    private static final String TAG = "App";

    private Handler handler;
    private UriObserver observer;

    @Override
    public void onCreate() {
        super.onCreate();
        appViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(SongViewModel.class);
        InsertUtil.updateMusicList(appViewModel,this);

        handler = new Handler();
        observer = new UriObserver(handler,this);
        this.getContentResolver().
                registerContentObserver(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        true,
                        observer);
        //UriObserver.InnerUriObserver.getInstance(this.getContentResolver(),MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,this);
    }

    public AppViewModel getViewmodel(){
        return appViewModel;
    }


    @Override
    public void onChange(Uri uri) {
        Log.d(TAG, "onChange: with uri " + uri);
        InsertUtil.updateMusicList(appViewModel,this);
    }
}
