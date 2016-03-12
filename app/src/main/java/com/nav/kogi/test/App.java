package com.nav.kogi.test;

import android.app.Application;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.Gson;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * @author Eduardo Naveda
 */
public class App extends Application {

    private AppComponent appComponent;

    @Inject
    Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        appComponent.inject(this);

        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());

        try {
            Reservoir.init(this, 128000, gson);
        } catch (Exception e) {
            Timber.e(e, "Exception thrown while initializing Reservoir");
        }

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
