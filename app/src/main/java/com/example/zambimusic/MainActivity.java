package com.example.zambimusic;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zambimusic.viewmodel.ViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity{



    Button btnSongs,btnAlbum,btnNowPlaying;
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewModel viewModel = new  ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllSongs().observe(this, new Observer<List<com.example.zambimusic.roomdb.Song>>() {
            @Override
            public void onChanged(List<com.example.zambimusic.roomdb.Song> songs) {
                for(com.example.zambimusic.roomdb.Song song: songs){
                    Log.d(TAG, "onChanged: song name " + song.getAlbum());
                }
            }
        });
        Intent playIntent = new Intent(this,PlayService.class);
        startService(playIntent);

        requestRuntimePermision();
        btnSongs = findViewById(R.id.btnSongs);
        btnAlbum = findViewById(R.id.btnAlbum);
        btnNowPlaying = findViewById(R.id.btnNowPlayingPlayActivity);
        btnSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,MusicList.class);
                startActivity(intent);


            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AlbumListActivity.class);
                startActivity(intent);
            }
        });
        btnNowPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,PlayActivity.class);
                intent.putExtra("class name","MainActivity");
                startActivity(intent);
            }
        });




    }



    @Override
    protected void onResume() {
        super.onResume();


    }
    private void requestRuntimePermision(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getApplicationContext(),"You should Accept permission to use this app",Toast.LENGTH_SHORT).show();
                showAlertDialogButtonClicked();

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }

    public void showAlertDialogButtonClicked() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog");
        builder.setMessage("You cannot use music player if you denied permission. Click continue to accept permission");

        // add the buttons

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // do something like...
                requestRuntimePermision();
            }
        }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                onDestroy();

            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static ArrayList<Song> GetAllMediaMp3Files(String sortBy,  Context context){
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

            Toast.makeText(context,"Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(context,"No Music Found on SD Card.", Toast.LENGTH_LONG);

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



}
