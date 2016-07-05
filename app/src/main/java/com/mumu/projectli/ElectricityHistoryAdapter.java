package com.mumu.projectli;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by josh on 2016/7/5.
 */
public class ElectricityHistoryAdapter extends BaseAdapter {
    public final static int PRESERVE = 1;

    public List<ElectricityRecordParser.Entry> mList;
    LayoutInflater mInflater;
    Activity mActivity;
    TextView mDateTextView;
    TextView mRecordTextView;
    TextView mDiffTextView;

    public ElectricityHistoryAdapter(LayoutInflater activity,List<ElectricityRecordParser.Entry> list){
        super();
        this.mInflater = activity;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size() + PRESERVE;
    }

    @Override
    public ElectricityRecordParser.Entry getItem(int i) {
        if (i < PRESERVE)
            return new ElectricityRecordParser.Entry("-1", "日期", "讀數");
        else
            return mList.get(i - PRESERVE);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null){
            view = mInflater.inflate(R.layout.electricity_history_list, null);

            mDateTextView = (TextView) view.findViewById(R.id.ListViewRowDate);
            mRecordTextView = (TextView) view.findViewById(R.id.ListViewRowRecord);
            mDiffTextView = (TextView) view.findViewById(R.id.ListViewRowDiff);
        }

        ElectricityRecordParser.Entry entry = getItem(i);
        mDateTextView.setText(entry.date);
        mRecordTextView.setText(entry.record);
        mDiffTextView.setText("^_^");

        return view;
    }
}
