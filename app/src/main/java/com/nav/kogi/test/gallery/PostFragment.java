package com.nav.kogi.test.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nav.kogi.test.BaseFragment;
import com.nav.kogi.test.R;
import com.nav.kogi.test.shared.annotation.ForFragment;
import com.nav.kogi.test.shared.models.Post;
import com.viewpagerindicator.CirclePageIndicator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PostFragment extends BaseFragment implements PostView {

    public static final int TO_DETAIL_GALLERY_RESULT_CODE = 1;

    public static final String SHOW_INFO = "show_info";
    public static final String INDEX = "index";
    public static final String NAVIGATION = "navigation";

    @Inject
    @ForFragment
    PostPresenter postPresenter;

    @Bind(R.id.timestamp)
    TextView mTimestamp;
    @Bind(R.id.author)
    TextView mAuthor;
    @Bind(R.id.tags)
    TextView mTags;
    @Bind(R.id.info)
    LinearLayout mInfo;
    @Bind(R.id.image)
    ImageView mImage;
    @Bind(R.id.indicator)
    CirclePageIndicator mIndicator;

    boolean showInfo;
    private Navigation navigation;

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(boolean showInfo, int index, Navigation navigation) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOW_INFO, showInfo);
        args.putInt(INDEX, index);
        args.putSerializable(NAVIGATION, navigation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentComponent().inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            showInfo = arguments.getBoolean(SHOW_INFO);
            int index = arguments.getInt(INDEX);
            navigation = (Navigation) arguments.getSerializable(NAVIGATION);

            postPresenter.takeView(this);
            postPresenter.load(index);
        } else {
            throw new IllegalArgumentException("Fragment arguments are null");
        }

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postPresenter.onViewerClicked(navigation);
            }
        });

        return view;
    }

    @Override
    public void show(Post post) {
        if (!showInfo)
            mInfo.setVisibility(View.GONE);
        else {
            setInfo(post);
        }
        Glide.with(getContext())
                .load(post.getImages().get(Post.ImageResolution.STANDARD).getUrl())
                .crossFade()
                .into(mImage);
    }

    @Override
    public void toDetail(Post post, int index) {
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra(INDEX, postPresenter.getIndex());
        getActivity().startActivityForResult(intent, TO_DETAIL_GALLERY_RESULT_CODE);
    }

    @Override
    public void toUserProfile(String username) {
        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        intent.putExtra(UserProfileActivity.USERNAME, username);
        getActivity().startActivity(intent);
    }

    public void setInfo(Post post) {
        long unixSeconds = post.getCreatedTime();
        Date date = new Date(unixSeconds * 1000L);
        DateFormat format = SimpleDateFormat.getTimeInstance();
        String formattedDate = format.format(date);
        mTimestamp.setText(getString(R.string.info_publish_date, formattedDate));
        mAuthor.setText(getString(R.string.info_author, post.getUser().getUsername()));
        List<String> tags = post.getTags();
        if (tags.size() > 0)
            mTags.setText(getString(R.string.info_tag, processTagInfo(tags)));
        else
            mTags.setVisibility(View.GONE);
    }

    public String processTagInfo(List<String> tags) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tags.size(); i++) {
            builder.append("#");
            builder.append(tags.get(i));
            if (i < (tags.size() - 1))
                builder.append(", ");
        }
        return builder.toString();
    }

}
