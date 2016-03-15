package com.nav.kogi.test;

import com.nav.kogi.test.gallery.PostPresenter;
import com.nav.kogi.test.shared.annotation.ForFragment;
import com.nav.kogi.test.shared.annotation.Fragments;
import com.nav.kogi.test.shared.cache.Cache;

import dagger.Module;
import dagger.Provides;

/**
 * @author Eduardo Naveda
 */
@Fragments
@Module
public class FragmentModule {

    @Fragments
    @ForFragment
    @Provides
    public PostPresenter providePostPresenter(Cache cache) {
        return new PostPresenter(cache);
    }

}
