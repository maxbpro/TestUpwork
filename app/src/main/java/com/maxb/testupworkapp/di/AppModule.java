package com.maxb.testupworkapp.di;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.projection.MediaProjectionManager;
import android.view.WindowManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.MEDIA_PROJECTION_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by MaxB on 19/08/2017.
 */
@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences("Pref", Context.MODE_PRIVATE);
    }

    @Provides
    WindowManager provideWindowManager(Application application){
        return (WindowManager) application.getSystemService(WINDOW_SERVICE);
    }

    @Provides
    NotificationManager provideNotificationManager(Application application){
        return (NotificationManager) application.getSystemService(NOTIFICATION_SERVICE);
    }



}
