package com.example.zambimusic.ui2.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;





import com.example.zambimusic.R;
import com.example.zambimusic.data.Constants;
import com.example.zambimusic.roomdb.model.Audio;
import com.example.zambimusic.ui2.ivutil.ImageViewSetter;
import com.example.zambimusic.ui2.ivutil.ImageViewSetterFactory;

import java.util.Collections;
import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Audio> list = Collections.emptyList();
    Context context;

    public SongListAdapter(List<Audio> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_cardview, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        Audio audio = list.get(position);
        holder.title.setText(audio.getTitle());
        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri =  ContentUris.withAppendedId(sArtworkUri, Long.parseLong(audio.getAlbum_id()) );
        ImageViewSetter imageViewSetter = ImageViewSetterFactory.getImageViewSetter(Constants.NOW_PLAYING_IV_SMALL);
        imageViewSetter.setImageView(holder.play_pause, uri);
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

}

class ViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    ImageView play_pause;

    ViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        play_pause = (ImageView) itemView.findViewById(R.id.imageView_small);
    }
}




/**
 * Created by Valdio Veliu on 16-08-06.
 */

