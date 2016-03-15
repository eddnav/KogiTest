package com.nav.kogi.test;

import android.app.Activity;
import android.content.Context;

import com.nav.kogi.test.gallery.PostPresenter;
import com.nav.kogi.test.shared.annotation.Activities;
import com.nav.kogi.test.shared.annotation.ForActivity;
import com.nav.kogi.test.shared.cache.Cache;

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

    @Activities
    // May be scoped to standalone activities as well, so no harm in defining it
    // in both modules with the appropriate qualifier.
    @ForActivity
    @Provides
    public PostPresenter providePostPresenter(Cache cache) {
        return new PostPresenter(cache);
    }

}
