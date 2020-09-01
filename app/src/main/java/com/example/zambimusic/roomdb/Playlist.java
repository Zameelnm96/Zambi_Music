package com.example.zambimusic.roomdb;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import static androidx.room.ForeignKey.CASCADE;
@Entity(foreignKeys = @ForeignKey(entity = Song.class ,parentColumns = "id",childColumns = "songId",onDelete = CASCADE) , primaryKeys = {"name","songId"})
public class Playlist {
    @NonNull
    private String name;
    @NonNull
    private long songId;

    public Playlist(String name, long songId) {
        this.name = name;
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public long getSongId() {
        return songId;
    }
}
