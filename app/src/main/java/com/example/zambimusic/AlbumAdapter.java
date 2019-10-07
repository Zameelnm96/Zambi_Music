package com.example.zambimusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumAdapter  extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder>{
    ArrayList<Song> albumSongs;
    public AlbumAdapter(ArrayList<Song> albumSongs) {
        this.albumSongs = albumSongs;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_album_song,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

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
            tvArtist = itemView.findViewById(R.id.tvArtist);

        }
    }
}
