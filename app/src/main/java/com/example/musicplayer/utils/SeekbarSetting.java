package com.example.musicplayer.utils;

public class SeekbarSetting {

    public static String milli_to_time(long millisecond) {

        String hours = String.valueOf((int) (millisecond / (60 * 60 * 1000)));
        String minutes = String.valueOf(((int) (millisecond % (60 * 60 * 1000) / (60 * 1000))));
        String seconds = String.valueOf(((int) ((millisecond % (60 * 60 * 1000)) % (60 * 1000) / 1000)));


        if (Integer.parseInt(hours) < 10)
            hours = "0" + hours;


        if (Integer.parseInt(minutes) < 10)
            minutes = "0" + minutes;

        if (Integer.parseInt(seconds) < 10)
            seconds = "0" + seconds;


        if (Integer.parseInt(hours) > 0)
            return hours + ":" + minutes + ":" + seconds;
        else
            return minutes + ":" + seconds;

    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        return percentage.intValue();

    }

    public static int progressToTime(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        return currentDuration * 1000;
    }
}
