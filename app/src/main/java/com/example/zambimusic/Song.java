package com.example.zambimusic;

public class Song {
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

    public Song(long id, String name, String path, String album, String artists, String dateModified, String dateAdded, String composer, String duration, long albumId) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.album = album;
        this.artists = artists;
        this.dateModified = dateModified;
        this.dateAdded = dateAdded;
        this.composer = composer;
        this.duration = duration;
        this.albumId = albumId;
    }
}
