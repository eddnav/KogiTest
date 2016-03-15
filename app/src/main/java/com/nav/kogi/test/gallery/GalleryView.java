package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.presenter.PresenterView;

/**
 * @author Eduardo Naveda
 */
public interface GalleryView extends PresenterView {

    void refresh();

    void setSelectedPost(int position);

}
