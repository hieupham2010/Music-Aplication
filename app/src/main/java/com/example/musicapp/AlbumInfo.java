package com.example.musicapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlbumInfo implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String category;

    public AlbumInfo(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    protected AlbumInfo(Parcel in) {
        id = in.readLong();
        title = in.readString();
        category = in.readString();
    }

    public static final Creator<AlbumInfo> CREATOR = new Creator<AlbumInfo>() {
        @Override
        public AlbumInfo createFromParcel(Parcel in) {
            return new AlbumInfo(in);
        }

        @Override
        public AlbumInfo[] newArray(int size) {
            return new AlbumInfo[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(category);
    }
}
