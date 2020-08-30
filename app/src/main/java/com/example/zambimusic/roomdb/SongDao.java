package com.example.zambimusic.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {

    @Query("SELECT * FROM song")
    LiveData<List<Song>> getAll();

    @Query("SELECT * FROM song WHERE name LIKE :name ")
    Song findByName(String name);

    @Insert
    void insertAll(Song... androids);

    @Delete
    void delete(Song android);

    @Query("DELETE FROM song")
    void deleteAll();
}
