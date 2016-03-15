package com.nav.kogi.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Eduardo Naveda
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public AppComponent getAppComponent() {
        return ((App) getApplication()).getAppComponent();
    }

    public ActivityComponent getActivityComponent() {
        return DaggerActivityComponent.builder()
                .appComponent(getAppComponent())
                .activityModule(new ActivityModule(this))
                .build();
    }

}
