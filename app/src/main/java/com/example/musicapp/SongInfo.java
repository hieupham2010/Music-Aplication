package com.example.musicapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import androidx.room.PrimaryKey;

@Entity
public class SongInfo implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long idAlbum;
    private String SongName;
    private String SingerName;

    public long getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(long idAlbum) {
        this.idAlbum = idAlbum;
    }

    public SongInfo(String SongName , String SingerName) {
        this.SingerName = SingerName;
        this.SongName = SongName;
    }


    protected SongInfo(Parcel in) {
        id = in.readLong();
        idAlbum = in.readLong();
        SongName = in.readString();
        SingerName = in.readString();
    }

    public static final Creator<SongInfo> CREATOR = new Creator<SongInfo>() {
        @Override
        public SongInfo createFromParcel(Parcel in) {
            return new SongInfo(in);
        }

        @Override
        public SongInfo[] newArray(int size) {
            return new SongInfo[size];
        }
    };

    public String getSongName() {
        return SongName;
    }

    public String getSingerName() {
        return SingerName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public void setSingerName(String singerName) {
        SingerName = singerName;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(idAlbum);
        parcel.writeString(SongName);
        parcel.writeString(SingerName);
    }
}
