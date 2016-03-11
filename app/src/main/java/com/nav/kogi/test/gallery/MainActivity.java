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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
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

public class MainActivity extends AppCompatActivity implements GalleryView {

    public static final int GRID_COLUMN_COUNT = 3;

    @Inject
    GalleryPresenter galleryPresenter;

    private PostAdapter postAdapter;
    private int selectedPostIndex = 0;

    @Bind(R.id.viewer)
    ImageView mViewer;
    @Bind(R.id.gallery)
    RecyclerView mGallery;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
                galleryPresenter.fetch();
            }
        });

        //TODO set empty views for viewer and grid.

        galleryPresenter.fetch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        galleryPresenter.dropView();
    }

    public void refresh() {
        postAdapter.notifyDataSetChanged();
        Glide.with(this)
                .load(galleryPresenter.getPosts().get(selectedPostIndex).getImages().get(Post.ImageResolution.STANDARD).getUrl())
                .crossFade()
                .into(mViewer);
        if(mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
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
        public void onBindViewHolder(PostItemViewHolder holder, int position) {
            Post post = presenter.getPosts().get(position);
            holder.caption.setText(post.getCaption() == null ? "..." : post.getCaption().getText());
            final DrawableRequestBuilder<String> drawableRequestBuilder = Glide.with(context)
                    .load(post.getImages().get(Post.ImageResolution.STANDARD).getUrl())
                    .placeholder(R.drawable.placeholder) // TODO create better place holders, one for the top viewer and one for the cells.
                    .crossFade();

            drawableRequestBuilder.into(holder.image);
            holder.imageClickableForeground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawableRequestBuilder.into(viewer);
                }
            });
        }

        @Override
        public int getItemCount() {
            return presenter.getPosts().size();
        }

        public class PostItemViewHolder extends RecyclerView.ViewHolder {
            public FrameLayout imageClickableForeground;
            public ImageView image;
            public TextView caption;

            public PostItemViewHolder(View v) {
                super(v);
                imageClickableForeground = ButterKnife.findById(v, R.id.imageClickableForeground);
                image = ButterKnife.findById(v, R.id.image);
                caption = ButterKnife.findById(v, R.id.caption);
            }
        }
    }

}
