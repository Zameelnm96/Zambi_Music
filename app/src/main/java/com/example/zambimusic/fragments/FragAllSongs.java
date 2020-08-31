package com.example.zambimusic.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zambimusic.PlayActivity;
import com.example.zambimusic.service.PlayService;
import com.example.zambimusic.R;
import com.example.zambimusic.SongAdapter;
import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.service.ServiceUtil;
import com.example.zambimusic.viewmodel.AppViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FragAllSongs extends Fragment implements SongAdapter.ItemClicked, SongAdapter.ItemLongClicked, View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    RecyclerView recyclerView;
    SongAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    View view;
    Context parentActivity;
    FloatingActionButton floatingActionButton;


    PlayService playService;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<Song> mSongs;
    private String sortOrderByName = MediaStore.MediaColumns.DISPLAY_NAME;
    private String sortOrderByDateModified = MediaStore.MediaColumns.DATE_MODIFIED;
    private String sortOrderByDateAdded = MediaStore.MediaColumns.DATE_ADDED;

    private String sortBy;
    private boolean reversed;
    int position;

    TextView textView ;
    private AppViewModel appViewModel;
    private static final String TAG = "FragAllSongs";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_songs,container,false);
        floatingActionButton = view.findViewById(R.id.fab);
        if(floatingActionButton != null){
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(parentActivity,PlayActivity.class);

                    startActivity(intent);
                }
            });
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(parentActivity);
        editor = sharedPreferences.edit();

        recyclerView = view.findViewById(R.id.recyclerView_all_song);
        recyclerView.setHasFixedSize(true);
        Log.d(TAG, "onActivityCreated: called");
        layoutManager = new LinearLayoutManager(this.getActivity());
        adapter = new SongAdapter(this);
        recyclerView.setLayoutManager(layoutManager);


        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        appViewModel.getAllSongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                Log.d(TAG, "onChanged: called of observer with size of list " + songs.size());

                mSongs = (ArrayList<Song>) songs;
                adapter.setSongs(songs);
                recyclerView.setAdapter(adapter);
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity =  context;
    }

    public static FragAllSongs newInstance(){
        return new FragAllSongs();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemClicked(int index) {
        ServiceConnection serviceConnection= new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PlayService.MyBinder myBinder = (PlayService.MyBinder) service;
                playService = myBinder.getService();
                //playService.stop();
                playService.setList(mSongs);
                playService.setPosition(position);
                ServiceUtil.setSong(playService,playService.getMediaPlayer(),playService.getSongs().get(playService.getPosition()));
                parentActivity.unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        position = index;
        Intent playIntent = new Intent(parentActivity,PlayService.class);
        parentActivity.bindService(playIntent,serviceConnection,Context.BIND_AUTO_CREATE);

        Intent intent = new Intent(parentActivity, PlayActivity.class);
        intent.putExtra("index",index);
        Bundle bundle = new Bundle();
        bundle.putSerializable("songs", mSongs);
        intent.putExtras(bundle);
        intent.putExtra("class name","MusicList");

        /*Intent intent = new Intent(this,PlayActivity.class);
        intent.putExtra("song",(Parcelable) songs.get(index));*/

        startActivity(intent);
    }
    private Long  TempAlbumID;
    @Override
    public void onLongItemClicked(int index, View v) {
        position = index;
        PopupMenu popupMenu = new PopupMenu(parentActivity,v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.pop_up_song_menu);
        popupMenu.show();

        TempAlbumID = mSongs.get(index).getAlbumId();
    }


}
