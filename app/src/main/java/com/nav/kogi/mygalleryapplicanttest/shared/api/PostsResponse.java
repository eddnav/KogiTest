package com.nav.kogi.mygalleryapplicanttest.shared.api;

import com.nav.kogi.mygalleryapplicanttest.shared.models.Post;

import java.util.List;

/**
 * @author Eduardo Naveda
 */
public class PostsResponse extends Response {

    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

}
