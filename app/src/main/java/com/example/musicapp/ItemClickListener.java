package com.example.musicapp;

public interface ItemClickListener {
    void onItemClick(int position , SongInfo songs);
    void onItemAlbumClick(int position , AlbumInfo albumInfo);
}
