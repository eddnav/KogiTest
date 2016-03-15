package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.api.ApiError;

/**
 * @author Eduardo Naveda
 */
public interface GalleryView {

    void refresh();

    void setSelectedPost(int position);

    void showError(ApiError error);

}
