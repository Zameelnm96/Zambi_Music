package com.example.zambimusic.ui2.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;




import com.example.zambimusic.R;
import com.example.zambimusic.data.Constants;
import com.example.zambimusic.roomdb.model.Album;
import com.example.zambimusic.ui2.ivutil.ImageViewSetter;
import com.example.zambimusic.ui2.ivutil.ImageViewSetterFactory;

import java.util.Collections;
import java.util.List;

public class AlbumListMainAdapter extends RecyclerView.Adapter<AlbumListViewHolder> {

    List<Album> list = Collections.emptyList();
    Context context;
    private OnItemClickListener mListener;

    public AlbumListMainAdapter(List<Album> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public AlbumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_album_list, parent, false);
        AlbumListViewHolder holder = new AlbumListViewHolder(v,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListViewHolder holder, int position) {
        Album album = list.get(position);

        holder.album_name.setText(album.getAlbum_name());
        holder.tvComposer.setText(album.getComposer());

        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri =  ContentUris.withAppendedId(sArtworkUri, Long.parseLong(album.getAlbum_id()) );
        ImageViewSetter smallImageViewSetter = ImageViewSetterFactory.getImageViewSetter(Constants.NOW_PLAYING_IV_SMALL);
        smallImageViewSetter.setImageView(holder.album_ar_medium, uri);


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

        void onAlbumPlayClicked(int position);
        void onMoreOptionClicked(int position);
        void onItemClickListener(int position);

    }
}

class AlbumListViewHolder extends RecyclerView.ViewHolder {

    ImageButton btnMoreOption;
    ImageButton btn_album_play;
    TextView album_name;
    TextView tvComposer;
    ImageView album_ar_medium;


    AlbumListViewHolder(View itemView , final AlbumListMainAdapter.OnItemClickListener listener) {
        super(itemView);
        btnMoreOption = itemView.findViewById(R.id.more_option);
        btn_album_play = itemView.findViewById(R.id.album_play);

        album_name = itemView.findViewById(R.id.tvAlbumName);
        tvComposer = itemView.findViewById(R.id.albumComposer);

        album_ar_medium = itemView.findViewById(R.id.album_art_medium);


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
        btnMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onMoreOptionClicked(position);
                    }
                }
            }
        });

        btn_album_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onAlbumPlayClicked(position);
                    }
                }
            }
        });







    }

}

