package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

public class MusicList extends AppCompatActivity implements SongAdapter.ItemClicked {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Song> songs;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout seekBarParent;
    Button btn;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    private String sortOrderByName = MediaStore.MediaColumns.DISPLAY_NAME;
    private String sortOrderByDateModified = MediaStore.MediaColumns.DATE_MODIFIED;
    private String sortOrderByDateAdded = MediaStore.MediaColumns.DATE_ADDED;

    private String sortBy;
    private boolean reversed;

    TextView textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();


        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Toast.makeText(this,"On create Running",Toast.LENGTH_SHORT).show();
        sortBy = sharedPreferences.getString("sortBy", MediaStore.MediaColumns.DATE_ADDED)+"";//get the data from storage
        reversed = sharedPreferences.getBoolean("reversed",false);
        //if reversed true only list will reverse. reverse variable save on storage.

        Toast.makeText(this,"SORT BY " + sortBy + "reversed " +reversed ,Toast.LENGTH_SHORT).show();
        textView = findViewById(R.id.tvName);

        songs = new ArrayList<>();
        GetAllMediaMp3Files(sortBy);
        if (reversed){
            Collections.reverse(songs);
        }
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new SongAdapter(this,songs);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_music_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemName:

                GetAllMediaMp3Files(sortOrderByName);
                adapter = new SongAdapter(this,songs);
                recyclerView.setAdapter(adapter);
                editor.putString("sortBy", MediaStore.MediaColumns.DISPLAY_NAME);// save the state of sorting
                editor.commit();
                return true;
            case R.id.itemDateModified:

                GetAllMediaMp3Files(sortOrderByDateModified);
                adapter = new SongAdapter(this,songs);
                recyclerView.setAdapter(adapter);
                editor.putString("sortBy", MediaStore.MediaColumns.DATE_MODIFIED);
                editor.commit();
                return true;
            case R.id.itemDateAdded:

                GetAllMediaMp3Files(sortOrderByDateAdded);
                adapter = new SongAdapter(this,songs);
                recyclerView.setAdapter(adapter);
                editor.putString("sortBy", MediaStore.MediaColumns.DATE_ADDED);
                editor.commit();
                return true;
            case R.id.ascending:
                if (reversed){ // only reversed the list reversed equals true. if reversed equals true mean list in descending order.
                    reversed = false;
                    editor.putBoolean("reversed",false);// save the value on local storage like database
                    editor.commit();// must commit make changes
                    Collections.reverse(songs);
                    adapter = new SongAdapter(this,songs);
                    recyclerView.setAdapter(adapter);
                }
                return true;

            case R.id.descending:
                if (!reversed){
                    reversed = true;
                    editor.putBoolean("reversed",true);
                    editor.commit();
                    Collections.reverse(songs);
                    adapter = new SongAdapter(this,songs);
                    recyclerView.setAdapter(adapter);
                }
                return  true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(int index) {
            textView.setText(songs.get(index).getDateModified());
    }

    public void GetAllMediaMp3Files(String sortBy){
        songs.clear();
        ContentResolver contentResolver;
        Cursor cursor;
        Uri uri;

        contentResolver = this.getContentResolver();

        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        cursor = contentResolver.query(
                uri, // Uri
                null,
                null,
                null,
                sortBy
        );

        if (cursor == null) {

            Toast.makeText(this,"Something Went Wrong.", Toast.LENGTH_LONG);

        } else if (!cursor.moveToFirst()) {

            Toast.makeText(this,"No Music Found on SD Card.", Toast.LENGTH_LONG);

        }
        else {

            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int dateModified = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED);
            int dateAdded = cursor.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED);



            //Getting Song ID From Cursor.
            //int id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {

                // You can also get the Song ID using cursor.getLong(id).
                //long SongID = cursor.getLong(id);

                String songTitle = cursor.getString(title);
                String songAlbum = cursor.getString(album);
                String songDateModified = cursor.getString(dateModified);
                String songDateAdded = cursor.getString(dateAdded);
                String songAtrists = cursor.getString(artist);


                // Adding Media File Names to ListElementsArrayList.
                Song song = new Song();
                song.setName(songTitle);
                song.setAlbum(songAlbum);
                song.setDateAdded(songDateAdded);
                song.setDateModified(songDateModified);
                song.setArtists(songAtrists);


                songs.add(song);

            } while (cursor.moveToNext());
        }
    }

}
