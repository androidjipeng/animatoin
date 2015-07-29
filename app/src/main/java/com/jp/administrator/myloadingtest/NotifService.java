package com.jp.administrator.myloadingtest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotifService extends Service {
    public NotifService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Download.ShowDialogProgressBar(Download.mContext);
        System.out.println("onStartCommand..................>>>>>");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate..................>>>>>");
    }
}
