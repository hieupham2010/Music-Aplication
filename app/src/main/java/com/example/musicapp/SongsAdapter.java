package com.example.musicapp;

import android.content.Context;
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

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyHolder> {
    private Context context;
    private List<SongInfo> items;
    private int resource;
    private ItemClickListener listener;
    public SongsAdapter(Context context , int resource , List<SongInfo> items) {
        this.context = context;
        this.resource = resource;
        this.items = items;
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
        final SongInfo songs = items.get(position);
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

    @Override
    public int getItemCount() {
        return items.size();
    }
    void updateListSongs(ArrayList<SongInfo> listSongs) {
        items = new ArrayList<>();
        items.addAll(listSongs);
        notifyDataSetChanged();
    }
    public static class MyHolder extends RecyclerView.ViewHolder{
        private TextView SongsName,SingerName;
        private ImageView img;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            SongsName = itemView.findViewById(R.id.txtSongName);
            SingerName = itemView.findViewById(R.id.txtSingerName);
            img = itemView.findViewById(R.id.img);
        }
    }
}
