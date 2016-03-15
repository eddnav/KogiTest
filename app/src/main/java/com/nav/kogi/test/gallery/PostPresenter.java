package com.nav.kogi.test.gallery;

import com.nav.kogi.test.shared.AppError;
import com.nav.kogi.test.shared.cache.Cache;
import com.nav.kogi.test.shared.models.Post;
import com.nav.kogi.test.shared.presenter.Presenter;

/**
 * @author Eduardo Naveda
 */
public class PostPresenter implements Presenter<PostView> {

    private Cache cache;

    private PostView postView;
    private Post post;
    private int index;

    public PostPresenter(Cache cache) {
        this.cache = cache;
    }

    public void load(int index) {
        this.index = index;
        post = cache.getPopularPostByIndex(index);
        if(post != null)
            postView.show(post);
        else
            postView.showError(AppError.GENERIC);
    }

    @Override
    public void takeView(PostView postView) {
        this.postView = postView;
    }

    @Override
    public void dropView() {
        this.postView = null;
    }

    public void onViewerClicked(PostView.Navigation navigation) {
        switch (navigation) {
            case DETAIL:
                postView.toDetail(post, index);
                break;
            case USER_PROFILE:
                postView.toUserProfile(post.getUser().getUsername());
                break;
            default:
                throw new IllegalArgumentException(navigation + " is not a valid navigation tag");
        }
    }

    public Post getPost() {
        return post;
    }

    public int getIndex() {
        return index;
    }

}
