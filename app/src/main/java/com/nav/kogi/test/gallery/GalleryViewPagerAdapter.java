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
    private PostView.Navigation navigation;

    public GalleryViewPagerAdapter(FragmentManager fm, GalleryPresenter galleryPresenter, boolean showInfo, PostView.Navigation navigation) {
        super(fm);
        this.galleryPresenter = galleryPresenter;
        this.showInfo = showInfo;
        this.navigation = navigation;
    }

    @Override
    public Fragment getItem(int position) {
        return PostFragment.newInstance(showInfo, position, navigation);
    }

    @Override
    public int getCount() {
        return galleryPresenter.getPosts().size();
    }

}
