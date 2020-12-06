package com.example.musicapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;


public class AlbumFragment extends Fragment {
    private List<AlbumInfo> albumInfoList;
    static AlbumAdapter adapter;
    private RecyclerView listAlbum;
    private MusicDatabase database;
    private AlbumDAO albumDAO;
    private SongDAO songDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        database = MusicDatabase.getInstance(getContext());
        albumDAO = database.albumDAO();
        songDAO = database.songDAO();
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        albumInfoList = albumDAO.getAll();
        setHasOptionsMenu(true);
        listAlbum = view.findViewById(R.id.list_album);
        registerForContextMenu(listAlbum);
        passAdapter();
        return view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        albumInfoList = albumDAO.getAll();
        int position = item.getItemId();
        AlbumInfo albumInfo = albumInfoList.get(position);
        if(item.getGroupId() == 0) {
            editAlbum(albumInfo);
        }else if(item.getGroupId() == 1) {
            deleteAlbum(albumInfo);
        }
        return super.onContextItemSelected(item);
    }

    private void deleteAlbum(final AlbumInfo albumInfo) {
     new AlertDialog.Builder(getActivity() , R.style.AlertDialogCustom)
                .setTitle("Remove Album")
                .setMessage("Are you sure remove album " + albumInfo.getTitle() + "?")
                .setPositiveButton("Remove" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        songDAO.deleteSongInAlbum(albumInfo.getId());
                        albumDAO.delete(albumInfo);
                        albumInfoList = albumDAO.getAll();
                        adapter.updateListAlbum(albumInfoList);
                        Toast.makeText(getActivity() , "Remove success" , Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel" , null).create().show();
    }

    public void editAlbum(AlbumInfo albumInfo) {
        Intent intent = new Intent(getContext() , CreateAlbum.class);
        intent.putExtra("album"  , albumInfo);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AlbumInfo albumInfo = null;
        if(resultCode == -1) {
            albumInfo = data.getParcelableExtra("album");
        }
        if(albumInfo == null) {
            Log.e("TAG" , "Error");
            return;
        }
        if(requestCode == 2) {
            albumDAO.update(albumInfo);
            albumInfoList = albumDAO.getAll();
            AlbumFragment.adapter.updateListAlbum(albumInfoList);
            Toast.makeText(getContext() , "Update success" , Toast.LENGTH_SHORT).show();
        }

    }


    public void passAdapter() {
        adapter = new AlbumAdapter(getContext(), albumInfoList , R.layout.list_album_item );
        GridLayoutManager manager = new GridLayoutManager(getContext() , 2);
        listAlbum.setHasFixedSize(true);
        listAlbum.setLayoutManager(manager);
        listAlbum.setAdapter(adapter);
    }

}