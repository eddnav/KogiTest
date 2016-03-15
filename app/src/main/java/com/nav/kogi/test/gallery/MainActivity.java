package com.nav.kogi.test.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nav.kogi.test.BaseActivity;
import com.nav.kogi.test.R;
import com.nav.kogi.test.shared.models.Post;
import com.nav.kogi.test.shared.util.AndroidUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements GalleryView {

    public static final String SELECTED_INDEX = "selected_index";
    public static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager";
    public static final int GRID_COLUMN_COUNT = 3;

    @Inject
    GalleryPresenter galleryPresenter;

    @Bind(R.id.viewerPager)
    ViewPager mViewerPager;
    @Bind(R.id.gallery)
    RecyclerView mGallery;
    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        galleryPresenter.takeView(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_COLUMN_COUNT);
        mGallery.setHasFixedSize(true);
        mGallery.setLayoutManager(layoutManager);

        mGallery.setAdapter(new PostAdapter(this, galleryPresenter));
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
        mViewerPager.setAdapter(new GalleryViewPagerAdapter(getSupportFragmentManager(),
                galleryPresenter, false, PostView.Navigation.DETAIL));
        mViewerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectFromGallery(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (savedInstanceState == null)
            galleryPresenter.fetchPopular();
        else {
            if (galleryPresenter.loadCachedPopularPosts()) {
                galleryPresenter.selectPost(savedInstanceState.getInt(SELECTED_INDEX));
                mGallery.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER));
            } else
                galleryPresenter.fetchPopular();
        }
    }

    private void selectFromGallery(int position) {
        ((PostAdapter) mGallery.getAdapter()).select(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            mRefreshLayout.setRefreshing(true);
            galleryPresenter.fetchPopular();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_INDEX, galleryPresenter.getSelectedIndex());
        outState.putParcelable(SAVED_LAYOUT_MANAGER, mGallery.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        galleryPresenter.dropView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PostFragment.TO_DETAIL_GALLERY_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                selectFromGallery(data.getIntExtra(PostDetailActivity.SELECTED_INDEX, 0));
            }
        }
    }

    public void refresh() {
        mGallery.getAdapter().notifyDataSetChanged();
        mViewerPager.getAdapter().notifyDataSetChanged();
        if (mRefreshLayout.isRefreshing())
            mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setSelectedPost(int position) {
        mViewerPager.setCurrentItem(position, false);
        mGallery.scrollToPosition(position);
    }

    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostItemViewHolder> {

        private Context context;
        private GalleryPresenter presenter;

        public PostAdapter(Context context, GalleryPresenter presenter) {
            this.context = context;
            this.presenter = presenter;
        }

        @Override
        public PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

            return new PostItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PostItemViewHolder holder, final int position) {
            Post post = presenter.getPosts().get(position);
            holder.caption.setText(post.getCaption() == null ? "..." : post.getCaption().getText());
            Glide.with(context)
                    .load(post.getImages().get(Post.ImageResolution.STANDARD).getUrl())
                    .placeholder(R.drawable.placeholder)
                    .crossFade()
                    .into(holder.image);
            holder.caption.setSelected(position == presenter.getSelectedIndex());
        }

        public void select(int position) {
            int selectedIndex = presenter.getSelectedIndex();
            if (position != selectedIndex) {
                notifyItemChanged(selectedIndex);
                presenter.selectPost(position);
                notifyItemChanged(presenter.getSelectedIndex());
            }
        }

        @Override
        public int getItemCount() {
            return presenter.getPosts().size();
        }

        public class PostItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public ImageView image;
            public TextView caption;

            public PostItemViewHolder(View v) {
                super(v);
                v.setOnClickListener(this);
                image = ButterKnife.findById(v, R.id.image);
                caption = ButterKnife.findById(v, R.id.caption);
            }

            @Override
            public void onClick(View view) {
                select(getLayoutPosition());
            }
        }
    }

}
