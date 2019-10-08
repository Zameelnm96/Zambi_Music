package com.example.zambimusic;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.io.Serializable;

public class Song implements Comparable<Song> , Parcelable, Serializable {


    long id;
    private String name;
    private String path;
    private String album;
    private String artists;
    private String dateModified;
    private String dateAdded;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    private String composer;
    private long albumId;

    public Song() {
    }

    protected Song(Parcel in) {
        name = in.readString();
        path = in.readString();
        album = in.readString();
        artists = in.readString();
        dateModified = in.readString();
        dateAdded = in.readString();
        albumId = in.readLong();
        duration = in.readString();
        composer = in.readString();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getAlbumId() {
        return albumId;
    }

    public Uri getUriAlbumArt(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId); // return the uri of album art.
    }

    public Uri getUri(){
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, getId());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(album);
        dest.writeString(artists);
        dest.writeString(dateModified);
        dest.writeString(dateAdded);
        dest.writeLong(albumId);
        dest.writeString(duration);
        dest.writeString(composer);
    }
}
