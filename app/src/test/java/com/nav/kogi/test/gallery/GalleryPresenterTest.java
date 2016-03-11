package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.api.Api;
import com.nav.kogi.test.shared.api.PostsResponse;
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Eduardo Naveda
 */
public class GalleryPresenterTest {

    @Before
    public void setup() {
        SchedulerOverrider.withImmediate();
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test
    public void fetchShouldPopulatePosts() throws Exception {
        Api api = mock(Api.class);
        PostsResponse postsResponse = mock(PostsResponse.class);
        GalleryView galleryView = mock(GalleryView.class);

        when(postsResponse.getPosts()).thenReturn(new ArrayList<Post>() {{
            add(mock(Post.class));
            add(mock(Post.class));
        }});
        when(api.getPopularPosts(anyString())).thenReturn(Observable.just(postsResponse));
        GalleryPresenter presenter = new GalleryPresenter(api);
        presenter.takeView(galleryView);
        presenter.fetch();

        assertThat("Presenter's post count", presenter.getPosts().size(), equalTo(2));
        verify(galleryView).refresh();
    }

    @Test
    public void getPostsShouldReturnNonNull() throws Exception {
        Api api = mock(Api.class);
        PostsResponse postsResponse = mock(PostsResponse.class);

        when(postsResponse.getPosts()).thenReturn(new ArrayList<Post>());

        when(api.getPopularPosts(anyString())).thenReturn(Observable.just(postsResponse));
        GalleryPresenter presenter = new GalleryPresenter(api);
        presenter.fetch();

        assertThat(presenter.getPosts(), is(notNullValue()));
    }

}