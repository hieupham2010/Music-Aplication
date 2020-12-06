package com.example.musicapp;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AlbumWithSongs {
    @Embedded public AlbumInfo albumInfo;
    @Relation( parentColumn =  "id" , entityColumn = "idAlbum")
    public List<SongInfo> songInfo;
}
