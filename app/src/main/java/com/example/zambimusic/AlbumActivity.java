package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity implements AlbumAdapter.ItemClicked, PlayService.ServiceCallback,PopupMenu.OnMenuItemClickListener {
    ArrayList<Song> albumSongs = new ArrayList<>();
    AlbumAdapter albumAdapter;
    RecyclerView recyclerView;
    TextView albumName,albumComposer;
    PlayService playService;
    Button btnAlbumMenu;
    boolean isBounded = false;
    private int position;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }//set tranparent notification bar

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        albumSongs = (ArrayList<Song>) bundle.getSerializable("albumSongs");
        Song song = albumSongs.get(0);
        ImageView ivImageView = findViewById(R.id.ivAlbumArtPlayActivity);
        Picasso.get().load(song.getUriAlbumArt(song.getAlbumId())).fit().into(ivImageView);
        recyclerView = findViewById(R.id.recyclerViewAlbum);
        albumAdapter = new AlbumAdapter(this,albumSongs);
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        albumName = findViewById(R.id.tvAlbum);
        albumComposer = findViewById(R.id.tvComposer);
        btnAlbumMenu = findViewById(R.id.btnAlbumMenu);
        btnAlbumMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AlbumActivity.this,btnAlbumMenu);
                popupMenu.setOnMenuItemClickListener(AlbumActivity.this);
                popupMenu.inflate(R.menu.pop_up_menu_album);
                popupMenu.show();
            }
        });
        String album;
        if (song.getAlbum() !=null){
            album = song.getAlbum();
        }
        else {
            album = "Unknown";
        }
        albumName.setText(album);
        String composer;
        if (song.getComposer()!= null){
            composer=song.getComposer();
        }
        else{
            composer = "Unknown";
        }
        albumComposer.setText("Composer- "+composer);

    }

    @Override
    public void onItemClicked(int index) {
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
                playService = myBinder.getService();
                playService.setList(albumSongs);
                playService.setPosition(position);
                playService.setSong();
                playService.setServiceCallback(AlbumActivity.this);
                isBounded = true;
                unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isBounded = false;
            }
        };
        position = index;
        Intent playIntent = new Intent(this,PlayService.class);
        bindService(playIntent,serviceConnection,Context.BIND_AUTO_CREATE);
        Intent intent = new Intent(this,PlayActivity.class);
        intent.putExtra("index",index);
        Bundle bundle = new Bundle();
        bundle.putSerializable("songs",albumSongs);
        intent.putExtras(bundle);
        intent.putExtra("class name","AlbumActivity");
        startActivity(intent);

    }

    @Override
    public void callSetView(Song song) {

    }

    @Override
    public void updateSeekbar() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemPlayNextAlbum:


                bindService(new Intent(this,PlayService.class),
                        new ServiceConnection() {
                            @Override
                            public void onServiceConnected(ComponentName name, IBinder service) {
                                PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
                                playService = myBinder.getService();
                                playService.addToPlaynext(albumSongs);
                                isBounded = true;
                                unbindService(this);
                            }

                            @Override
                            public void onServiceDisconnected(ComponentName name) {
                                isBounded = false;
                            }
                        },Context.BIND_AUTO_CREATE);
                return true;
            case R.id.itemAddToQueueAlbum:
                bindService(new Intent(this,PlayService.class),
                        new ServiceConnection() {
                            @Override
                            public void onServiceConnected(ComponentName name, IBinder service) {
                                PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
                                playService = myBinder.getService();
                                playService.addToqueue(albumSongs);
                                isBounded = true;
                                unbindService(this);
                            }

                            @Override
                            public void onServiceDisconnected(ComponentName name) {
                                isBounded = false;
                            }
                        },Context.BIND_AUTO_CREATE);
                return true;
        }
        return false;
    }
}
