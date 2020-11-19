package com.example.musicplayer.model;

public class Artist {
    private Long mID;
    private Long mArtistKey;
    private String mArtist;
    private int mCount;

    public void setID(Long ID) {
        mID = ID;
    }

    public void setArtistKey(Long artistKey) {
        mArtistKey = artistKey;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public Long getID() {
        return mID;
    }

    public Long getArtistKey() {
        return mArtistKey;
    }

    public String getArtist() {
        return mArtist;
    }

    public int getCount() {
        return mCount;
    }
}
