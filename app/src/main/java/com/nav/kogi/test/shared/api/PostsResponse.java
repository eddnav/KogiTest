package com.nav.kogi.test.shared.api;

import com.google.gson.annotations.SerializedName;
import com.nav.kogi.test.shared.models.Post;

import java.util.List;

/**
 * @author Eduardo Naveda
 */
public class PostsResponse extends Response {

    @SerializedName("data")
    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

}
