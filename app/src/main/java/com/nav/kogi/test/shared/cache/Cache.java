package com.nav.kogi.test.shared.cache;

import com.anupcowkur.reservoir.Reservoir;
import com.nav.kogi.test.shared.api.PostsResponse;

import timber.log.Timber;

/**
 * Simple wrapper over Reservoir.
 *
 * @author Eduardo Naveda
 */
public class Cache {

    public static String POPULAR_POSTS_FEED = "popular_posts_feed";

    public void putPopularPostsResponse(PostsResponse posts) {
        try {
            Reservoir.put(POPULAR_POSTS_FEED, posts);
        } catch (Exception e) {
            Timber.e(e, "Error saving popular posts to cache");
        }
    }

    public PostsResponse getPopularPostsResponse() {
        try {
            return Reservoir.get(POPULAR_POSTS_FEED, PostsResponse.class);
        } catch (Exception e) {
            Timber.w(e, "Error fetching popular posts to cache");
            return null;
        }
    }

}
