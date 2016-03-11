package com.nav.kogi.test;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nav.kogi.test.shared.annotation.ForApplication;
import com.nav.kogi.test.shared.api.Api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
    public Gson provideGson() {
       Gson gson = new GsonBuilder()
               .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
               .create();

        return gson;
    }

    @Singleton
    @Provides
    public Api provideApi(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(Api.class);
    }

}
