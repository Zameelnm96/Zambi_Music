package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {
    ArrayList<Song> albumSongs = new ArrayList<>();
    AlbumAdapter albumAdapter;
    RecyclerView recyclerView;
    TextView albumName,albumComposer;
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
        albumAdapter = new AlbumAdapter(albumSongs);
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
}
