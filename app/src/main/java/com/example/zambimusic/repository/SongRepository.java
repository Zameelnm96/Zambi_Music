package com.example.zambimusic.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.zambimusic.roomdb.AppDB;
import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.roomdb.dao.SongDao;

import java.util.ArrayList;
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

    public LiveData<List<Song>> getAll() {
        return allSongs;
    }

    public void insertModels(Song... song){
        new InsertAsyncTask(songDao).execute(song);
        Log.d(TAG, "insert: completed");
    }

    public void deleteModel(Song song){new DeleteSongsAsyncTask(songDao).execute(song);}
    public void deleteModel(Song[] songs){ new DeleteSongsAsyncTask(songDao).execute(songs); }
    public void deleteAllExcept(Long[] longs){  new DeleteExistAsyncTaskWith(songDao).execute(longs); }

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



    private static class DeleteSongsAsyncTask extends AsyncTask<Song, Void, Void> {

        private SongDao mAsyncTaskDao;

        public DeleteSongsAsyncTask(SongDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }


        @Override
        protected Void doInBackground(final Song... params) {

            mAsyncTaskDao.deleteSongs(params[0]);
            Log.d(TAG, "doInBackground: database cleared");
            return null;
        }
    }

    private static class DeleteExistAsyncTaskWith extends AsyncTask<Long, Void, Void> {

        private SongDao mAsyncTaskDao;

        public DeleteExistAsyncTaskWith(SongDao mAsyncTaskDao) {
            this.mAsyncTaskDao = mAsyncTaskDao;
        }



        @Override
        protected final Void doInBackground(final Long... params) {

            mAsyncTaskDao.deleteAllExcept(params[0]);
            Log.d(TAG, "doInBackground: database cleared");
            return null;
        }
    }
}