package com.almog;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.almog.db.DataMgr;
import com.parse.ParseUser;

/**
 * Created by almog on 3/20/16.
 */
public class NotifyService extends Service {

    private String serviceName = NotifyService.class.getSimpleName();
    private ParseUser user;
    private int interval;
    private DataMgr dataMgr;

    @Override
    public void onCreate() {
        super.onCreate();
        user=ParseUser.getCurrentUser();
        interval=50;
        dataMgr=new DataMgr(this,user,false);
        Thread t = new Thread(new Runnable(){
            public void run() {
                while (true) {
                    //update data
                    if(dataMgr.checkUpdate())
                    {
                        sendNotification();
                    }
                    try {
                        Thread.sleep(1000 * interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return NotifyService.START_STICKY;
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

    public void setInterval(int interval) {
        this.interval = interval;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotification(){
        int icon= R.mipmap.ic_launcher;
        String ticket="You have new task !";
        long when=System.currentTimeMillis();
        Intent i=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, i, 0);
        Notification noti = new Notification.Builder(this)
                .setContentTitle("To-Do")
                .setContentText(ticket)
                .setSmallIcon(icon)
                .setWhen(when)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, noti);
    }
}


/*
        return super.onStartCommand(intent, flags, startId);
*/