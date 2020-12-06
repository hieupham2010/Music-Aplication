package com.example.musicapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SongDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(SongInfo songInfo);

    @Delete
    void delete(SongInfo songInfo);

    @Update
    void update(SongInfo songInfo);
    @Query("DELETE FROM SongInfo WHERE ID = :id AND IDALBUM = :idAlbum")
    void deleteSongInAlbum(long id , long idAlbum);

    @Query("DELETE FROM SongInfo WHERE IDALBUM = :idAlbum")
    void deleteSongInAlbum(long idAlbum);

    @Query("SELECT * FROM SongInfo WHERE ID = :id")
    SongInfo get(long id);

    @Query("SELECT * FROM SongInfo WHERE IDALBUM = :id")
    public List<SongInfo> getSongInAlbum(long id);

    @Query("SELECT * FROM SongInfo ORDER BY id DESC")
    public List<SongInfo> getAll();
}
