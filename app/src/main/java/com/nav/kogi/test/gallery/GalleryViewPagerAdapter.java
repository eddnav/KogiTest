package com.nav.kogi.test.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author Eduardo Naveda
 */
public class GalleryViewPagerAdapter extends FragmentStatePagerAdapter {

    private GalleryPresenter galleryPresenter;
    private boolean showInfo;
    private String feed;

    public GalleryViewPagerAdapter(FragmentManager fm, GalleryPresenter galleryPresenter, boolean showInfo, String feed) {
        super(fm);
        this.galleryPresenter = galleryPresenter;
        this.showInfo = showInfo;
        this.feed = feed;
    }

    @Override
    public Fragment getItem(int position) {
        return PostFragment.newInstance(showInfo, feed, position);
    }

    @Override
    public int getCount() {
        return galleryPresenter.getPosts().size();
    }

}
