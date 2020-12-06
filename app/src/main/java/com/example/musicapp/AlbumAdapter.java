package com.example.musicapp;

import android.content.Context;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {
    private Context context;
    private List<AlbumInfo> listItems;
    private int resource;
    private ItemClickListener listener;
    public AlbumAdapter(Context context , List<AlbumInfo> listItems , int resource) {
        this.context = context;
        this.listItems = listItems;
        this.resource = resource;
        if(context instanceof ItemClickListener) {
            this.listener = (ItemClickListener) context;
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource , parent , false);
        return new MyHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        final AlbumInfo albumInfo = listItems.get(position);
        holder.txt_album.setText(albumInfo.getTitle());
        holder.txt_category.setText(albumInfo.getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onItemAlbumClick(position , albumInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listItems != null) {
            return listItems.size();
        }
        return 0;
    }
    void updateListAlbum(List<AlbumInfo> albumInfo) {
        listItems = new ArrayList<>();
        listItems.addAll(albumInfo);
        notifyDataSetChanged();
    }
    public static class MyHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private ImageView img_album;
        private TextView txt_album , txt_category;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            img_album = itemView.findViewById(R.id.img_album);
            txt_album = itemView.findViewById(R.id.txt_album);
            txt_category = itemView.findViewById(R.id.txt_category);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                        ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0 , getAdapterPosition() , 0 , "Update");
            contextMenu.add(1 , getAdapterPosition() , 0 , "Remove");
        }
    }
}
