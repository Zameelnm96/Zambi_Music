package com.example.zambimusic.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.zambimusic.roomdb.AppDB;
import com.example.zambimusic.roomdb.Playlist;
import com.example.zambimusic.roomdb.dao.PlaylistDao;

import java.util.List;

public class PlaylistRepository {

    private static final String TAG = "PlaylistRepository";
    private PlaylistDao playlistDao;
    private LiveData<List<Playlist>> playlists;

    public PlaylistRepository(Context context) {
        Log.d(TAG, "PlaylistRepository: constructor called");
        AppDB appDatabase = AppDB.getDatabase(context);
        playlistDao = appDatabase.playlistDao();
    }

    public LiveData<List<Playlist>> getAll(){
        return playlistDao.getAllPlaylist();
    }

    public LiveData<List<Playlist>> filterByName(String playlistName) {
        return  playlistDao.getPlaylistByName(playlistName);
    }

    public void insertAll(Playlist... playlists){
        new InsertAsyncTask(playlistDao).execute(playlists);
        Log.d(TAG, "insert: completed");
    }
    
   

    public void delete(Playlist playlist){
        new DeleteAsyncTask(playlistDao).execute(playlist);
    }
    public void delete(Playlist[] playlist){
        new DeleteAsyncTask(playlistDao).execute(playlist);
    }


    private static class InsertAsyncTask extends AsyncTask<Playlist, Void, Void> {

        private PlaylistDao mAsyncTaskDao;

        InsertAsyncTask(PlaylistDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Playlist... params) {
            try {
                mAsyncTaskDao.insertAll(params[0]);
            }
            catch (Exception e){

            }

            Log.d(TAG, "doInBackground: insert executed");
            return null;
        }
    }



    private static class DeleteAsyncTask extends AsyncTask<Playlist, Void, Void> {

        private PlaylistDao playlistDao;

        public DeleteAsyncTask(PlaylistDao mAsyncTaskDao) {
            this.playlistDao = mAsyncTaskDao;
        }


        @Override
        protected Void doInBackground(final Playlist... params) {
            playlistDao.delete(params[0]);

            Log.d(TAG, "doInBackground: database cleared");
            return null;
        }
    }
}