package com.example.musicapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AlbumDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(AlbumInfo albumInfo);

    @Delete
    void delete(AlbumInfo albumInfo);

    @Update
    void update(AlbumInfo albumInfo);

    @Query("SELECT * FROM AlbumInfo WHERE ID = :id")
    AlbumInfo get(long id);

    @Query("SELECT * FROM AlbumInfo ORDER BY id DESC")
    public List<AlbumInfo> getAll();
}
