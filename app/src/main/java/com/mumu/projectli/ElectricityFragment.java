package com.mumu.projectli;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.mumu.projectli.utility.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ElectricityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ElectricityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElectricityFragment extends MainFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ProjectLI";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    // View holder
    private TextView mTodayElectricityTextView;
    private TextView mLastRecordTextView, mCurrentRecordTextView, mCurrentRecordDiffTextView, mCircleTextView;
    private TextView mLineGraphTextView;

    private LineGraph mLineGraph;
    private LayoutInflater mInflater;

    private OnFragmentInteractionListener mListener;
    private Typeface mAndroidClockMonoThin;
    private Typeface mAlphabetFace;

    // Data Holder
    private ElectricityRecordHandler mRecordHandler;

    public ElectricityFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ElectricityFragment newInstance(String param1, String param2) {
        ElectricityFragment fragment = new ElectricityFragment();
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
        Log.d(TAG,"onCreate with params " + mParam1 + ", " + mParam2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView");
        mInflater = inflater;
        return inflater.inflate(R.layout.fragment_electricity, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setFabAppearance();
    }

    @Override
    public void setFabAppearance() {
        if (mFab == null) {
            return;
        }
        Log.d(TAG, "set fab appearance");
        mFab.setImageResource(R.drawable.ic_menu_add);
        mFab.setContentDescription("Add record");
        mFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFabClick(View view) {
        Log.d(TAG, "Fab click from electricity");
        final Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            final MainActivity deskClockActivity = (MainActivity) activity;
            deskClockActivity.showSnackBarMessage(getString(R.string.electric_add_info));
            showAddDialog();
        }
    }

    @Override
    public void onDetailClick() {
        Log.d(TAG, "Detail click on electricity fragment");
        showBottomSheet();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        prepareView(view);
        prepareData();
        applyDataToViews();
    }

    private void prepareView(View view) {
        mLastRecordTextView = (TextView) view.findViewById(R.id.textViewElectricLastRecord);
        mCurrentRecordTextView = (TextView) view.findViewById(R.id.textViewElectricTodayRecord);
        mCurrentRecordDiffTextView = (TextView) view.findViewById(R.id.textViewElectricRecordDiff);
        mCircleTextView = (TextView) view.findViewById(R.id.textViewElectricCircle);

        mLineGraphTextView = (TextView) view.findViewById(R.id.textViewGraphDetail);
        mLineGraph = (LineGraph)view.findViewById(R.id.graphElectricity);

        mAndroidClockMonoThin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AndroidClockMono-Thin.ttf");
        mAlphabetFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        mLastRecordTextView.setTypeface(mAndroidClockMonoThin);
        mCurrentRecordTextView.setTypeface(mAndroidClockMonoThin);
        mCurrentRecordDiffTextView.setTypeface(mAndroidClockMonoThin);
        mCircleTextView.setTypeface(mAlphabetFace);
    }

    private void prepareData() {
        mRecordHandler = new ElectricityRecordHandler(getActivity());
    }

    private void applyDataToViews() {
        // handle circle outline view
        if (mRecordHandler.getHistoryList().size() == 0) {
            mCircleTextView.setText(getString(R.string.electric_no_data));
            mLastRecordTextView.setText("");
            mCurrentRecordDiffTextView.setText("");
            mCurrentRecordTextView.setText("");
        } else if (mRecordHandler.getHistoryList().size() == 1) {
            mCircleTextView.setText(mRecordHandler.getRecord(0)); // make sure circle doesn't contain text
            mLastRecordTextView.setText("");
            mCurrentRecordDiffTextView.setText("");
            mCurrentRecordTextView.setText(getString(R.string.electric_add_next));
            mCurrentRecordTextView.setTextSize(26);
        } else {
            int current, last;
            current = Integer.parseInt(mRecordHandler.getRecord(0));
            last = Integer.parseInt(mRecordHandler.getRecord(1));

            mCircleTextView.setText(""); // make sure circle doesn't contain text
            mCurrentRecordTextView.setTextSize(58);
            mCurrentRecordTextView.setText(""+current);
            mLastRecordTextView.setText(""+last);
            animateTextView(0, current-last, mCurrentRecordDiffTextView);
        }

        // handle chart
        if (mRecordHandler.getCount() < 1) {
            drawFakeChart();
        } else  {
            drawChart(10);
        }

        mLineGraph.setOnPointClickedListener(new LineGraph.OnPointClickedListener() {
            public void onClick(int lineIndex, int pointIndex) {
                touchOnChart(lineIndex, pointIndex);
            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 30) * count;
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText("+" + finalCount);
                }
            }, time);
        }
    }

    /*
     * drawChart
     *   we only draw the newest 10 points
     */
    private void drawChart(int maxCount) {
        int availableDataCount = mRecordHandler.getCount() - 1;
        int maximumY = 10;
        Line l = new Line();

        if (availableDataCount <= 1) {
            Log.d(TAG, "At least 2 points to draw a line");
            return;
        }

        mLineGraph.removeAllLines();

        for (int i = 0; i < (availableDataCount > maxCount ? maxCount : availableDataCount); i ++) {
            int increment = mRecordHandler.getIncrement(i);
            LinePoint p = new LinePoint();
            p.setX(i);
            p.setY(increment);
            l.addPoint(p);

            if (increment > maximumY)
                maximumY = increment;
        }
        l.setColor(Color.parseColor("#FFBB33"));

        mLineGraph.addLine(l);
        mLineGraph.setRangeY(-5, maximumY);
        mLineGraph.setLineToFill(0);
        mLineGraphTextView.setText(getString(R.string.electric_graphic_touch_to_see_detail));
    }

    private void drawFakeChart() {
        Line l = new Line();
        mLineGraph.removeAllLines();
        mLineGraph.showHorizontalGrid(true);
        mLineGraph.showMinAndMaxValues(true);

        for (int i = 0; i < 10; i ++) {
            LinePoint p = new LinePoint();
            p.setX(i);
            p.setY(0);
            l.addPoint(p);
        }
        l.setColor(Color.parseColor("#FFBB33"));

        mLineGraph.addLine(l);
        mLineGraph.setRangeY(-5, 22);
        mLineGraph.setLineToFill(0);
        mLineGraphTextView.setText(getString(R.string.electric_graphic_no_enough_data));
    }

    private void touchOnChart(int lineIndex, int pointIndex) {
        Log.d(TAG, "Touch on line " + lineIndex + " point " + pointIndex);
        if (mRecordHandler.getCount() < 2) {
            mLineGraphTextView.setText(getString(R.string.electric_graphic_no_detail));
        } else {
            String text = "+" + mRecordHandler.getIncrement(pointIndex) + "\n" +
                    mRecordHandler.getDateFormatted(pointIndex);
            mLineGraphTextView.setText(text);
        }
    }

    private void showBottomSheet() {
        ElectricityBottomSheet ebs = new ElectricityBottomSheet();
        ebs.setElectricityRecordHandler(mRecordHandler);
        ebs.show(getFragmentManager(), ebs.getTag());
    }

    /*
     *  Add electricity
     */
    private void showAddDialog() {
        MaterialDialog builder = new MaterialDialog.Builder(getContext())
                .title(getString(R.string.electric_add))
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(getString(R.string.electric_add_field_holder), mRecordHandler.getRecord(0), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        Log.d(TAG, "Get input " + input);
                        addNewRecordFromUser(input.toString(), "NOW");
                        applyDataToViews();
                    }
                }).negativeText(getString(R.string.electric_add_cancel)).show();
    }

    private int addNewRecordFromUser(String record, String date) {
        String targetDate;

        if (date.equals("NOW")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            targetDate = df.format(Calendar.getInstance().getTime());
        } else {
            targetDate = date;
        }

        try {
            mRecordHandler.addRecord(new ElectricityRecordParser.Entry(mRecordHandler.getNextSerial(), targetDate, record));
            mRecordHandler.refreshFromFile();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Fail to add record " + e.getMessage());
        }

        return -1;
    }
}
