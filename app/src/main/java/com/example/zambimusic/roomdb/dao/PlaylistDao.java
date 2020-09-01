package com.example.zambimusic.roomdb.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.zambimusic.roomdb.Playlist;
import com.example.zambimusic.roomdb.Song;

import java.util.List;

@Dao
public interface PlaylistDao {

    @Query("SELECT playlist.songId ,playlist.name FROM song INNER JOIN playlist on song.id = playlist.songId WHERE playlist.name LIKE :name  ")
    LiveData<List<Playlist>> getPlaylistByName(String name);

    @Query("SELECT  * FROM playlist")
    LiveData<List<Playlist>> getAllPlaylist();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Playlist... playlists);

    @Delete
    void delete(Playlist playlist);

    @Query( "DELETE FROM song \n" +
            "WHERE id NOT IN (:longs)")
    void deleteAllExcept(Long... longs);
}
