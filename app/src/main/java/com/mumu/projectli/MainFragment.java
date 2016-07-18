package com.mumu.projectli;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

/**
 * Created by josh on 2016/7/8.
 */
public class MainFragment extends Fragment {
    final static String TAG = "MainFragment";
    protected FloatingActionButton mFab;

    public void onFabClick(View view){
        // Do nothing here , only in derived classes
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Activity activity = getActivity();
        Log.d(TAG, "MainFragment created.");
        if (activity instanceof MainActivity) {
            final MainActivity deskClockActivity = (MainActivity) activity;
            mFab = deskClockActivity.getFab();
            /*mLeftButton = deskClockActivity.getLeftButton();
            mRightButton = deskClockActivity.getRightButton();*/
        }
    }

    public void setFabAppearance() {
        // Do nothing here , only in derived classes
    }

    protected final MainActivity getDeskClock() {
        return (MainActivity) getActivity();
    }
}
