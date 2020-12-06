package com.example.musicapp;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SongInAlbumAdapter extends RecyclerView.Adapter<SongInAlbumAdapter.MyHolder> {

    private Context context;
    private List<SongInfo> listSong;
    private int resource;
    private ItemClickListener listener;
    public SongInAlbumAdapter(Context context , List<SongInfo> listSong , int resource) {
        this.context = context;
        this.listSong = listSong;
        this.resource = resource;
        if(context instanceof ItemClickListener) {
            this.listener = (ItemClickListener) context;
        }
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource , parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        final SongInfo songs = listSong.get(position);
        holder.SingerName.setText(songs.getSingerName());
        holder.SongsName.setText(songs.getSongName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onItemClick(position,songs);
                }
            }
        });
    }
    void updateListSongs(ArrayList<SongInfo> songs) {
        listSong = new ArrayList<>();
        listSong.addAll(songs);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(listSong != null) {
            return listSong.size();
        }
        return 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView SongsName,SingerName;
        private ImageView img;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            SongsName = itemView.findViewById(R.id.txtSongName);
            SingerName = itemView.findViewById(R.id.txtSingerName);
            img = itemView.findViewById(R.id.img);
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0 , getAdapterPosition() , 0 , "Delete");
        }
    }
}
