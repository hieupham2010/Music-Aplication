package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddSongsToAlbumActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private RecyclerView listSongsAdd;
    private SongsAddAdapter adapter;
    private List<SongInfo> items;
    private MusicDatabase database;
    private SongDAO songDAO;
    private AlbumInfo albumInfo;
    private long idAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_songs_to_album);
        database = MusicDatabase.getInstance(this);
        songDAO = database.songDAO();
        Intent intent = getIntent();
        albumInfo = intent.getParcelableExtra("album");
        idAlbum = albumInfo.getId();
        getSupportActionBar().setTitle("All Songs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listSongsAdd = findViewById(R.id.list_song_add);
        registerForContextMenu(listSongsAdd);
        getSongs();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search , menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getItemId();
        SongInfo songInfo = items.get(position);
        songInfo.setIdAlbum(idAlbum);
        if(item.getGroupId() == 0) {
            addSong(songInfo);
        }
        return super.onContextItemSelected(item);
    }

    private void addSong(SongInfo songInfo) {
        List<SongInfo> currentSongs;
        currentSongs = songDAO.getSongInAlbum(idAlbum);
        int i;
        for(i = 0 ; i < currentSongs.size() ; i++) {
            String songNameAdding = songInfo.getSongName() + songInfo.getSingerName();
            String songNameAdded = currentSongs.get(i).getSongName() + currentSongs.get(i).getSingerName();
            if(songNameAdding.equals(songNameAdded)) {
                Toast.makeText(this , "This song already in the album" , Toast.LENGTH_SHORT).show();
                break;
            }
        }
        if(i == currentSongs.size()) {
            songDAO.add(songInfo);
            Toast.makeText(this , "Add success" , Toast.LENGTH_SHORT).show();
        }
    }

    private void passAdapter() {
        adapter = new SongsAddAdapter(this, R.layout.list_song_items, items);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listSongsAdd.setHasFixedSize(true);
        listSongsAdd.setLayoutManager(manager);
        listSongsAdd.setAdapter(adapter);
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

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String input = s.toLowerCase();
        Mp3Provider mp3Provider = new Mp3Provider();
        final ArrayList<File> songs = mp3Provider.getListMp3(Environment.getExternalStorageDirectory());
        ArrayList<SongInfo> listSongSearched = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            String[] temp = songs.get(i).getName().split("-");
            String SongName, SingerName;
            if (temp.length >= 2) {
                SongName = temp[0];
                SingerName = temp[1];
            } else {
                SongName = songs.get(i).getName().replace(".mp3", "");
                SingerName = "";
            }
            if(SongName.toLowerCase().contains(input) || SingerName.toLowerCase().contains(input)) {
                listSongSearched.add(new SongInfo(SongName , SingerName));
            }
        }
        adapter.updateListSongs(listSongSearched);
        return false;
    }

}