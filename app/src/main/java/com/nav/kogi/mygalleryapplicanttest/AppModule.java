package com.nav.kogi.mygalleryapplicanttest;

import android.content.Context;

import com.nav.kogi.mygalleryapplicanttest.shared.annotation.ForApplication;
import com.nav.kogi.mygalleryapplicanttest.shared.api.Api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author Eduardo Naveda
 */
@Singleton
@Module
public class AppModule {

    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Singleton
    @ForApplication
    @Provides
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Singleton
    @Provides
    public Api provideApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .build();

        return retrofit.create(Api.class);
    }

}
