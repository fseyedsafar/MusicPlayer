package com.example.musicplayer.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class MusicPrefences {
    public static final String PREF_REPEAT_ONE = "repeatOne";
    public static final String PREF_REPEAT_ALL = "repeatAll";
    public static final String PREF_SHUFFLE = "shuffle";
    public static final String PREF_MUSIC_ID = "musicId";

    private static SharedPreferences getSharedPrefences(Context context){
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static boolean getRepeatOne(Context context) {
        return getSharedPrefences(context).getBoolean(PREF_REPEAT_ONE, false);
    }

    public static void setRepeatOne(Context context, boolean repeatOneFlag) {
        getSharedPrefences(context).edit().putBoolean(PREF_REPEAT_ONE, repeatOneFlag).apply();
    }

    public static boolean getRepeatAll(Context context) {
        return getSharedPrefences(context).getBoolean(PREF_REPEAT_ALL, false);
    }

    public static void setRepeatAll(Context context, boolean repeatAllFlag) {
        getSharedPrefences(context).edit().putBoolean(PREF_REPEAT_ALL, repeatAllFlag).apply();
    }

    public static boolean getShuffle(Context context) {
        return getSharedPrefences(context).getBoolean(PREF_SHUFFLE, false);
    }

    public static void setShuffle(Context context, boolean shuffleFlag) {
        getSharedPrefences(context).edit().putBoolean(PREF_SHUFFLE, shuffleFlag).apply();
    }

    public static Long getMusicId(Context context) {
        return getSharedPrefences(context).getLong(PREF_MUSIC_ID, 0);
    }

    public static void setMusicId(Context context, Long musicId) {
        getSharedPrefences(context).edit().putLong(PREF_MUSIC_ID, musicId).apply();
    }
}
