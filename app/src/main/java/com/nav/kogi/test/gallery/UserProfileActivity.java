package com.nav.kogi.test.gallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nav.kogi.test.R;
import com.nav.kogi.test.shared.api.Api;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserProfileActivity extends AppCompatActivity {

    public static final String USERNAME = "username";

    @Bind(R.id.web_view)
    WebView mWebView;

    private String userName;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        userName = getIntent().getExtras().getString(USERNAME);
        if(savedInstanceState != null)
            userName = savedInstanceState.getString(USERNAME);

        //noinspection ConstantConditions
        getSupportActionBar().setTitle(userName);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Api.USER_PROFILE_URL_BASE + userName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(USERNAME, userName);
    }
}
