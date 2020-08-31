package com.example.zambimusic;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.viewmodel.AppViewModel;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;

public class App extends Application {
    AppViewModel appViewModel;
    BroadcastReceiver scanFileReceiver = new BroadcastReceiver() {
        private static final String TAG = "App";
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
                Uri uri = intent.getData();

                Log.d(TAG, "onReceive: called with uri" + uri.getPath());
                String path = null;
                if (uri != null) {
                    path = uri.getPath();
                    File file = new File(uri.getPath());
                    String ext1 = FilenameUtils.getExtension(path);
                    Log.d(TAG, "onReceive: is file exist  " + file.exists());
                    Log.d(TAG, "onReceive: name of file = " + ext1);

                    List<com.example.zambimusic.roomdb.Song> songs = Util.getAllMp3(MediaStore.MediaColumns.TITLE, App.this);
                    com.example.zambimusic.roomdb.Song[] songsArr = new Song[songs.size()];
                    songsArr = songs.toArray(songsArr);
                    appViewModel.insert(songsArr);
                }
                //TODO:refresh list in path

            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        appViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(AppViewModel.class);

        IntentFilter scanFileReceiverFilter= new IntentFilter();
        scanFileReceiverFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        scanFileReceiverFilter.addDataScheme("file");


        this.registerReceiver(scanFileReceiver, scanFileReceiverFilter);

    }

    public AppViewModel getViewmodel(){
        return appViewModel;
    }
}
