package com.example.zambimusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicList extends AppCompatActivity implements SongAdapter.ItemClicked {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Song> songs;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout seekBarParent;
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        textView = findViewById(R.id.tvName);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.mediaPlayer!=null){
                }
            }
        });
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.mediaPlayer!=null){
                    if (MyApplication.mediaPlayer.isPlaying()){
                        btn.setBackgroundResource(R.drawable.ic_icon);
                        MyApplication.mediaPlayer.pause();
                        MyApplication.lastPosition = MyApplication.mediaPlayer.getCurrentPosition();
                    }
                    else
                    {
                        btn.setBackgroundResource(R.drawable.ic_pause_circular_button);
                        MyApplication.mediaPlayer.seekTo(MyApplication.lastPosition);
                        MyApplication.mediaPlayer.start();
                    }

                }
            }
        });


        seekBarParent = findViewById(R.id.seekBarParent);
        seekBarParent.setVisibility(View.INVISIBLE);
        if (MyApplication.mediaPlayer!=null){

            seekBarParent.setVisibility(View.VISIBLE);


        }
        
        songs = new ArrayList<>();
        fillMusicList();

        MyApplication.seekBar = findViewById(R.id.seekBar);


        recyclerView = findViewById(R.id.recyclerView);
        adapter = new SongAdapter(this,songs);
         layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        MyApplication.seekBar .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCurrentPosition = progress;
                MyApplication.mediaPlayer.seekTo(progress);
            }
        });


    }

    /*private List<String> musicFileList = new ArrayList<>();*/
    public void fillMusicList(){
        songs.clear();
        addMusicFileFrom(String.valueOf( Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC)));
        addMusicFileFrom(String.valueOf( Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS)));

    }
    public void addMusicFileFrom(String dirPath){
        final File musicDir = new File(dirPath);
        if (!musicDir.exists()){
            musicDir.mkdir();
            return;
        }
        final File[] files = musicDir.listFiles();
        for (File file:files){
            final String path = file.getAbsolutePath();
            if (path.endsWith(".mp3")){
                Song song = new Song(path);
                song.setName(getSongName(path));
                songs.add(song);
            }
        }
    }

    public void playMusicFile(String path){
        MyApplication.mediaPlayer = new MediaPlayer();
        try{
            MyApplication.mediaPlayer.setDataSource(path);
            MyApplication.mediaPlayer.prepare();
            MyApplication.mediaPlayer.start();
        }
        catch (Exception e ){
            e.printStackTrace();
        }

    }
    int mCurrentPosition;

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.mediaPlayer!=null){
                seekBarParent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
           }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this,"onBackPressed running",Toast.LENGTH_SHORT).show();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemClicked(int index) {
        if (MyApplication.mediaPlayer!=null) {
            MyApplication.mediaPlayer.stop(); // stop the media player
            MyApplication.mediaPlayer.reset();
        }
        seekBarParent.setVisibility(View.VISIBLE);
        MyApplication.seekBar .setVisibility(View.VISIBLE);
        btn.setBackgroundResource(R.drawable.ic_pause_circular_button);
        Song song = songs.get(index);
        playMusicFile(song.getPath());
        int duration =MyApplication.mediaPlayer.getDuration();
        MyApplication.seekBar .setMax(duration);
        final Handler handler = new Handler();
//Make sure you update Seekbar on UI thread
        MusicList.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(MyApplication.mediaPlayer != null){
                    mCurrentPosition = MyApplication.mediaPlayer.getCurrentPosition() ;
                    MyApplication.seekBar .setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 1000);
            }
        });



    }
    public String getSongName(String path){
        String[] temp = path.split("/");
        return temp[temp.length-1];
    }


}
