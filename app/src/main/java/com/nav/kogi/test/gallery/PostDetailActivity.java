package com.nav.kogi.test.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.nav.kogi.test.BaseActivity;
import com.nav.kogi.test.R;
import com.nav.kogi.test.shared.models.Caption;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PostDetailActivity extends BaseActivity implements GalleryView {

    public static final String SELECTED_INDEX = "selected_index";

    @Inject
    GalleryPresenter galleryPresenter;

    @Bind(R.id.viewerPager)
    ViewPager mViewerPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        galleryPresenter.takeView(this);

        int index = 0;
        if (savedInstanceState == null)
            index = getIntent().getExtras().getInt(PostFragment.INDEX);
        else
            index = savedInstanceState.getInt(SELECTED_INDEX);

        mViewerPager.setAdapter(new GalleryViewPagerAdapter(getSupportFragmentManager(),
                galleryPresenter, true, PostView.Navigation.USER_PROFILE));
        mViewerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                galleryPresenter.selectPost(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        galleryPresenter.loadCachedPopularPosts();
        galleryPresenter.selectPost(index);
        mViewerPager.setCurrentItem(index, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_INDEX, galleryPresenter.getSelectedIndex());
    }

    @Override
    public void refresh() {
        mViewerPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setSelectedPost(int position) {
        updateCaption(position);
    }

    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        mIntent.putExtra(SELECTED_INDEX, galleryPresenter.getSelectedIndex());
        setResult(RESULT_OK, mIntent);
        super.onBackPressed();
    }

    @SuppressWarnings("ConstantConditions")
    public void updateCaption(int position) {
        Caption caption = galleryPresenter.getPosts().get(position).getCaption();
        if (caption != null)
            getSupportActionBar().setTitle(caption.getText());
        else
            getSupportActionBar().setTitle(getString(R.string.app_name));
    }

}
