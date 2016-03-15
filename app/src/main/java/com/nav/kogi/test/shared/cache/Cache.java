package com.nav.kogi.test.shared.cache;

import com.anupcowkur.reservoir.Reservoir;
import com.nav.kogi.test.shared.api.PostsResponse;
import com.nav.kogi.test.shared.models.Post;

import timber.log.Timber;

/**
 * Simple singleton wrapper over Reservoir, also keeps memory references of disk cached feeds.
 *
 * @author Eduardo Naveda
 */
public class Cache {

    public final static String POPULAR_POSTS_FEED = "popular_posts_feed";

    public PostsResponse popularPostsResponse;

    public void putPopularPostsResponse(PostsResponse postsResponse) {
        try {
            Reservoir.put(POPULAR_POSTS_FEED, postsResponse);
            popularPostsResponse = postsResponse;
        } catch (Exception e) {
            Timber.e(e, "Error saving popular posts to cache");
        }
    }

    public PostsResponse getPopularPostsResponse() {
        try {
            if (popularPostsResponse == null)
                popularPostsResponse = Reservoir.get(POPULAR_POSTS_FEED, PostsResponse.class);
            return popularPostsResponse;
        } catch (Exception e) {
            Timber.w(e, "Error fetching popular posts to cache");
            return null;
        }
    }

    public Post getPopularPostByIndex(int index) {
        PostsResponse postsResponse = getPopularPostsResponse();
        if (postsResponse != null)
            return postsResponse.getPosts().get(index);
        else
            return null;
    }

}
