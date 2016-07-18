package com.mumu.projectli;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ElectricityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ElectricityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElectricityFragment extends MainFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "ProjectLI";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<ElectricityRecordParser.Entry> mHistoryList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FloatingActionButton mFab;
    private TextView mTodayElectricityTextView;
    private TextView mLastTextView, mLastRecordTextView, mCurrentTextView, mCurrentRecordTextView;
    private ListView mHistoryListView;
    private LayoutInflater mInflater;

    private OnFragmentInteractionListener mListener;
    private Typeface mAndroidClockMonoThin;
    private Typeface mAlphabetFace;

    public ElectricityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ElectricityFragment.
     */
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
        Log.d(TAG,"onCreate");
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
    public void setFabAppearance() {
        if (mFab == null) {
            return;
        }

        mFab.setImageResource(R.drawable.ic_menu_gallery);
        mFab.setContentDescription("Description from electricity");
        mFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFabClick(View view) {
        Log.d(TAG, "Fab click from electricity");
        final Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            final MainActivity deskClockActivity = (MainActivity) activity;
            deskClockActivity.showSnackBarMessage("New record is not implemented");
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        //applyDataToViews();
    }

    private void prepareView(View view) {
        mTodayElectricityTextView = (TextView) view.findViewById(R.id.textViewTodayElectricity);
        //mHistoryListView = (ListView) view.findViewById(R.id.listViewElectricHistory);
        mLastRecordTextView = (TextView) view.findViewById(R.id.textViewElectricLastRecord);
        mCurrentRecordTextView = (TextView) view.findViewById(R.id.textViewElectricTodayRecord);
        mLastTextView = (TextView) view.findViewById(R.id.textViewElectricLast);
        mCurrentTextView = (TextView) view.findViewById(R.id.textViewElectricToday);

        mAndroidClockMonoThin = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AndroidClockMono-Thin.ttf");
        mAlphabetFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Thin.ttf");
        mLastRecordTextView.setTypeface(mAndroidClockMonoThin);
        mLastRecordTextView.setTextSize(75);
        mCurrentRecordTextView.setTypeface(mAndroidClockMonoThin);
        mCurrentRecordTextView.setTextSize(75);
        mLastTextView.setTextSize(40);
        mLastTextView.setTypeface(mAlphabetFace);
        mLastTextView.setTextColor(Color.BLACK);
        mCurrentTextView.setTextSize(40);
        mCurrentTextView.setTypeface(mAlphabetFace);
        mCurrentTextView.setTextColor(Color.BLACK);
    }

    private void prepareData() {
        InputStream in = getResources().openRawResource(R.raw.electricity_sample);
        try {
            mHistoryList = new ElectricityRecordParser().parse(in);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Parsing XML file failed. Fetching xml to developer.");
            return;
        }
        Log.d(TAG, "xml data:");
        for (ElectricityRecordParser.Entry entry: mHistoryList) {
            Log.d(TAG, "  " + entry.toString());
        }
    }

    private void applyDataToViews() {
        ElectricityHistoryAdapter adapter = new ElectricityHistoryAdapter(mInflater, mHistoryList);
        mHistoryListView.setAdapter(adapter);
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
}
