package com.example.zambimusic;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;




public class MusicList extends AppCompatActivity implements SongAdapter.ItemClicked, SongAdapter.ItemLongClicked, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    RecyclerView recyclerView;
    SongAdapter adapter;
    ArrayList<Song> songs;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout seekBarParent;
    Button btn;
    PlayService playService;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    private String sortOrderByName = MediaStore.MediaColumns.DISPLAY_NAME;
    private String sortOrderByDateModified = MediaStore.MediaColumns.DATE_MODIFIED;
    private String sortOrderByDateAdded = MediaStore.MediaColumns.DATE_ADDED;

    private String sortBy;
    private boolean reversed;
    int position;

    TextView textView ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();



        //setSupportActionBar(toolbar);
        Toast.makeText(this,"On create Running",Toast.LENGTH_SHORT).show();
        sortBy = sharedPreferences.getString("sortBy", MediaStore.MediaColumns.DATE_ADDED)+"";//get the data from storage
        reversed = sharedPreferences.getBoolean("reversed",false);
        //if reversed true only list will reverse. reverse variable save on storage.

        Toast.makeText(this,"SORT BY " + sortBy + "reversed " +reversed ,Toast.LENGTH_SHORT).show();
        textView = findViewById(R.id.tvName);

        songs = new ArrayList<>();
        ///GetAllMediaMp3Files(sortBy);
        songs = MainActivity.GetAllMediaMp3Files(sortBy,this);
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
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemName:

                songs = MainActivity.GetAllMediaMp3Files(sortOrderByName,this);
                if (reversed){
                    Collections.reverse(songs);
                }
                adapter.setSongs(songs);
                adapter.notifyDataSetChanged();
                editor.putString("sortBy", MediaStore.MediaColumns.DISPLAY_NAME);// save the state of sorting
                editor.commit();
                return true;
            case R.id.itemDateModified:

                songs = MainActivity.GetAllMediaMp3Files(sortOrderByDateModified,this);
                if (reversed){
                    Collections.reverse(songs);
                }
                adapter.setSongs(songs);
                adapter.notifyDataSetChanged();
                editor.putString("sortBy", MediaStore.MediaColumns.DATE_MODIFIED);
                editor.commit();
                return true;
            case R.id.itemDateAdded:

                songs = MainActivity.GetAllMediaMp3Files(sortOrderByDateAdded,this);
                if (reversed){
                    Collections.reverse(songs);
                }
                adapter.setSongs(songs);
                adapter.notifyDataSetChanged();
                editor.putString("sortBy", MediaStore.MediaColumns.DATE_ADDED);
                editor.commit();
                return true;
            case R.id.ascending:
                if (reversed){ // only reversed the list reversed equals true. if reversed equals true mean list in descending order.
                    reversed = false;
                    editor.putBoolean("reversed",false);// save the value on local storage like database
                    editor.commit();// must commit make changes
                    Collections.reverse(songs);
                    adapter.setSongs(songs);
                    adapter.notifyDataSetChanged();
                }
                return true;

            case R.id.descending:
                if (!reversed){
                    reversed = true;
                    editor.putBoolean("reversed",true);
                    editor.commit();
                    Collections.reverse(songs);
                    adapter.setSongs(songs);
                    adapter.notifyDataSetChanged();

                }
                return  true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(int index) {
        ServiceConnection serviceConnection= new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
                playService = myBinder.getService();
                playService.setList(songs);
                playService.setPosition(position);
                playService.setSong();
                unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        position = index;
        Intent playIntent = new Intent(this,PlayService.class);
        bindService(playIntent,serviceConnection,Context.BIND_AUTO_CREATE);

        Intent intent = new Intent(this,PlayActivity.class);
        intent.putExtra("index",index);
        Bundle bundle = new Bundle();
        bundle.putSerializable("songs",songs);
        intent.putExtras(bundle);
        intent.putExtra("class name","MusicList");

        /*Intent intent = new Intent(this,PlayActivity.class);
        intent.putExtra("song",(Parcelable) songs.get(index));*/

        startActivity(intent);
        //finish();


    }


    @Override
    public void onClick(View v) {

    }

    private Long  TempAlbumID;
    @Override
    public void onLongItemClicked(int index,View v) {
        position = index;
        PopupMenu popupMenu = new PopupMenu(this,v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.pop_up_song_menu);
        popupMenu.show();

        TempAlbumID = songs.get(index).getAlbumId();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.itemAlbum:
                Intent intent = new Intent(MusicList.this,AlbumActivity.class);
                ArrayList<Song> albumSongs = new ArrayList<>() ;
                for(Song song:songs){
                    if (song.getAlbumId() == TempAlbumID)
                        albumSongs.add(song);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("albumSongs",albumSongs);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;

            case R.id.itemPlayNext:
                ServiceConnection serviceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
                        playService = myBinder.getService();
                        Song song = songs.get(position);
                        playService.addToPlayNext(song);
                        unbindService(this);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
                Intent playIntent = new Intent(this,PlayService.class);
                bindService(playIntent,serviceConnection,Context.BIND_AUTO_CREATE);
                return true;
            case R.id.itemAddToQueue:


                bindService( new Intent(this,PlayService.class),
                        new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
                        playService = myBinder.getService();
                        Song song = songs.get(position);
                        playService.addToQueue(song);
                        unbindService(this);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, Context.BIND_AUTO_CREATE);
                return true;
        }
        return false;
    }


}


