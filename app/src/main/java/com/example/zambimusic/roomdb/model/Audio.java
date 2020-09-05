package com.example.zambimusic.roomdb.model;

import java.io.Serializable;

public class Audio implements Serializable {


    private String audio_Id;


    private String album_id;//insert as foriengn key
    private String uri;
    private String title;
    private String album;
    private String artist;
    public Audio(String audio_Id, String album_id, String uri, String title, String album, String artist) {
        this.audio_Id = audio_Id;
        this.album_id = album_id;
        this.uri = uri;
        this.title = title;
        this.album = album;
        this.artist = artist;
    }



    public String getAudio_Id() {
        return audio_Id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}