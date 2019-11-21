package com.example.smartcalendar;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.Settings;

public class AudioPlay {

    public static MediaPlayer mediaPlayer ;
    private static SoundPool soundPool;
    public static boolean isplayingAudio=false;
    public static void playAudio(Context c){
        mediaPlayer = MediaPlayer.create(c, Settings.System.DEFAULT_RINGTONE_URI);
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        if(!mediaPlayer.isPlaying())
        {
            isplayingAudio=true;
            mediaPlayer.start();
        }
    }
    public static void stopAudio(){
        isplayingAudio=false;
        mediaPlayer.stop();
    }

}