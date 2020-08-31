package com.example.zambimusic.roomdb;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Song implements Comparable<com.example.zambimusic.Song> ,Parcelable, Serializable {
    @PrimaryKey
    private long id;
    private String name;
    private String path;
    private String album;
    private String artists;
    private String dateModified;
    private String dateAdded;
    private String composer;
    private String duration;
    private long albumId;



    public Song(long id) {
        this.id = id;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setComposer(String composer) {
        this.composer = composer;
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

    public String getComposer() {
        return composer;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getDuration() {
        return duration;
    }

    public Uri getUri(){
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

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
        id = in.readLong();
    }

    public Uri getUriAlbumArt(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId); // return the uri of album art.
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
        dest.writeLong(id);
    }

    @Override
    public int compareTo(com.example.zambimusic.Song o) {
        if (getName() == null || o.getName() == null)
            return 0;
        return  getName().compareTo(o.getName());
    }
}
