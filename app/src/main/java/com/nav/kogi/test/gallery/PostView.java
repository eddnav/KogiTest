package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.models.Post;
import com.nav.kogi.test.shared.presenter.PresenterView;

/**
 * @author Eduardo Naveda
 */
public interface PostView extends PresenterView {

    void show(Post post);

    void toDetail(Post post, int index);

    void toUserProfile(String username);

    enum Navigation {
        DETAIL, USER_PROFILE
    }

}
