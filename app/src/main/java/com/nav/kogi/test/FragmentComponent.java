package com.nav.kogi.test;

import android.content.Context;

import com.nav.kogi.test.gallery.PostFragment;
import com.nav.kogi.test.gallery.PostPresenter;
import com.nav.kogi.test.shared.annotation.ForActivity;
import com.nav.kogi.test.shared.annotation.ForFragment;
import com.nav.kogi.test.shared.annotation.Fragments;

import dagger.Component;

/**
 * @author Eduardo Naveda
 */
@Fragments
@Component(
        dependencies = ActivityComponent.class,
        modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(PostFragment fragment);

    @ForActivity
    Context getContext();

    @ForFragment
    PostPresenter getPostPresenter();

}
