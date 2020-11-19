package com.example.musicplayer.model;

public class Music {

    private Long mMusicID;
    private String mTitle;
    private Long mArtistID;
    private String mArtist;
    private Long mAlbumID;
    private String mAlbum;
    private String mImage;
    private int mDuration;

    public Long getArtistID() {
        return mArtistID;
    }

    public void setArtistID(Long artistID) {
        mArtistID = artistID;
    }

    public Long getAlbumID() {
        return mAlbumID;
    }

    public void setAlbumID(Long albumID) {
        mAlbumID = albumID;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public Long getmID() {
        return mMusicID;
    }

    public void setmID(Long mID) {
        this.mMusicID = mID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getmAlbum() {
        return mAlbum;
    }

    public void setmAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }
}
