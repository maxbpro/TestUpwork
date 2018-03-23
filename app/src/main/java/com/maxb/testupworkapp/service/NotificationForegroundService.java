package com.maxb.testupworkapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;


import com.maxb.testupworkapp.App;
import com.maxb.testupworkapp.MainActivity;
import com.maxb.testupworkapp.R;
import com.maxb.testupworkapp.remoteView.RecordingState;
import com.maxb.testupworkapp.remoteView.RemoteViewHelper;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;



/**
 * Created by MaxB on 31/07/2017.
 */

public class NotificationForegroundService extends Service {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, NotificationForegroundService.class);
        return intent;
    }


    private Context context = null;
    private RemoteViews remoteViews = null;
    private Notification notification = null;

    private boolean isRemoteViewsStarted = false;


    @Inject
    NotificationManager notificationManager;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    private int counter = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        ((App)getApplication()).applicationComponent().inject(this);
    }


    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {

       if(intent.getAction().equals("stop")) {

            hideNotificationRemoteView(intent);
            stopSelf();

        }else if (intent.getAction().equals("start")){

           // setup the notification first
           if(!isRemoteViewsStarted){
               isRemoteViewsStarted = true;
               setRemoteUpNotification();
               startTimer();
           }
       }


        return START_NOT_STICKY;

    }


    private void setRemoteUpNotification(){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);


        remoteViews = RemoteViewHelper.getRemoteViews(this);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                // Dismiss Notification
                .setAutoCancel(false)
                .setUsesChronometer(true)
                .setContent(remoteViews);

        notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        //notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        //notificationManager.notify(RemoteViewHelper.NOTIFICATION_ID, notification);

        startForeground(RemoteViewHelper.NOTIFICATION_ID, notification);
        updateRemoteUpNotification(RecordingState.NOT_RUNNING);
    }


    private void updateRemoteUpNotification(RecordingState recordingState){

        if(remoteViews != null){
            RemoteViewHelper.updateRemoteViews(remoteViews, recordingState);
            notificationManager.notify(RemoteViewHelper.NOTIFICATION_ID, notification);
        }

    }

    private void updateTimer(int seconds){
        if(remoteViews != null){
            RemoteViewHelper.updateTimer(context, remoteViews, seconds);
            notificationManager.notify(RemoteViewHelper.NOTIFICATION_ID, notification);
        }
    }



    private void hideNotificationRemoteView(Intent intent){
        int notificationId = intent.getIntExtra("notificationId", 0);
        notificationManager.cancel(notificationId);
    }


    @Override
    public void onDestroy() {

        if (mTimer != null) {
            mTimer.cancel();
        }

        super.onDestroy();
    }


    private void startTimer(){

        if (mTimer != null) {
            mTimer.cancel();
        }

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        counter = 0;
        mTimer.schedule(mMyTimerTask, 1000, 1000);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            counter++;
            updateTimer(counter);

            if(counter == 60 * 5){
                notificationManager.cancelAll();
                stopSelf();
            }
        }
    }






}
