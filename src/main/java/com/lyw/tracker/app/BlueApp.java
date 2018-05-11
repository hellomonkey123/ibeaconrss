package com.lyw.tracker.app;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.Bmob;

/**
 * Created by liuyuanwen on 17/4/24.
 */

public class BlueApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"e472b561deb36a48f336719fe6d5bb90");
        MobclickAgent.setDebugMode( true );
    }
}
