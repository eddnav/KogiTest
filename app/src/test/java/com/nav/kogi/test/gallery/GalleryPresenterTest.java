package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.api.Api;
import com.nav.kogi.test.shared.api.PostsResponse;
import com.nav.kogi.test.shared.cache.Cache;
import com.nav.kogi.test.shared.models.Post;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Eduardo Naveda
 */
public class GalleryPresenterTest {

    private Api api;
    private Cache cache;
    private PostsResponse postsResponse;
    private GalleryPresenter presenter;

    @Before
    public void setup() {
        SchedulerOverrider.withImmediate();
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
        api = mock(Api.class);
        cache = mock(Cache.class);
        postsResponse = mock(PostsResponse.class);
        presenter = new GalleryPresenter(api, cache);
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void fetchPopularShouldPopulatePosts() throws Exception {
        GalleryView galleryView = mock(GalleryView.class);

        when(postsResponse.getPosts()).thenReturn(new ArrayList<Post>() {{
            add(mock(Post.class));
            add(mock(Post.class));
        }});
        when(api.getPopularPosts(anyString())).thenReturn(Observable.just(postsResponse));
        presenter.takeView(galleryView);
        presenter.fetchPopular();

        assertThat("Presenter's post count", presenter.getPosts().size(), greaterThan(0));
        verify(galleryView).refresh();
    }

    @Test
    public void getPostsShouldReturnNonNull() throws Exception {
        when(postsResponse.getPosts()).thenReturn(new ArrayList<Post>());

        when(api.getPopularPosts(anyString())).thenReturn(Observable.just(postsResponse));
        presenter.fetchPopular();

        assertThat(presenter.getPosts(), is(notNullValue()));
    }

    @Test
    public void loadCachedPopularPostsShouldPopulatePosts() throws Exception {
        GalleryView galleryView = mock(GalleryView.class);
        presenter.takeView(galleryView);

        when(cache.getPopularPostsResponse()).thenReturn(postsResponse);
        when(postsResponse.getPosts()).thenReturn(new ArrayList<Post>() {{
            add(mock(Post.class));
        }});
        boolean success = presenter.loadCachedPopularPosts();

        assertThat("Presenter's post count", presenter.getPosts().size(), greaterThan(0));
        verify(galleryView).refresh();
    }


    @Test
    public void loadCachedPopularPostsShouldReturnTrueWhenSuccessful() throws Exception {
        GalleryView galleryView = mock(GalleryView.class);
        presenter.takeView(galleryView);

        when(cache.getPopularPostsResponse()).thenReturn(postsResponse);
        when(postsResponse.getPosts()).thenReturn(new ArrayList<Post>() {{
            add(mock(Post.class));
        }});
        boolean success = presenter.loadCachedPopularPosts();

        assertThat(success, is(true));
    }

    @Test
    public void loadCachedPopularPostsShouldReturnFalseWhenUnsuccessful() throws Exception {
        when(cache.getPopularPostsResponse()).thenReturn(postsResponse);
        when(postsResponse.getPosts()).thenReturn(new ArrayList<Post>());
        boolean success = presenter.loadCachedPopularPosts();

        assertThat(success, is(false));

        postsResponse = null;
        success = presenter.loadCachedPopularPosts();

        assertThat(success, is(false));
    }

}