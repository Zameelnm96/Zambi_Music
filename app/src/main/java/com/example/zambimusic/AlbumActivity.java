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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity implements AlbumAdapter.ItemClicked, PlayService.ServiceCallback {
    ArrayList<Song> albumSongs = new ArrayList<>();
    AlbumAdapter albumAdapter;
    RecyclerView recyclerView;
    TextView albumName,albumComposer;
    PlayService playService;
    boolean isBounded = false;
    private int position;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
            playService = myBinder.getService();
            playService.setList(albumSongs);
            playService.setPosition(position);
            playService.setSong();
            playService.setServiceCallback(AlbumActivity.this);
            isBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBounded = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }//set tranparent notification bar

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        albumSongs = (ArrayList<Song>) bundle.getSerializable("albumSongs");
        Song song = albumSongs.get(0);
        ImageView ivImageView = findViewById(R.id.ivAlbumArt);
        Picasso.get().load(song.getUriAlbumArt(song.getAlbumId())).fit().into(ivImageView);
        recyclerView = findViewById(R.id.recyclerViewAlbum);
        albumAdapter = new AlbumAdapter(this,albumSongs);
        recyclerView.setAdapter(albumAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        albumName = findViewById(R.id.tvAlbum);
        albumComposer = findViewById(R.id.tvComposer);
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
        position = index;
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
}
