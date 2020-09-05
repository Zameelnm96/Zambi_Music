package com.example.zambimusic.ui2.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import com.example.zambimusic.ui2.CustomTouchListener;
import com.example.zambimusic.ui2.OnItemClickListener;
import com.example.zambimusic.ui2.PageViewModel;
import com.example.zambimusic.ui2.adapter.SongListAdapter;

import java.util.ArrayList;

public class AllSongsFragment extends Fragment {
    private ArrayList<Audio> audioList = new ArrayList<>();

    public static AllSongsFragment newInstance(){
        return new AllSongsFragment();
    }

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    View view;
    Activity activity;
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

    private void initRecyclerView() {
        if (audioList.size() > 0) {
            RecyclerView recyclerView = (RecyclerView)  activity.findViewById(R.id.recyclerview_all_song);
            SongListAdapter adapter = new SongListAdapter(audioList, activity.getApplication());
            recyclerView.setAdapter(adapter);
            recyclerView.setFocusable(true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.addOnItemTouchListener(new CustomTouchListener(activity, new OnItemClickListener() {
                @Override
                public void onClick(View view, int index) {
                    //playAudio(index);
                }
            }));



        }
    }




}
