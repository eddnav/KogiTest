package com.nav.kogi.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * @author Eduardo Naveda
 */
public class BaseFragment extends Fragment {

    private AppComponent appComponent;
    private FragmentComponent fragmentComponent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public AppComponent getAppComponent() {
        return ((App) getContext().getApplicationContext()).getAppComponent();
    }

    public ActivityComponent getActivityComponent() {
        return ((BaseActivity) getActivity()).getActivityComponent();
    }

    public FragmentComponent getFragmentComponent() {
        return DaggerFragmentComponent.builder()
                .activityComponent(getActivityComponent())
                .fragmentModule(new FragmentModule())
                .build();
    }

}
