package com.example.musicapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener, SearchView.OnQueryTextListener {
    private MusicDatabase database;
    private AlbumDAO albumDAO;
    private List<AlbumInfo> album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        database = MusicDatabase.getInstance(this);
        albumDAO = database.albumDAO();
        album = albumDAO.getAll();
    }

    private void requestPermission() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        initViewPager();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest
                            , PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SongsFragment() , "Play list");
        viewPagerAdapter.addFragment(new AlbumFragment() , "Album");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onItemClick(int position, SongInfo songs) {
        Mp3Provider mp3Provider = new Mp3Provider();
        final ArrayList<File> songFiles = mp3Provider.getListMp3(Environment.getExternalStorageDirectory());
        Intent intent = new Intent(this , PlayerActivity.class);
        intent.putExtra("songFiles" , songFiles);
        intent.putExtra("songs" , songs.getSongName() + "-" + songs.getSingerName());
        intent.putExtra("position" , position);
        startActivity(intent);
    }

    @Override
    public void onItemAlbumClick(int position, AlbumInfo albumInfo) {
        album = albumDAO.getAll();
        Intent intent = new Intent(this , AlbumActivity.class);
        intent.putExtra("name" , album.get(position).getTitle());
        intent.putExtra("album" , album.get(position));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AlbumInfo albumInfo = null;
        if(resultCode == RESULT_OK) {
            albumInfo = data.getParcelableExtra("album");
        }
        if(albumInfo == null) {
            Log.e("TAG" , "Error");
            return;
        }
        if(requestCode == 1) {
            albumDAO.add(albumInfo);
            album = albumDAO.getAll();
            AlbumFragment.adapter.updateListAlbum(album);
            Toast.makeText(this , "Album "+albumInfo.getTitle()+" created" , Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.create_album) {
            createAlbumList(null);
        }
        return super.onOptionsItemSelected(item);
    }

    public void createAlbumList(AlbumInfo albumInfo) {
        Intent intent = new Intent(this , CreateAlbum.class);
        intent.putExtra("album"  , albumInfo);
        startActivityForResult(intent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search , menu);
        getMenuInflater().inflate(R.menu.create_album , menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
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
        SongsFragment.adapter.updateListSongs(listSongSearched);
        return false;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private List<String> titles;
        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }
        void addFragment(Fragment fragment , String title) {
            fragments.add(fragment);
            titles.add(title);
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}