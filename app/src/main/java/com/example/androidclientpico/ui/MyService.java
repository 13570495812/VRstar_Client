package com.example.androidclientpico.ui;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        //创建通知实例
        Notification notification=new Notification();
        //notification.contentView=new RemoteViews(getPackageName(),R.layout.notice_bar);
        notification.contentIntent= PendingIntent.getActivity(this,0,new Intent(this,MainActivity.class),0);
        startForeground(1,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
