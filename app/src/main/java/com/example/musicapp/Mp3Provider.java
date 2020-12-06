package com.example.musicapp;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class Mp3Provider {
    public ArrayList<File> getListMp3(File file) {
        ArrayList<File> listSongs = new ArrayList<>();
        for (File item : file.listFiles()) {
            if (item.isDirectory()) {
                listSongs.addAll(getListMp3(item));
            } else {
                if (item.getName().toLowerCase().endsWith(".mp3")) {
                    listSongs.add(item);
                }
            }
        }
        return listSongs;
    }
}
