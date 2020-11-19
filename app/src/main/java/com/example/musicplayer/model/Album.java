package com.example.musicplayer.model;

public class Album {

    private Long mID;
    private Long mAlbumID;
    private String mAlbum;
    private String mArtist;
    private String mImage;
    private int mCount;

    public Long getID() {
        return mID;
    }

    public Long getAlbumID() {
        return mAlbumID;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getImage() {
        return mImage;
    }

    public int getCount() {
        return mCount;
    }

    public void setID(Long ID) {
        mID = ID;
    }

    public void setAlbumID(Long albumID) {
        mAlbumID = albumID;
    }

    public void setAlbum(String album) {
        mAlbum = album;
    }

    public void setArtist(String artist) {
        mArtist = artist;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public void setCount(int count) {
        mCount = count;
    }
}
