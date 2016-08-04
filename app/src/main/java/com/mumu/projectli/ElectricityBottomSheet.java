package com.mumu.projectli;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * ElectricityBottomSheet class holds the view of total usage data
 *
 * We must implement a maximum length of data in case this occupies too
 * many resources
 */
public class ElectricityBottomSheet extends BottomSheetDialogFragment {

    private RecyclerView mEHRecycler;
    private ElectricityRecyclerViewAdapter mEHAdapter;
    private StaggeredGridLayoutManager mSGLM;
    private static ElectricityRecordHandler mRecordHandler;

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

        //  Setup Adapter & DataSet //
        mEHAdapter = new ElectricityRecyclerViewAdapter(mRecordHandler);
        mEHRecycler.setAdapter(mEHAdapter);

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    public void setElectricityRecordHandler(ElectricityRecordHandler handler) {
        mRecordHandler = handler;
    }
}
