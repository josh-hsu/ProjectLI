package com.mumu.projectli;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.mumu.projectli.utility.Log;

public class OutlineFragment extends MainFragment {
    private static final String TAG = "OutlineFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private PieGraph mMoneyGraph;
    private LineGraph mBodyWeightGraph;

    private OnFragmentInteractionListener mListener;

    public OutlineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoneyFragment.
     */
    public static OutlineFragment newInstance(String param1, String param2) {
        OutlineFragment fragment = new OutlineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ontline, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFabClick(View view) {
        Log.d(TAG, "Fab click from outline");
        final Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            final MainActivity deskClockActivity = (MainActivity) activity;
            deskClockActivity.showSnackBarMessage("Test for outline");
        }
    }

    @Override
    public void onDetailClick() {
        Log.d(TAG, "on detail click on outline");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        prepareView(view);
    }

    private void prepareView(View view) {
        mMoneyGraph = (PieGraph) view.findViewById(R.id.graphMoney);
        mBodyWeightGraph = (LineGraph) view.findViewById(R.id.graphBodyWeight);
        drawFakeChart();
    }

    private void drawFakeChart() {
        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#99CC00"));
        slice.setValue(2);
        slice.setTitle("可用");
        mMoneyGraph.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFBB33"));
        slice.setValue(3);
        slice.setTitle("男人");
        mMoneyGraph.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#AA66CC"));
        slice.setValue(8);
        slice.setTitle("食物");
        mMoneyGraph.addSlice(slice);

        Line l = new Line();
        mBodyWeightGraph.removeAllLines();
        mBodyWeightGraph.showHorizontalGrid(true);
        mBodyWeightGraph.showMinAndMaxValues(true);

        for (int i = 0; i < 10; i ++) {
            LinePoint p = new LinePoint();
            p.setX(i);
            p.setY(73-i);
            l.addPoint(p);
        }
        l.setColor(Color.parseColor("#FFBB33"));

        mBodyWeightGraph.addLine(l);
        mBodyWeightGraph.setRangeY(50, 80);
        mBodyWeightGraph.setLineToFill(0);
    }
}
