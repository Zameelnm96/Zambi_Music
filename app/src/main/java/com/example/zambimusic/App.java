package com.example.zambimusic;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.zambimusic.observer.UriObserver;
import com.example.zambimusic.roomdb.InsertUtil;
import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.roomdb.model.Audio;
import com.example.zambimusic.ui2.service.StorageUtil;
import com.example.zambimusic.viewmodel.AppViewModel;
import com.example.zambimusic.viewmodel.SongViewModel;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class App extends Application implements UriObserver.OnChangeListener  {
    AppViewModel appViewModel;
    private static final String TAG = "App";
    private ArrayList<Audio> currentPlaylist = new ArrayList<>();
    private Handler handler;
    private UriObserver observer;
    private MutableLiveData<ArrayList<Audio>> currentPlaylistLive;
    Integer currentPosition = 0;



    private MutableLiveData<Integer> currentPositionLive;

    @Override
    public void onCreate() {
        super.onCreate();
       /* appViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(SongViewModel.class);
        InsertUtil.updateMusicList(appViewModel,this);

        handler = new Handler();
        observer = new UriObserver(handler,this);
        this.getContentResolver().
                registerContentObserver(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        true,
                        observer);*/
        //UriObserver.InnerUriObserver.getInstance(this.getContentResolver(),MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,this);
        StorageUtil storageUtil = new StorageUtil(this);
        currentPlaylist = storageUtil.loadAudio();
        currentPosition = storageUtil.loadAudioIndex();



    }

    /*public AppViewModel getViewmodel(){
        return appViewModel;
    }*/

    public void setCurrentPlaylist(ArrayList<Audio> audios){
        this.currentPlaylist.clear();
        this.currentPlaylist.addAll(audios);
        currentPlaylistLive.setValue(audios);

    }

    public void setCurrentPosition(int position){
        this.currentPosition = position;
        currentPositionLive.setValue(currentPosition);
    }

    @Override
    public void onChange(Uri uri) {
        Log.d(TAG, "onChange: with uri " + uri);
        //InsertUtil.updateMusicList(appViewModel,this);
    }

    LiveData<ArrayList<Audio>> getCurrentPlaylist(){
        Log.d(TAG, "getCurrentPlaylist: size" + currentPlaylist.size() );
        if(currentPlaylistLive==null ){
            Log.d(TAG, "getCurrentPlaylist: currentPlaylistLive is null");
            currentPlaylistLive = new MutableLiveData<>() ;

        }

        return currentPlaylistLive;
    }

    public LiveData<Integer> getCurrentPositionLive() {
        if(currentPositionLive == null){
            currentPositionLive = new MutableLiveData<>();
            currentPositionLive.setValue(currentPosition);
        }
        return currentPositionLive;
    }
}
