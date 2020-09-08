package com.example.zambimusic.ui2.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zambimusic.R;
import com.example.zambimusic.data.Constants;
import com.example.zambimusic.roomdb.model.Audio;
import com.example.zambimusic.ui2.ivutil.ImageViewSetter;
import com.example.zambimusic.ui2.ivutil.ImageViewSetterFactory;


import java.util.Collections;
import java.util.List;

public class NowPlayingMainAdapter extends RecyclerView.Adapter<MainViewHolder> {



    List<Audio> list = Collections.emptyList();
    Context context;
    private OnItemClickListener mListener;

    public NowPlayingMainAdapter( Context context) {

        this.context = context;
    }

    public void setList(List<Audio> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.now_playing_main_card, parent, false);
        MainViewHolder holder = new MainViewHolder(v,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Audio audio = list.get(position);

        holder.song_name.setText(audio.getTitle());
        holder.artist_name.setText(audio.getArtist());
        holder.album.setText(audio.getAlbum());
        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri =  ContentUris.withAppendedId(sArtworkUri, Long.parseLong(audio.getAlbum_id()) );
        ImageViewSetter smallImageViewSetter = ImageViewSetterFactory.getImageViewSetter(Constants.NOW_PLAYING_IV_SMALL);
        ImageViewSetter large_imageViewSetter = ImageViewSetterFactory.getImageViewSetter(Constants.NOW_PLAYING_IV_LARGE);
        smallImageViewSetter.setImageView(holder.album_art, uri);
        large_imageViewSetter.setImageView(holder.album_art_large,uri);

    }




    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public interface OnItemClickListener{

        void onMusicListClicked();
        void onMoreOptionClicked();
        void onItemClickListener(int position);

    }
}

class MainViewHolder extends RecyclerView.ViewHolder {

    AppCompatImageButton btnMusicList;
    AppCompatImageButton btnMoreOption;
    TextView song_name;
    TextView artist_name;
    TextView album;
    ImageView album_art;
    ImageView album_art_large;


    MainViewHolder(View itemView , final NowPlayingMainAdapter.OnItemClickListener listener) {
        super(itemView);
        album_art = (ImageView) itemView.findViewById(R.id.imageView_small);
        album_art_large = itemView.findViewById(R.id.album_art_large);

        song_name = (TextView) itemView.findViewById(R.id.song_name);
        artist_name = itemView.findViewById(R.id.artist_name);
        album = itemView.findViewById(R.id.album_name);
        song_name.setSelected(true);
        artist_name.setSelected(true);
        album.setSelected(true);


        btnMusicList = (AppCompatImageButton) itemView.findViewById(R.id.now_playing_list);
        btnMoreOption = itemView.findViewById(R.id.more_option);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClickListener(position);
                    }
                }
            }
        });

        btnMusicList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {


                    listener.onMusicListClicked();

                }
            }
        });

        btnMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMoreOptionClicked();
                }
            }
        });






    }

}

