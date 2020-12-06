package com.example.musicapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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

public class AlbumActivity extends AppCompatActivity implements ItemClickListener{
    private AlbumInfo albumInfo;
    private String title;
    private SongDAO songDAO;
    private MusicDatabase database;
    private SongInAlbumAdapter adapter;
    private List<SongInfo> songInAlbum;
    private RecyclerView listSongs;
    private long idAlbum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        database = MusicDatabase.getInstance(this);
        songDAO = database.songDAO();
        listSongs = findViewById(R.id.album_songs);
        Intent intent = getIntent();
        title = intent.getStringExtra("name");
        albumInfo = intent.getParcelableExtra("album");
        idAlbum = albumInfo.getId();
        songInAlbum = songDAO.getSongInAlbum(idAlbum);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        passAdapter();
    }


    @Override
    public void onItemClick(int position, SongInfo songs) {
        Mp3Provider mp3Provider = new Mp3Provider();
        final ArrayList<File> songFiles = getSongFileInAlbum(mp3Provider.getListMp3(Environment.getExternalStorageDirectory()));
        int songPosition = getSongPosition(songs);
        Intent intent = new Intent(this , PlayerActivity.class);
        intent.putExtra("songFiles" , songFiles);
        intent.putExtra("songs" , songs.getSongName() + "-" + songs.getSingerName());
        intent.putExtra("position" , songPosition);
        startActivity(intent);
    }

    private ArrayList<File> getSongFileInAlbum(ArrayList<File> songFiles) {
        ArrayList<File> fileContainInAlbum = new ArrayList<>();
        for (int i = 0; i < songFiles.size(); i++) {
            String[] temp = songFiles.get(i).getName().split("-");
            String SongName , SingerName;
            if(temp.length >= 2) {
                SongName = temp[0];
                SingerName = temp[1];
            }else {
                SongName = songFiles.get(i).getName().replace(".mp3" , "");
                SingerName = "";
            }
            for(int j = 0 ; j < songInAlbum.size() ; j++) {
                String songNameInAlbum = songInAlbum.get(j).getSongName() + songInAlbum.get(j).getSingerName();
                if(songNameInAlbum.equals(SongName + SingerName)) {
                    fileContainInAlbum.add(songFiles.get(i));
                }
            }
        }
        return fileContainInAlbum;
    }

    private int getSongPosition(SongInfo songInfo) {
        String nameSongSelected = songInfo.getSongName() + songInfo.getSingerName();
        Mp3Provider mp3Provider = new Mp3Provider();
        final ArrayList<File> songs = mp3Provider.getListMp3(Environment.getExternalStorageDirectory());
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
            if(nameSongSelected.equals(SongName + SingerName)) {
                return i;
            }
        }
        return -1;
    }
    @Override
    public void onItemAlbumClick(int position, AlbumInfo albumInfo) {

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = item.getItemId();
        SongInfo songInfo = songInAlbum.get(position);
        if(item.getGroupId() == 0) {
            deleteSongs(songInfo);
        }
        return super.onContextItemSelected(item);
    }
    private void deleteSongs(final SongInfo songInfo) {
        new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setTitle("Remove Songs")
                .setMessage("Are you sure remove this song from " + albumInfo.getTitle() + "?")
                .setPositiveButton("Remove" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        songDAO.deleteSongInAlbum(songInfo.getId() , idAlbum);
                        songInAlbum = songDAO.getSongInAlbum(idAlbum);
                        passAdapter();
                        Toast.makeText(AlbumActivity.this , "Remove success" , Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel" , null).create().show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        songInAlbum = songDAO.getSongInAlbum(idAlbum);
        passAdapter();
    }

    private void passAdapter() {
        adapter = new SongInAlbumAdapter(this, songInAlbum ,R.layout.list_song_items);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listSongs.setHasFixedSize(true);
        listSongs.setLayoutManager(manager);
        listSongs.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_album , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.create_album) {
            Intent intent = new Intent(this , AddSongsToAlbumActivity.class);
            intent.putExtra("album" , albumInfo);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}