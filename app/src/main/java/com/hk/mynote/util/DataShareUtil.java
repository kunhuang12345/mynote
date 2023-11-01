package com.hk.mynote.util;

import android.media.MediaPlayer;

public class DataShareUtil {
    public static MediaPlayer mediaPlayer;
    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public static void setMediaPlayer(MediaPlayer mediaPlayer) {
        DataShareUtil.mediaPlayer = mediaPlayer;
    }
}
