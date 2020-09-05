package com.example.zambimusic.roomdb.model;

public class Album { //independent from song table

    private String album_id;
    private String album_name;
    private String composer;

    public Album(String album_id, String album_name, String composer) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.composer = composer;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public String getComposer() {
        return composer;
    }
}
