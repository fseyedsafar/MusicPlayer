package com.example.musicplayer.controller;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.widget.ImageButton;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.musicplayer.R;
import com.example.musicplayer.controller.activity.MusicPagerActivity;
import com.example.musicplayer.model.Music;
import com.example.musicplayer.prefs.MusicPrefences;
import com.example.musicplayer.repository.Repository;
import com.example.musicplayer.service.MusicService;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MediaPlayerTasks implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    public static final int REQUEST_CODE_PENDING_INTENT = 0;
    public static final int NOTIFICATION_ID = 0;
    private static MediaPlayerTasks mTestMediaPlayer;
    private static android.media.MediaPlayer mMediaPlayer;
    public static MusicService mMusicService;
    private Repository mRepository;
    private boolean mMusicBound = false, mShuffleFlag;
//            , mRepeatAllFlag, mRepeatOneFlag;
    private Context mContext;
    private List<Music> mMusicList;
    private int mMusicPosition, mCurrentPage;
    private Long mID;
    private PrevNextInterface mPrevNextInterface;
    private Music mMusic;

    public MediaPlayerTasks(Context context) {
        mContext = context.getApplicationContext();
        mRepository = Repository.getInstance(mContext);
        initMusicPlayer();
    }

    public static MediaPlayerTasks getTestMediaPlayer(Context context) {
        if (mTestMediaPlayer == null){
            mTestMediaPlayer = new MediaPlayerTasks(context);
        }
        return mTestMediaPlayer;
    }

    public static android.media.MediaPlayer getMediaPlayer() {
        if (mMediaPlayer == null){
            mMediaPlayer = new android.media.MediaPlayer();
        }
        return mMediaPlayer;
    }

//    public void setRepeatAllFlag(boolean repeatAllFlag) {
//        mRepeatAllFlag = repeatAllFlag;
//    }

//    public void setRepeatOneFlag(boolean repeatOneFlag) {
//        mRepeatOneFlag = repeatOneFlag;
//    }

    public void setPrevNextInterface(PrevNextInterface prevNextInterface) {
        mPrevNextInterface = prevNextInterface;
    }

    public void setMusic(Music music) {
        mMusic = music;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public void setID(Long ID) {
        mID = ID;
    }

    public void setMusicPosition(int musicPosition) {
        mMusicPosition = musicPosition;
    }

    public void setMusicList(List<Music> musicList) {
        mMusicList = musicList;
    }

    public void setShuffleFlag(boolean shuffleFlag) {
        mShuffleFlag = shuffleFlag;
    }

    public void initMusicPlayer(){
        mMediaPlayer = getMediaPlayer();
        mMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(android.media.MediaPlayer mediaPlayer) {
        if (MusicPrefences.getRepeatOne(mContext)){
            playMusic();
        }else if(MusicPrefences.getRepeatAll(mContext)){
//            nextMusic(mID, mCurrentPage);
        }
    }

    @Override
    public void onPrepared(android.media.MediaPlayer mediaPlayer) {
        mMediaPlayer = mediaPlayer;
        mMediaPlayer.start();
//        createAndSendNotification(mID, mCurrentPage);
    }

    public int getCurrentPosition(){
        if (mMusicService != null && mMusicBound == true && mTestMediaPlayer.isPlaying() == true){
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    public int getDuration(){
        if (mMusicService != null && mMusicBound == true && mTestMediaPlayer.isPlaying() == true){
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public boolean isPlaying(){
        if (mMusicService != null && mMusicBound == true){
            return mMediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public void playMusic(){
        mMediaPlayer.reset();
        Music music = mMusicList.get(mMusicPosition);
        Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, music.getmID());
        try {
            mMediaPlayer.setDataSource(mContext, trackUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();
//        createAndSendNotification(mID, mCurrentPage);
    }

    public void prevMusic(Long id, int currentPage){

        setID(id);
        setCurrentPage(currentPage);

        List<Music> musicList = mRepository.getTypeList(mCurrentPage);

        Random random = new Random();
        if (mShuffleFlag){
            int musicPosition = random.nextInt(musicList.size());
            setMusicPosition(musicPosition);
            playMusic();
            mPrevNextInterface.setMusicForPrevNext(mRepository.getShuffleMusic(musicList, musicPosition));
        }else {

            setMusicPosition(--mMusicPosition);
            if (mMusicPosition < 0) {
                setMusicPosition(mMusicList.size() - 1);
            }
            playMusic();
            mPrevNextInterface.setMusicForPrevNext(mRepository.getPrevMusic(mID, mCurrentPage));
        }
        mPrevNextInterface.uiForPrevNext();
    }

    public void nextMusic(Long id, int currentPage){

        setID(id);
        setCurrentPage(currentPage);

        List<Music> musicList = mRepository.getTypeList(mCurrentPage);

        Random random = new Random();
        if (mShuffleFlag) {
            int musicPosition = random.nextInt(musicList.size());
            setMusicPosition(musicPosition);
            playMusic();
            mPrevNextInterface.setMusicForPrevNext(mRepository.getShuffleMusic(musicList, musicPosition));
        } else {
            setMusicPosition(++mMusicPosition);
            if (mMusicPosition >= mMusicList.size()) {
                setMusicPosition(0);
            }
            playMusic();
            mPrevNextInterface.setMusicForPrevNext(mRepository.getNextMusic(mID, mCurrentPage));
        }
        mPrevNextInterface.uiForPrevNext();
    }

    public void playPause(ImageButton button){
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();

            button.setBackgroundResource(R.drawable.ic_play_activity);

        }else {
            mMediaPlayer.start();

            button.setBackgroundResource(R.drawable.ic_pause_activity);
        }
    }


    public void setUIPlayPause(ImageButton button){
        if (mMediaPlayer.isPlaying()) {
            button.setBackgroundResource(R.drawable.ic_pause_activity);

        }else {
            button.setBackgroundResource(R.drawable.ic_play_activity);
        }
    }

    public interface PrevNextInterface{
        void uiForPrevNext();
        void setMusicForPrevNext(Music music);
    }

//    disconnect service
    public ServiceConnection musicConnetion = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
            mMusicService = binder.getService();
            setMusicList(mRepository.getAllMusicList());

            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mMusicBound = false;
        }
    };

//    public static void createAndSendNotification(Long id, int currentPage){
//        Intent intent = MusicPagerActivity.newIntent(mMusicService);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mMusicService, REQUEST_CODE_PENDING_INTENT, intent, 0);
//
//        String channelID = mMusicService.getString(R.string.channel_id);
//        Notification notification = new NotificationCompat.Builder(mMusicService, channelID)
//                .setContentTitle("Music Player")
//                .setContentText("hello")
//                .setSmallIcon(R.id.pause_play)
//                .setContentIntent(pendingIntent)
//                .build();
//
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mMusicService);
//        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
//    }
}
