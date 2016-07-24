package com.mumu.projectli;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by josh on 2016/7/24.
 */
public class ElectricityRecyclerViewAdapter extends RecyclerView.Adapter<ElectricityRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener {

    private static final String TAG = "ProjectLI";

    // Some dark background colors to spice up
    // our title in the display (Black, Red, Green, Blue)
    private static final int[] bgColors = {
            0xAA000000,
            0xFF800000,
            0xFF008000,
            0xFF000080
    };

    // Some darker background colors to spice up
    // our subtitle in the display (Black, Red, Green, Blue)
    private static final int[] sbgColors = {
            0xFF000000,
            0xFF600000,
            0xFF006000,
            0xFF000060
    };

    // Get a Random generated to pick a cell's
    // background color from the list above.
    private static Random randy = new Random();


    // Hold the position of the expanded item
    private int expandedPosition = -1;


    private ArrayList<String> mDataset;


    public ElectricityRecyclerViewAdapter (ArrayList<String> myDataset) {
        this.mDataset = myDataset;
    }

    @Override
    public ElectricityRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.electricity_card_cell, parent, false);

        ViewHolder holder = new ViewHolder(v);

        // Sets the click adapter for the entire cell
        // to the one in this class.
        holder.itemView.setOnClickListener(ElectricityRecyclerViewAdapter.this);
        holder.itemView.setTag(holder);

        return holder;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onBindViewHolder(ElectricityRecyclerViewAdapter.ViewHolder holder, int position) {

        int colorIndex = randy.nextInt(bgColors.length);
        holder.tvTitle.setText(mDataset.get(position));
        //holder.tvTitle.setBackgroundColor(bgColors[colorIndex]);
        //holder.tvSubTitle.setBackgroundColor(sbgColors[colorIndex]);

        if (position == expandedPosition) {
            holder.llExpandArea.setVisibility(View.VISIBLE);
        } else {
            holder.llExpandArea.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        String theString = mDataset.get(holder.getPosition());

        // Check for an expanded view, collapse if you find one
        if (expandedPosition >= 0) {
            int prev = expandedPosition;
            notifyItemChanged(prev);
        }
        // Set the current position to "expanded"
        expandedPosition = holder.getPosition();
        notifyItemChanged(expandedPosition);
        Log.d(TAG, theString);
    }

    /**
     * Create a ViewHolder to represent your cell layout
     * and data element structure
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvSubTitle;
        LinearLayout llExpandArea;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tvSubTitle);
            llExpandArea = (LinearLayout) itemView.findViewById(R.id.llExpandArea);
        }
    }
}
