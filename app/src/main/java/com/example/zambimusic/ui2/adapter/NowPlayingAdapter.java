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
import androidx.recyclerview.widget.RecyclerView;


import com.example.zambimusic.R;
import com.example.zambimusic.data.Constants;
import com.example.zambimusic.roomdb.model.Audio;
import com.example.zambimusic.ui.main.Button.CustomToggleImageButton;
import com.example.zambimusic.ui2.ivutil.ImageViewSetter;
import com.example.zambimusic.ui2.ivutil.ImageViewSetterFactory;


import java.util.Collections;
import java.util.List;

public class NowPlayingAdapter extends RecyclerView.Adapter<BottomViewHolder> {

    List<Audio> list = Collections.emptyList();
    Context context;
    private OnItemClickListener mListener;

    public NowPlayingAdapter(List<Audio> list, Context context) {
        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public BottomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.now_playing_bottom, parent, false);
        BottomViewHolder holder = new BottomViewHolder(v,mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BottomViewHolder holder, int position) {
        Audio audio = list.get(position);

        holder.title.setText(audio.getTitle());
        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri =  ContentUris.withAppendedId(sArtworkUri, Long.parseLong(audio.getAlbum_id()) );
        ImageViewSetter imageViewSetter = ImageViewSetterFactory.getImageViewSetter(Constants.NOW_PLAYING_IV_SMALL);
        imageViewSetter.setImageView(holder.album_art, uri);
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
        void onPlayButtonClicked(int position);
        void onItemClickListener(int position);

    }
}

class BottomViewHolder extends RecyclerView.ViewHolder {

    CustomToggleImageButton customToggleImageButton;
    TextView title;
    ImageView album_art;


    BottomViewHolder(View itemView , final NowPlayingAdapter.OnItemClickListener listener) {
        super(itemView);
        album_art = (ImageView) itemView.findViewById(R.id.imageView_small);
        title = (TextView) itemView.findViewById(R.id.title);

        title.setSelected(true);
        customToggleImageButton = (CustomToggleImageButton) itemView.findViewById(R.id.btRepeat);
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



        customToggleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customToggleImageButton.nextState();
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onPlayButtonClicked(position);
                    }
                }
            }
        });
    }

}

