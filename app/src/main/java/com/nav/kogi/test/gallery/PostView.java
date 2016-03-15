package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.models.Post;

/**
 * @author Eduardo Naveda
 */
public interface PostView {

    void show(Post post);

    void toDetail(Post post, int index);

    void toUserProfile(String username);

    enum Navigation {
        DETAIL, USER_PROFILE
    }

}
