package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class NowPlayingActivity extends AppCompatActivity implements AlbumAdapter.ItemClicked {
    ArrayList<Song> songs;
    PlayService playService;
    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    TextView tvSongname,tvArtist;
    ImageView ivAlbumArt;
    Button btnNowPlaying,btnMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        tvSongname = findViewById(R.id.tvSongNameNowPlaying);
        tvArtist = findViewById(R.id.tvArtistNowPlaying);
        btnNowPlaying = findViewById(R.id.btnNowPlayingList);
        btnMenu = findViewById(R.id.btnMenuNowPlaying);
        ivAlbumArt = findViewById(R.id.ivAlbumArtNowPlaying);


        bindService(new Intent(this,PlayService.class),
                new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
                        playService = myBinder.getService();
                        songs =  playService.getSongs();
                        recyclerView = findViewById(R.id.recyclerViewNowPlaying);
                        albumAdapter = new  AlbumAdapter(NowPlayingActivity.this,songs);
                        recyclerView.setAdapter(albumAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(NowPlayingActivity.this));
                        recyclerView.setHasFixedSize(true);
                        Song song = playService.getSong();
                        tvSongname.setText(song.getName());
                        tvArtist.setText(song.getArtists());
                        Picasso.get().load(song.getUri()).error(R.drawable.album).into(ivAlbumArt);
                        unbindService(this);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onItemClicked(int index) {

    }
}
