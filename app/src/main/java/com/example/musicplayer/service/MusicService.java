package com.example.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.musicplayer.controller.MediaPlayerTasks;

public class MusicService extends Service {

    public static final int NOTIFY_ID = 1;
    private final IBinder mMusicBind = new MusicBinder();
    private android.media.MediaPlayer mMediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        mMediaPlayer = MediaPlayerTasks.getMediaPlayer();
    }

    public class MusicBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMusicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        return false;
    }

//    @Override
//    public void onPrepared(MediaPlayer mediaPlayer) {
//        mediaPlayer.start();

//        for notification
//        Intent notItent = new Intent(this, MusicListFragment.class);
//        notItent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE_PENDING_INTENT, notItent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//
//        builder.setContentIntent(pendingIntent)
////                .setSmallIcon()
//                .setTicker(mMusicTitle)
//                .setOngoing(true)
//                .setContentTitle("Playing")
//                .setContentText(mMusicTitle);
//        Notification notification = null;
//   //    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            notification = builder.build();
////        }
//
//        startForeground(NOTIFY_ID, notification);
//    }
}
