package com.example.musicapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AlbumInfo.class , SongInfo.class} , version = 1)
public abstract class MusicDatabase extends RoomDatabase {

    public static MusicDatabase getInstance(Context context){
        return Room.databaseBuilder(context.getApplicationContext() ,
                MusicDatabase.class , "music_database.db")
                .allowMainThreadQueries().build();
    }

    public abstract AlbumDAO albumDAO();
    public abstract SongDAO songDAO();
}
