package com.example.musicapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment  {
    private RecyclerView listSongs;
    private List<SongInfo> items;
    static SongsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        listSongs = view.findViewById(R.id.listSongs);
        getSongs();
        return view;
    }

    private void passAdapter() {
        adapter = new SongsAdapter(getContext(), R.layout.list_song_items, items);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        listSongs.setHasFixedSize(true);
        listSongs.setLayoutManager(manager);
        listSongs.setAdapter(adapter);
    }

    private void getSongs() {
        Mp3Provider mp3Provider = new Mp3Provider();
        final ArrayList<File> songs = mp3Provider.getListMp3(Environment.getExternalStorageDirectory());
        items = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            String[] temp = songs.get(i).getName().split("-");
            String SongName , SingerName;
            if(temp.length >= 2) {
                SongName = temp[0];
                SingerName = temp[1];
            }else {
                SongName = songs.get(i).getName().replace(".mp3" , "");
                SingerName = "";
            }
            items.add(new SongInfo(SongName, SingerName));
        }
        passAdapter();
    }



}