package com.example.zambimusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {
    private List<Song> songs;
    private Context context;
    private ItemClicked itemClicked;
    private ItemLongClicked itemLongClicked;

    public SongAdapter( Context context,List<Song> songs) {
        this.context = context;
        itemClicked = (ItemClicked) context;
        itemLongClicked = (ItemLongClicked) context;
        this.songs = songs;

    }
    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setTag(songs.get(position));
        Song song = songs.get(position);
        holder.tvSong.setText(song.getName());
        holder.tvArtists.setText(song.getArtists());
        Picasso.get()
                .load(song.getUriAlbumArt(song.getAlbumId()).toString())       //here we load image with album art uri uri
                .error(R.drawable.ic_icon)
                .into(holder.ivAlbumArt);

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSong,tvArtists;
        private ImageView ivAlbumArt;
        Button ivMenu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSong = itemView.findViewById(R.id.tvSong);
            tvArtists = itemView.findViewById(R.id.tvPlayArtist);
            ivAlbumArt = itemView.findViewById(R.id.ivAlbumArt);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.onItemClicked(songs.indexOf((Song) v.getTag()));

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemLongClicked.onLongItemClicked(songs.indexOf((Song) v.getTag()),v);
                    return true;
                }
            });
        }


    }

    public interface ItemClicked{
        public void onItemClicked(int index);
    }
    public interface ItemLongClicked{
        public void onLongItemClicked(int index,View v);
    }


}
