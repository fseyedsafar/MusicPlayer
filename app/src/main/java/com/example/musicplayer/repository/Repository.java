package com.example.musicplayer.repository;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;

import com.example.musicplayer.R;
import com.example.musicplayer.model.Album;
import com.example.musicplayer.model.Artist;
import com.example.musicplayer.model.Music;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    public static Repository instance;
    private Context mContext;
    ContentResolver mContentResolver;
    private List<Music> mMusicList = new ArrayList<>();
    private List<Album> mAlbumList = new ArrayList<>();
    private List<Artist> mArtistList = new ArrayList<>();

    public Repository(Context context) {
        this.mContext = context;

        if (mContentResolver == null) {
            mContentResolver = mContext.getContentResolver();
        }
    }

    public static Repository getInstance(Context context) {
        if (instance == null){
            instance = new Repository(context);
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        return instance;
    }

    public Music getShuffleMusic(List<Music> musicList, int musicPosition){
        for (int i = 0 ; i < musicList.size() ; i++){
            if (i == musicPosition){
                return musicList.get(i);
            }
        }
        return null;
    }

    public Music getPrevMusic(Long mID, int position) {

        List<Music> musicList = getTypeList(position);

        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getmID() == mID) {

                if (i == 0) {
                    return musicList.get(musicList.size() - 1);
                } else {
                    return musicList.get(i - 1);
                }
            }
        }
        return null;
    }

    public Music getNextMusic(Long mID, int position) {

        List<Music> musicList = getTypeList(position);

        for (int i = 0; i < musicList.size(); i++) {
            if (musicList.get(i).getmID() == mID) {

                if (i == musicList.size() - 1) {
                    return musicList.get(0);
                } else {
                    return musicList.get(i + 1);
                }
            }
        }
        return null;
    }

    public Music getMusic(Long mID){

        for (Music music : getAllMusicList()) {
            if (music.getmID() == mID)
                return music;
        }
        return null;
    }

    public List getTypeList(int position){
        List list = new ArrayList<>();
        switch (position){
            case 0:{
                return getAllMusicList();
            }
            case 1:{
                return getAlbumList();
            }
            case 2:{
                return getArtistList();
            }
        }
        return list;
    }

    public List<Music> getMusicList(int currentPage, Long fileID){
//        List<Music> musicList = getExternalMusicList();
//        List<Music> albumOrArtist = new ArrayList<>();
//        Music music = getMusic(id);
        switch (currentPage){
            case 0:{
                List list = mMusicList;
                return mMusicList;
            }
            case 1:{
                return getAlbumIDList(fileID);
//                for (Music modelMusic : getExternalMusicList()) {
//                    if (music.getmArtist().equalsIgnoreCase(modelMusic.getmArtist())){
//                        albumOrArtist.add(modelMusic);
//                    }
//                }
//                return albumOrArtist;
            }
            case 2:{
                List list = getArtistIDList(fileID);
                return getArtistIDList(fileID);
//                for (Music modelMusic : getExternalMusicList()) {
//                    if (music.getmAlbum().equalsIgnoreCase(modelMusic.getmAlbum())){
//                        albumOrArtist.add(modelMusic);
//                    }
//                }
//                return albumOrArtist;
            }
        }
        return null;
    }

    public List<Music> getAllMusicList(){
        List<Music> musicList = getExternalMusicList();
//        for (Music music : getInternalMusicList()) {
//            musicList.add(music);
//        }

        return musicList;
    }

    private List<Music> getExternalMusicList(){
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        return getAllMusicList(musicUri, albumUri);
    }

    private List<Music> getInternalMusicList(){
        Uri musicUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
        Uri albumUri = MediaStore.Audio.Albums.INTERNAL_CONTENT_URI;
        return getAllMusicList(musicUri, albumUri);
    }

    private List<Music> getAllMusicList(Uri musicUri, Uri albumUri) {

//        MusicPlayerAppliation.runTimePermisson(mContext);

        if (mMusicList.size() == 0) {
            Cursor musicCursor = mContentResolver.query(musicUri, null, null, null, null);
            Cursor albumCursor;

                try {

                    if (musicCursor != null) {

                        musicCursor.moveToFirst();

                            while (!musicCursor.isAfterLast()) {

                                Long musicID = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                                String title = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                                Long artistID = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));
                                String artist = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                                Long albumID = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                                String album = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                                int duration = musicCursor.getInt(musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

//                                for give album art
                                albumCursor = mContentResolver.query(albumUri, new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                                        MediaStore.Audio.Albums._ID + "=" + albumID, null, null);

                                albumCursor.moveToFirst();
                                String image = null;
                                try {
                                   image = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                                }catch (Exception e){

                                }

                                Music music = new Music();
                                music.setmID(musicID);
                                music.setmTitle(title);
                                music.setArtistID(artistID);
                                music.setmArtist(artist);
                                music.setAlbumID(albumID);
                                music.setmAlbum(album);
                                music.setmImage(image);
                                music.setmDuration(duration);

                                mMusicList.add(music);

                                musicCursor.moveToNext();

                                albumCursor.close();
                            }
                        }
                } finally {
                    musicCursor.close();
                }
        }
        return mMusicList;
    }

//    private List<Music> getFileList(int position){
//        List<Music> temp = getAllMusicList();
//        List<Music> withoutDuplicate = new ArrayList<>();
//        boolean flag = true;
//
//        for (int i = 0 ; i < temp.size() ; i++) {
//
//            if (position == 1) {
//                for (Music music : withoutDuplicate) {
//                    if (music.getArtistID().equals(temp.get(i).getArtistID())) {
//                        flag = false;
//                    }
//                }
//            }else if (position == 2) {
//                for (Music music : withoutDuplicate) {
//                    if (music.getAlbumID().equals(temp.get(i).getAlbumID())) {
//                        flag = false;
//                    }
//                }
//            }
//
//            if (flag == true)
//                withoutDuplicate.add(temp.get(i));
//        }
//        return withoutDuplicate;
//    }

//    public List<Integer> getCount(int position) {
//        List<Music> temp = getFileList(position);
//        List<Integer> countList = new ArrayList<>();
//        int count = 0;
//
//        for (int i = 0 ; i < temp.size() ; i++){
//            for (int j = 0 ; j < temp.size() ; j++){
//                if (position == 1) {
//                    if (temp.get(i).getmArtist().equals(temp.get(j).getmArtist())) {
//                        count++;
//                    }
//                }else if (position == 2) {
//                    if (temp.get(i).getmAlbum().equals(temp.get(j).getmAlbum())) {
//                        count++;
//                    }
//                }
//            }
//            countList.add(count);
//            count = 0;
//        }
//        return countList;
//    }

    private List<Album> getAlbumList() {

        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        if (mAlbumList.size() == 0) {
            Cursor albumCursor = mContentResolver.query(albumUri, null, null, null, null);

            try {

                if (albumCursor != null) {

                    albumCursor.moveToFirst();

                    while (!albumCursor.isAfterLast()) {

                        Long ID = albumCursor.getLong(albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
//                        Long albumID = albumCursor.getLong(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ID));
                        String album = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                        String artist = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
//                        int count = albumCursor.getInt(albumCursor.getColumnIndex(MediaStore.Audio.Albums._COUNT));
                        String image = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                        Album albumObject = new Album();
                        albumObject.setID(ID);
//                        albumObject.setAlbumID(albumID);
                        albumObject.setAlbum(album);
                        albumObject.setArtist(artist);
//                        albumObject.setCount(count);
                        albumObject.setImage(image);

                        mAlbumList.add(albumObject);

                        albumCursor.moveToNext();
                    }
                }
            } finally {
                albumCursor.close();
            }
        }
        return mAlbumList;
    }

    private List<Artist> getArtistList() {

        Uri artistUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        if (mArtistList.size() == 0) {
            Cursor artistCursor = mContentResolver.query(artistUri, null, null, null, null);

            try {

                if (artistCursor != null) {

                    artistCursor.moveToFirst();

                    while (!artistCursor.isAfterLast()) {

                        Long ID = artistCursor.getLong(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                        Long artistKey = artistCursor.getLong(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY));
                        String artist = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
//                        int count = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists._COUNT));

                        Artist artistObject = new Artist();
                        artistObject.setID(ID);
                        artistObject.setArtistKey(artistKey);
                        artistObject.setArtist(artist);
//                        artistObject.setCount(count);

                        mArtistList.add(artistObject);

                        artistCursor.moveToNext();
                    }
                }
            } finally {
                artistCursor.close();
            }
        }
        return mArtistList;
    }

    public List<Music> getAlbumIDList(Long id){
        List<Music> list = new ArrayList<>();
        for (Music music : mMusicList) {
            if (music.getAlbumID() == id){
                list.add(music);
            }
        }
        return list;
    }

    public List<Music> getArtistIDList(Long id){
        List<Music> list = new ArrayList<>();
        for (Music music : mMusicList) {
            if (music.getArtistID() == id){
                list.add(music);
            }
        }
        return list;
    }
}
