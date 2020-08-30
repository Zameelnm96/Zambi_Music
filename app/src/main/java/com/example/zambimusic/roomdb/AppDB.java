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
                    new PopulateDbAsync(instance).execute();
                }

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);

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
            List<Song> songs = getAllMp3(MediaStore.MediaColumns.TITLE, mcontext);
            Song[] songsArr = new Song[songs.size()];
            songsArr = songs.toArray(songsArr);
            mDao.insertAll(songsArr);
            Log.d(TAG, "doInBackground: inserted completed");
            return null;
        }
    }

    public static List<Song> getAllMp3(String sortBy, Context context){
        ArrayList<Song> songs = new ArrayList<>();
        ContentResolver contentResolver;
        Cursor cursor;
        Uri uri;

        contentResolver = context.getContentResolver();

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                sortBy
        );

        if (cursor == null) {

            Log.d(TAG, "getAllMp3: cursor is null");

        } else if (!cursor.moveToFirst()) {

            Log.d(TAG, "getAllMp3: no music found in sd card");

        }
        else {

            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int composer = cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER);
            int dateModified = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED);
            int dateAdded = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);
            int album_id = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID);// we can use this for get album art as define in Song class getUriAlbumArt() Method

            int duration = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);





            //Getting Song ID From Cursor.
            //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {

                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);
                long SongId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID));
                Log.d("xyz", "GetAllMediaMp3Files: id " + SongId);
                String songTitle = cursor.getString(title);
                String songAlbum = cursor.getString(album);
                String songComposer = cursor.getString(composer);
                String songDateModified = cursor.getString(dateModified);
                String songDateAdded = cursor.getString(dateAdded);
                String songAtrists = cursor.getString(artist);
                String songAbumId  = cursor.getString(album_id);
                String songDuration = cursor.getString(duration);
                long parseLong = Long.parseLong(songAbumId);




                // Adding Media File Names to ListElementsArrayList.
                Song song = new Song(SongId);

                song.setName(songTitle);
                song.setAlbum(songAlbum);
                song.setSongComposer(songComposer);
                song.setDateAdded(songDateAdded);
                song.setDateModified(songDateModified);
                song.setArtists(songAtrists);
                song.setAlbumId(parseLong);
                songs.add(song);

            } while (cursor.moveToNext());
        }
        return songs;
    }
}
