package com.mumu.projectli;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by josh on 2016/7/24.
 */
public class ElectricityRecyclerViewAdapter extends RecyclerView.Adapter<ElectricityRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener {

    private static final String TAG = "ProjectLI";
    private int expandedPosition = -1;
    private ElectricityRecordHandler mRecordHandler;

    public ElectricityRecyclerViewAdapter (ElectricityRecordHandler rh) {
        this.mRecordHandler = rh;
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
        return mRecordHandler.getCount();
    }

    @Override
    public void onBindViewHolder(ElectricityRecyclerViewAdapter.ViewHolder holder, int position) {
        int increment = mRecordHandler.getIncrement(position);

        holder.recordText.setText(mRecordHandler.getRecord(position));
        holder.dateText.setText(mRecordHandler.getDateFormatted(position));

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Not ready", Toast.LENGTH_SHORT).show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Not ready", Toast.LENGTH_SHORT).show();
            }
        });

        if (increment == -1)
            holder.incrementText.setText("+0");
        else
            holder.incrementText.setText("+"+increment);

        if (position == expandedPosition) {
            holder.llExpandArea.setVisibility(View.VISIBLE);
        } else {
            holder.llExpandArea.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        String theString = mRecordHandler.getRecord(holder.getPosition());
        int prev;

        // Check for an expanded view, collapse if you find one
        if (expandedPosition >= 0) {
            prev = expandedPosition;
            notifyItemChanged(prev);
        }

        // Set the current position to "expanded"
        if (expandedPosition == holder.getPosition())
            expandedPosition = -1;
        else
            expandedPosition = holder.getPosition();
        notifyItemChanged(holder.getPosition());
    }

    /**
     * Create a ViewHolder to represent your cell layout
     * and data element structure
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView recordText, dateText, incrementText;
        Button editButton, deleteButton;
        LinearLayout llExpandArea;

        public ViewHolder(View itemView) {
            super(itemView);

            recordText = (TextView) itemView.findViewById(R.id.tvTitle);
            dateText = (TextView) itemView.findViewById(R.id.tvSubTitle);
            incrementText = (TextView) itemView.findViewById(R.id.textViewElectricIncrease);
            llExpandArea = (LinearLayout) itemView.findViewById(R.id.llExpandArea);
            editButton = (Button) itemView.findViewById(R.id.btn_edit);
            deleteButton = (Button) itemView.findViewById(R.id.btn_delete);
        }
    }
}
