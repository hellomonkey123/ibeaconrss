package com.lyw.tracker.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;
//import android.app.Service;


import com.lyw.tracker.mag.BeepManager;


public class RssiReceiver extends BroadcastReceiver {

    private Vibrator vibrator;

    @Override
    public void onReceive(Context context, Intent intent) {
        BeepManager.playBeepSoundAndVibrate(context);


        //Toast.makeText(context, "发出警报的设备：" + intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
       // vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
       // vibrator.vibrate(300);
    }
}
