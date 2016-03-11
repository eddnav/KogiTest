package com.nav.kogi.test;

import android.app.Activity;
import android.content.Context;


import com.nav.kogi.test.shared.annotation.Activities;
import com.nav.kogi.test.shared.annotation.ForActivity;

import dagger.Module;
import dagger.Provides;

/**
 * @author Eduardo Naveda
 */
@Activities
@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Activities
    @ForActivity
    @Provides
    public Context provideContext() {
        return activity;
    }

}
