package com.nav.kogi.test;

import android.content.Context;

import com.nav.kogi.test.gallery.MainActivity;
import com.nav.kogi.test.shared.annotation.Activities;
import com.nav.kogi.test.shared.annotation.ForActivity;

import dagger.Component;

/**
 * @author Eduardo Naveda
 */
@Activities
@Component(
        dependencies = AppComponent.class,
        modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    @ForActivity
    Context getContext();

}
