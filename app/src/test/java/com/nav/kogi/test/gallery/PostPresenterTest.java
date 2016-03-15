package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.AppError;
import com.nav.kogi.test.shared.api.PostsResponse;
import com.nav.kogi.test.shared.cache.Cache;
import com.nav.kogi.test.shared.models.Post;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import rx.Observable;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Eduardo Naveda
 */
public class PostPresenterTest {

    private Cache cache;
    private PostPresenter presenter;

    @Before
    public void setup() {

        cache = mock(Cache.class);
        presenter = new PostPresenter(cache);
    }

    @Test
    public void loadShouldInitializePost() throws Exception {
        PostView postView = mock(PostView.class);
        Post post = mock(Post.class);

        when(cache.getPopularPostByIndex(anyInt())).thenReturn(post);
        presenter.takeView(postView);
        presenter.load(0);

        assertThat(presenter.getPost(), is(notNullValue()));
        verify(postView).show(post);
    }

    @Test
    public void loadShouldPresentErrorWhenUnsuccessful() throws Exception {
        PostView postView = mock(PostView.class);

        when(cache.getPopularPostByIndex(anyInt())).thenReturn(null);
        presenter.takeView(postView);
        presenter.load(0);

        verify(postView).showError(AppError.GENERIC);
    }

}