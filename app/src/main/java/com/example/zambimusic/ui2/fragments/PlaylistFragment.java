package com.example.zambimusic.ui2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zambimusic.R;
import com.example.zambimusic.ui2.PageViewModel;


public class PlaylistFragment extends Fragment {
    public static PlaylistFragment newInstance(){
        return new PlaylistFragment();
    }

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;
    View view;
    public static PlaylistFragment newInstance(int index) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.playlist_fragment, container, false);
        return view;
    }

}
