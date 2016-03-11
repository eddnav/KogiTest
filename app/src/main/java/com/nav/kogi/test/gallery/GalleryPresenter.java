package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.annotation.Activities;
import com.nav.kogi.test.shared.api.Api;
import com.nav.kogi.test.shared.api.PostsResponse;
import com.nav.kogi.test.shared.models.Post;

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
public class GalleryPresenter {

    public final String CLIENT_ID = "05132c49e9f148ec9b8282af33f88ac7";

    private Api api;

    private GalleryView galleryView;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private List<Post> posts = new ArrayList<>();

    @Inject
    public GalleryPresenter(Api api) {
        this.api = api;
    }

    public void takeView(GalleryView galleryView) {
        this.galleryView = galleryView;
    }

    public void fetch() {
        subscriptions.add(api.getPopularPosts(CLIENT_ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostsResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO use cached posts if available
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PostsResponse postsResponse) {
                        // TODO cache post response
                        // TODO hide network error snackbar.
                        posts.clear();
                        posts.addAll(postsResponse.getPosts());
                        if(galleryView != null)
                            galleryView.refresh();
                    }
                }));
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void dropView() {
        this.galleryView = null;
        this.subscriptions.unsubscribe();
    }

}
