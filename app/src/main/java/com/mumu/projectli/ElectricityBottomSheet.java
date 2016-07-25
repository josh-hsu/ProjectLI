package com.mumu.projectli;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by josh on 2016/7/24.
 */
public class ElectricityBottomSheet extends BottomSheetDialogFragment {

    private RecyclerView mEHRecycler;
    private ElectricityRecyclerViewAdapter mEHAdapter;
    private StaggeredGridLayoutManager mSGLM;

    // An array of meaningless titles
    private static final String[] someTitles = {
            "3412",
            "3421",
            "3427",
            "3433",
            "3439",
            "3445"
    };


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.electricity_bottom_sheet, null);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        mEHRecycler = (RecyclerView) contentView.findViewById(R.id.recyclerViewElectricityList);
        mSGLM = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mEHRecycler.setLayoutManager(mSGLM);

        //////////////////////////////
        //  Setup Adapter & DataSet //
        //////////////////////////////
        ArrayList<String> myDataset = new ArrayList<String>();

        // Load up the dataset with random titles
        for (int x = 0; x < 50; x++) {
            myDataset.add(someTitles[x % 5]);
        }

        mEHAdapter = new ElectricityRecyclerViewAdapter(myDataset);
        mEHRecycler.setAdapter(mEHAdapter);

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}
