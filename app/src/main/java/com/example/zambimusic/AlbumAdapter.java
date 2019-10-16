package com.example.zambimusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumAdapter  extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder>{
    ArrayList<Song> albumSongs;
    Context context;
    ItemClicked itemClicked;
    public AlbumAdapter(Context context, ArrayList<Song> albumSongs) {
        this.albumSongs = albumSongs;
        this.context = context;
        itemClicked = (ItemClicked) context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_album_song,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.itemView.setTag(position);
        String count = Integer.toString(position + 1);
        holder.tvNumber.setText( count );
        holder.tvName.setText(albumSongs.get(position).getName());
        holder.tvArtist.setText(albumSongs.get(position).getArtists());

    }

    @Override
    public int getItemCount() {
        return albumSongs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber,tvName,tvArtist;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            tvName = itemView.findViewById(R.id.tvName);
            tvArtist = itemView.findViewById(R.id.tvArtistPlayActivity);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClicked.onItemClicked((int)v.getTag());
                }
            });
        }
    }
    public interface ItemClicked{
        public void onItemClicked(int index);
    }
}
