package com.lyw.tracker.mag;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.lyw.tracker.R;
import com.lyw.tracker.comm.SP;

import java.io.IOException;

public class BeepManager {
    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;

    private static MediaPlayer mediaPlayer;
    private static AssetFileDescriptor file;
    private static Vibrator vibrator;

    public static void playBeepSoundAndVibrate(Context context) {
        init(context);
        if ((Boolean) SP.get(context,"SY",true)) {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        }
        if ((Boolean) SP.get(context,"ZD",true)) {
            vibrator.vibrate(VIBRATE_DURATION);
        }

    }

    public static void init(Context context) {

        vibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


            @Override
            public void onCompletion(MediaPlayer mp) {
//    TODO Auto-generated method stub

                mp.release();
                mediaPlayer = null;

            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {


            @Override
            public boolean onError(MediaPlayer mp, int arg1, int arg2) {
 //   TODO Auto-generated method stub
                mp.release();
                mediaPlayer = null;

                return true;
            }
        });

//
        file = context.getResources().openRawResourceFd(
                R.raw.losingwarming);
        try {
            mediaPlayer.setDataSource(file.getFileDescriptor(),
                    file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();

        } catch (IOException ioe) {

            mediaPlayer = null;
        }

    }


}