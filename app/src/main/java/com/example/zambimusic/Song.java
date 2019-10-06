package com.example.zambimusic;

import android.content.ContentUris;
import android.net.Uri;

public class Song implements Comparable<Song> {


    private String name;
    private String path;
    private String album;
    private String artists;
    private String dateModified;
    private String dateAdded;
    private long albumId;


    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public Uri getUriAlbumArt(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId); // return the uri of album art.
    }





    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    private String duration;

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getPath() {
        return path;
    }

    public void setPath() {
        this.path = path;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Song o) {
        if (getName() == null || o.getName() == null)
            return 0;
        return  getName().compareTo(o.getName());
    }
}
