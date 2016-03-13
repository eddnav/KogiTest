package com.nav.kogi.test;

import com.nav.kogi.test.shared.api.Api;
import com.nav.kogi.test.shared.cache.Cache;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Eduardo Naveda
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(App app);

    Api api();

    Cache cache();

}
