package com.example.zambimusic;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.roomdb.SongTypeConverter;
import com.example.zambimusic.roomdb.dao.SongDao;
import com.example.zambimusic.roomdb.model.Album;
import com.example.zambimusic.roomdb.model.Audio;
import com.example.zambimusic.viewmodel.AppViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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

    public static Long[] getAllIDs(ArrayList<Song> songs){
        ArrayList<Long> longs = new ArrayList<>();
        Long[] longsArr = new Long[longs.size()];

        return longs.toArray(longsArr);
    }

    //from ui 2

    public static void loadAudio(Context context , ArrayList<Audio> audio) {
        ContentResolver contentResolver = context.getContentResolver();
        ArrayList<Audio> audioList = null;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cursor = contentResolver.query(uri, null, selection, null, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();

            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID));
                String audio_uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id ).toString();
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String album_id = cursor.getString( cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                // Save to audioList
                audioList.add(new Audio(Long.toString(id),album_id,audio_uri, title, album, artist));

                String composer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.COMPOSER));
            }
        }
        if(audioList !=null){
            audio.addAll(audioList);
        }


        cursor.close();



    }

    public static void loadAlbum(Context context, ArrayList<Album> mAlbums){
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = getAlbumAlbumcursor(context);
        ArrayList <Album> albums = null;
        if (cursor != null && cursor.getCount() > 0) {
            albums = new ArrayList<>();
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String album_name = cursor.getString(1);
                String artists = cursor.getString(2);
                albums.add(new Album(id,album_name,artists));
            }
        }

        if(albums != null){
            mAlbums.addAll(albums);
        }
    }

    public static Uri getSongUri(String id){
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id) );
    }
    public static File toFile(Uri uri){
        return  new File(Objects.requireNonNull(uri.getPath()));
    }

    public static File toFile(String uri){
        return  new File(Objects.requireNonNull(Uri.parse( uri).getPath()));
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%02d:%02d",m,s);
    }

    public static Cursor getAlbumAlbumcursor(Context context)
    {
        String where = null;
        ContentResolver cr = context.getContentResolver();
        final Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        final String _id = MediaStore.Audio.Albums._ID;
        final String album_id = MediaStore.Audio.Albums.ALBUM_ID;
        final String album_name =MediaStore.Audio.Albums.ALBUM;
        final String artist = MediaStore.Audio.Albums.ARTIST;
        final String[]columns={_id,album_name, artist};
        Cursor cursor = cr.query(uri,columns,where,null, null);
        return cursor;
    }
}
