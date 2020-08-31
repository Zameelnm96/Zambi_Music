package com.example.zambimusic;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.zambimusic.roomdb.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class Util {
    private static final String TAG = "Util";
    public static List<com.example.zambimusic.roomdb.Song> getAllMp3(String sortBy, Context context){
        ArrayList<com.example.zambimusic.roomdb.Song> songs = new ArrayList<>();
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
                com.example.zambimusic.roomdb.Song song = new Song(SongId);

                song.setName(songTitle);
                song.setAlbum(songAlbum);
                song.setComposer(songComposer);
                song.setDateAdded(songDateAdded);
                song.setDateModified(songDateModified);
                song.setArtists(songAtrists);
                song.setAlbumId(parseLong);
                songs.add(song);

            } while (cursor.moveToNext());
        }
        return songs;
    }

    public static List<Song> sort(String sortBy, List<Song> songs,boolean reversed){

        if (reversed){
            Collections.reverse(songs);
        }
        switch (sortBy){
            case MediaStore.MediaColumns.DISPLAY_NAME:
                if (songs.size() > 0) {
                    Collections.sort(songs, new Comparator<Song>() {
                        @Override
                        public int compare(final Song object1, final Song object2) {
                            return object1.getName().compareTo(object2.getName());
                        }
                    });
                }

                return songs;

            case MediaStore.MediaColumns.DATE_MODIFIED:
                if (songs.size() > 0) {
                    Collections.sort(songs, new Comparator<Song>() {
                        @Override
                        public int compare(final Song object1, final Song object2) {
                            return object1.getDateModified().compareTo(object2.getDateModified());
                        }
                    });
                }
               return songs;
            default:
                return songs;
        }

    }
}
