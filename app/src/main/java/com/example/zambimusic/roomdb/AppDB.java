package com.example.zambimusic.roomdb;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.zambimusic.Util;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Song.class},version = 1,exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    private static final String TAG = "AppDB";
    public abstract SongDao songDao();
    private static Context mcontext;
    private static AppDB instance;
    public static AppDB getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AppDB.class) {
                if (instance == null) {
                    Log.d(TAG, "getDatabase: ");
                    instance =  Room.databaseBuilder(context,
                            AppDB.class, "app-database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        mcontext =  context;
        return instance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    Log.d(TAG, "onCreate: called");

                }

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);

                    new PopulateDbAsync(instance).execute();
                    Log.d(TAG, "onOpen: task excuted..");
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final SongDao mDao;
        // String[] words = {"dolphin", "crocodile", "cobra"};

        PopulateDbAsync(AppDB db) {
            mDao = db.songDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAll();
            List<Song> songs = Util.getAllMp3(MediaStore.MediaColumns.TITLE, mcontext);
            Song[] songsArr = new Song[songs.size()];
            songsArr = songs.toArray(songsArr);
            mDao.insertAll(songsArr);
            Log.d(TAG, "doInBackground: inserted completed");
            return null;
        }
    }


}
