package com.example.zambimusic.ui2.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.zambimusic.R;
import com.example.zambimusic.Util;
import com.example.zambimusic.roomdb.model.Audio;
import com.example.zambimusic.ui.main.Button.CustomToggleImageButton;
import com.example.zambimusic.ui2.CustomTouchListener;
import com.example.zambimusic.ui2.OnItemClickListener;
import com.example.zambimusic.ui2.PageViewModel;
import com.example.zambimusic.ui2.adapter.SongListAdapter;
import com.example.zambimusic.ui2.service.MediaPlayerService;
import com.example.zambimusic.ui2.service.StorageUtil;

import java.util.ArrayList;

import static com.example.zambimusic.MainActivity3.Broadcast_PLAY_NEW_AUDIO;

public class AllSongsFragment extends Fragment implements SongListAdapter.OnItemClickListener {
    private ArrayList<Audio> audioList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManger;
    private MediaPlayerService mediaPlayerService;
    private boolean isBounded;

    public static AllSongsFragment newInstance(){
        return new AllSongsFragment();
    }

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    View view;
    Activity activity;

    RecyclerView recyclerView;
    SongListAdapter adapter;

    private static final String TAG = "AllSongsFragment";
    public static AllSongsFragment newInstance(int index) {
        AllSongsFragment fragment = new AllSongsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.all_song_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Util.loadAudio(activity, audioList);
        initRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void initRecyclerView() {
        if (audioList.size() > 0) {
            recyclerView = (RecyclerView)  activity.findViewById(R.id.recyclerview_all_song);
            adapter = new SongListAdapter(audioList, activity.getApplication());
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setFocusable(true);
            recyclerView.setHasFixedSize(true);
            linearLayoutManger = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(linearLayoutManger);
            recyclerView.addOnItemTouchListener(new CustomTouchListener(activity, new OnItemClickListener() {
                @Override
                public void onClick(View view, int index) {
                    //playAudio(index);
                }
            }));



        }
    }


    @Override
    public void onItemClickListener(int position) {
        Log.d(TAG, "onItemClickListener: ");
        StorageUtil storageUtil = new StorageUtil(activity);
        storageUtil.storeAudio(audioList);
        storageUtil.storeAudioIndex(position);

        if(!isBounded){
            Intent playerIntent = new Intent(activity, MediaPlayerService.class);
            activity.startService(playerIntent);
            activity.bindService(playerIntent,serviceConnection,Context.BIND_AUTO_CREATE);
            isBounded = true;
        }else {
            //Store the new audioIndex to SharedPreferences


            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            CustomToggleImageButton customToggleImageButton = (CustomToggleImageButton) activity.findViewById(R.id.btnPlayPauseBottom);
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            activity.sendBroadcast(broadcastIntent);
        }



        Intent intent = new Intent(Broadcast_PLAY_NEW_AUDIO);
        activity.sendBroadcast(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            mediaPlayerService = (MediaPlayerService) binder.getService();

            isBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBounded = false;
        }
    };
}
