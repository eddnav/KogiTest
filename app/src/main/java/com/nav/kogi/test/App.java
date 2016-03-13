package com.nav.kogi.test;

import android.app.Application;

import timber.log.Timber;

/**
 * @author Eduardo Naveda
 */
public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        appComponent.inject(this);

        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
