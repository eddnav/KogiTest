package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.annotation.Activities;
import com.nav.kogi.test.shared.api.Api;
import com.nav.kogi.test.shared.api.PostsResponse;
import com.nav.kogi.test.shared.cache.Cache;
import com.nav.kogi.test.shared.models.Post;
import com.nav.kogi.test.shared.presenter.Presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Eduardo Naveda
 */
@Activities
public class GalleryPresenter implements Presenter<GalleryView> {

    private Api api;
    private Cache cache;

    private GalleryView galleryView;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private List<Post> posts = new ArrayList<>();
    private int selectedIndex = 0;

    @Inject
    public GalleryPresenter(Api api, Cache cache) {
        this.api = api;
        this.cache = cache;
    }

    @Override
    public void takeView(GalleryView galleryView) {
        this.galleryView = galleryView;
    }

    @Override
    public void dropView() {
        this.galleryView = null;
        this.subscriptions.unsubscribe();
    }

    public void fetchPopular() {
        subscriptions.add(api.getPopularPosts(Api.DEFAULT_CLIENT_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostsResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadCachedPopularPosts();
                        // notify gallery view of network error.
                    }

                    @Override
                    public void onNext(PostsResponse postsResponse) {
                        cache.putPopularPostsResponse(postsResponse);
                        GalleryPresenter.this.posts.clear();
                        GalleryPresenter.this.posts.addAll(postsResponse.getPosts());
                        if (galleryView != null) {
                            galleryView.refresh();
                            galleryView.setSelectedPost(0);
                        }
                    }
                }));
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Returns true if loading from the cache was successful (posts returned non null and at least 1).
     *
     * @return true if successful, false otherwise.
     */
    public boolean loadCachedPopularPosts() {
        PostsResponse cachedPosts = cache.getPopularPostsResponse();
        if (cachedPosts != null && cachedPosts.getPosts().size() > 0) {
            posts.addAll(cachedPosts.getPosts());
            if (galleryView != null)
                galleryView.refresh();
            return true;
        } else
            return false;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void selectPost(int position) {
        selectedIndex = position;
        galleryView.setSelectedPost(position);
    }

}
