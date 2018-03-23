package com.maxb.testupworkapp.remoteView;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;


import com.maxb.testupworkapp.R;
import com.maxb.testupworkapp.service.NotificationForegroundService;

import java.text.DecimalFormat;
import java.util.Formatter;

/**
 * Created by MaxB on 19/08/2017.
 */

public class RemoteViewHelper {

    public static final int NOTIFICATION_ID = 1999;
    private static final DecimalFormat format = new DecimalFormat("#.##");

    public static RemoteViews getRemoteViews(Context context) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remote_view_layout);
        return remoteViews;
    }

    public static void updateRemoteViews(RemoteViews remoteViews, RecordingState recordingState){

        switch (recordingState){
            case NOT_RUNNING:
                remoteViews.setViewVisibility(R.id.txtTimer, View.VISIBLE);
                break;
            case RUNNING:
                remoteViews.setViewVisibility(R.id.txtTimer, View.VISIBLE);
                break;
        }
    }

    public static void updateTimer(Context context, RemoteViews remoteViews, int seconds){
        remoteViews.setTextViewText(R.id.txtTimer, stringForTime(seconds * 1000));
    }

    private static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, NotificationForegroundService.class);
        intent.setAction(action);
        return PendingIntent.getService(context, 0, intent, 0);
    }

    private static PendingIntent getClosingPendingIntent(Context context) {

        Intent intent = new Intent(context, NotificationForegroundService.class);
        intent.putExtra("notificationId", NOTIFICATION_ID);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }

    private static String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        Formatter mFormatter = new Formatter();
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
