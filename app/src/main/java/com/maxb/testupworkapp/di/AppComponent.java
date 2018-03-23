package com.maxb.testupworkapp.di;


import com.maxb.testupworkapp.MainActivity;
import com.maxb.testupworkapp.service.NotificationForegroundService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by MaxB on 19/08/2017.
 */

@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {

    void inject(MainActivity target);

    void inject(NotificationForegroundService target);
}
