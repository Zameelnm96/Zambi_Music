package com.example.zambimusic.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.zambimusic.roomdb.AppDB;
import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.roomdb.SongDao;

import java.util.List;

public class SongRepository {

    private static final String TAG = "SongRepository";
    private SongDao songDao;
    private LiveData<List<Song>> allSongs;

    public SongRepository(Application application) {
        Log.d(TAG, "SongRepository: constructor called");
        AppDB appDatabase = AppDB.getDatabase(application);
        songDao = appDatabase.songDao();
        allSongs = songDao.getAll();
    }

    public LiveData<List<Song>> getAllSongs() {
        return allSongs;
    }

    public void insert(Song... song){
        new InsertAsyncTask(songDao).execute(song);
        Log.d(TAG, "insert: completed");
    }
    
   

    public void delete(Song song){
        new DeleteAsyncTask(songDao).execute(song);
    }

    public void deleteAll(){ new DeleteAllAsyncTask(songDao).execute(); }

    private static class InsertAsyncTask extends AsyncTask<Song, Void, Void> {

        private SongDao mAsyncTaskDao;

        InsertAsyncTask(SongDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Song... params) {
            mAsyncTaskDao.insertAll(params[0]);
            Log.d(TAG, "doInBackground: insert executed");
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Song, Void, Void> {

        private SongDao mAsyncTaskDao;

        DeleteAsyncTask(SongDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Song... params) {
            mAsyncTaskDao.delete(params[0]);
            Log.d(TAG, "doInBackground: deletion of song " + params[0].getName());
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private SongDao mAsyncTaskDao;

        public DeleteAllAsyncTask(SongDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }


        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            Log.d(TAG, "doInBackground: database cleared");
            return null;
        }
    }
}