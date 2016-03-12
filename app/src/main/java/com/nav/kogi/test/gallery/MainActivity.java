package com.nav.kogi.test.gallery;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nav.kogi.test.ActivityModule;
import com.nav.kogi.test.App;
import com.nav.kogi.test.DaggerActivityComponent;
import com.nav.kogi.test.R;
import com.nav.kogi.test.shared.models.Post;
import com.nav.kogi.test.shared.util.AndroidUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

public class MainActivity extends AppCompatActivity implements GalleryView {

    public static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager";
    public static final int GRID_COLUMN_COUNT = 3;

    @Inject
    GalleryPresenter galleryPresenter;

    private PostAdapter postAdapter;

    @Bind(R.id.viewer)
    ImageView mViewer;
    @Bind(R.id.gallery)
    RecyclerView mGallery;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @State
    public int selectedPostIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Icepick.restoreInstanceState(this, savedInstanceState);

        DaggerActivityComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);

        galleryPresenter.takeView(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_COLUMN_COUNT);
        mGallery.setHasFixedSize(true);
        mGallery.setLayoutManager(layoutManager);

        postAdapter = new PostAdapter(this, galleryPresenter, mViewer);
        mGallery.setAdapter(postAdapter);
        mGallery.addItemDecoration(new GridCellSpacingDecorator((int)
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        getResources().getDimension(R.dimen.post_item_space),
                        getResources().getDisplayMetrics())));

        mRefreshLayout.setColorSchemeColors(
                AndroidUtil.getColor(this, R.color.color_primary),
                AndroidUtil.getColor(this, R.color.color_primary_dark));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                galleryPresenter.fetchPopular();
            }
        });

        //TODO set empty/loading views for viewer and grid.
        if (savedInstanceState == null)
            galleryPresenter.fetchPopular();
        else {
            if (galleryPresenter.loadCachedPopularPosts()) {
                mGallery.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER));
                galleryPresenter.selectPost(selectedPostIndex);
            } else
                galleryPresenter.fetchPopular();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_LAYOUT_MANAGER, mGallery.getLayoutManager().onSaveInstanceState());
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        galleryPresenter.dropView();
    }

    public void refresh() {
        postAdapter.notifyDataSetChanged();
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setSelectedPost(int position) {
        selectedPostIndex = position;
        String url =
                galleryPresenter.getPosts().get(position).getImages().get(Post.ImageResolution.STANDARD).getUrl();
        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.placeholder) // TODO create better place holders, one for the top viewer and one for the cells.
                .crossFade()
                .into(mViewer);
    }

    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostItemViewHolder> {

        private Context context;
        private GalleryPresenter presenter;
        private ImageView viewer;

        public PostAdapter(Context context, GalleryPresenter presenter, ImageView viewer) {
            this.context = context;
            this.presenter = presenter;
            this.viewer = viewer;
        }

        @Override
        public PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

            return new PostItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PostItemViewHolder holder, final int position) {
            Post post = presenter.getPosts().get(position);
            holder.caption.setText(post.getCaption() == null ? "..." : post.getCaption().getText());
             Glide.with(context)
                    .load(post.getImages().get(Post.ImageResolution.STANDARD).getUrl())
                    .placeholder(R.drawable.placeholder) // TODO create better place holders, one for the top viewer and one for the cells.
                    .crossFade()
                    .into(holder.image);

            holder.foreground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.selectPost(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return presenter.getPosts().size();
        }

        public class PostItemViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout foreground;
            public ImageView image;
            public TextView caption;

            public PostItemViewHolder(View v) {
                super(v);
                foreground = (LinearLayout) v;
                image = ButterKnife.findById(v, R.id.image);
                caption = ButterKnife.findById(v, R.id.caption);
            }
        }
    }

}
