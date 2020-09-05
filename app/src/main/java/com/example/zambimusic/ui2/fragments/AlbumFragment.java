package com.example.zambimusic.ui2.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.zambimusic.R;
import com.example.zambimusic.Util;
import com.example.zambimusic.roomdb.model.Album;
import com.example.zambimusic.ui2.PageViewModel;
import com.example.zambimusic.ui2.adapter.AlbumListMainAdapter;

import java.util.ArrayList;

public class AlbumFragment extends Fragment implements AlbumListMainAdapter.OnItemClickListener {
    public static AlbumFragment newInstance(){
        return new AlbumFragment();
    }

    private static final String TAG = "AlbumFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    View view;
    Activity activity;
    ArrayList<Album> albumsList = new ArrayList<>();
    public static AlbumFragment newInstance(int index) {
        AlbumFragment fragment = new AlbumFragment();
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
        view = inflater.inflate(R.layout.album_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Util.loadAlbum(activity,albumsList);

        initRecyclerView();
    }

    @Override
    public void onAlbumPlayClicked(int position) {
        Log.d(TAG, "onAlbumPlayClicked: ");
    }

    @Override
    public void onMoreOptionClicked(int position) {
        Log.d(TAG, "onMoreOptionClicked: ");
    }

    @Override
    public void onItemClickListener(int position) {
        Log.d(TAG, "onItemClickListener: ");
    }

    private void initRecyclerView() {
        if (albumsList.size() > 0) {
            RecyclerView recyclerView = (RecyclerView)  activity.findViewById(R.id.recyclerViewAlbumList);
            AlbumListMainAdapter adapter = new AlbumListMainAdapter(albumsList, activity.getApplication());
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setFocusable(true);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(activity,2));


        }
    }
}
