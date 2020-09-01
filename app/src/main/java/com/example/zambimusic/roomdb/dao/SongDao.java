package com.example.zambimusic.roomdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.zambimusic.roomdb.Song;
import com.example.zambimusic.roomdb.SongTypeConverter;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SongDao {

    @Query("SELECT * FROM song")
    LiveData<List<Song>> getAll();

    @Query("SELECT * FROM song WHERE name LIKE :name ")
    Song findByName(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Song... androids);


    @Delete
    void deleteSongs(Song... songs);

    @Query( "DELETE FROM song \n" +
            "WHERE id NOT IN (:longs)")
    void deleteAllExcept(long... longs);

}
