package com.example.zambimusic.roomdb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Song {
    @PrimaryKey
    private long id;
    private String name;
    private String path;
    private String album;
    private String artists;
    private String dateModified;
    private String dateAdded;
    private String songComposer;
    private long albumId;



    public Song(long id) {
        this.id = id;
    }

    public void setSongComposer(String songComposer) {
        this.songComposer = songComposer;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtists() {
        return artists;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getSongComposer() {
        return songComposer;
    }

    public long getAlbumId() {
        return albumId;
    }

}
