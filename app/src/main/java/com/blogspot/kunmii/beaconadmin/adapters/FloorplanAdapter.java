package com.blogspot.kunmii.beaconadmin.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.data.FloorPlan;
import com.blogspot.kunmii.beaconadmin.data.FloorplanWithBeacons;
import com.blogspot.kunmii.beaconadmin.fragments.FloorplanListFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FloorplanAdapter extends RecyclerView.Adapter<FloorplanAdapter.RecyclerViewHolder>{

    List<FloorplanWithBeacons> floorPlans;
    FloorplanListFragment.FloorplanClickListener clickListener;
    public FloorplanAdapter(List<FloorplanWithBeacons> data, FloorplanListFragment.FloorplanClickListener listener){
        floorPlans = data;
        clickListener = listener;
    }

    @NonNull
    @Override
    public FloorplanAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FloorplanAdapter.RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.floorplan_item_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FloorplanAdapter.RecyclerViewHolder holder, int position) {
        FloorPlan floorPlan = floorPlans.get(position).getFloorPlan();


        holder.titleTextView.setText(floorPlan.getName());
        Picasso.get().load(Config.generateImageUrl(floorPlan.getFileurl())).into(holder.imaegView);

        holder.beacon_count_textView.setText(String.valueOf(floorPlans.get(position).getBeacons().size()));
        holder.createdTextView.setText(floorPlan.getUpdated());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(floorPlan);
            }
        });

    }

    @Override
    public int getItemCount() {
        return floorPlans.size();
    }

    public void addItems(List<FloorplanWithBeacons> data) {
        this.floorPlans = data;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView titleTextView;
        private ImageView imaegView;
        private TextView beacon_count_textView;
        private TextView createdTextView;

        RecyclerViewHolder(View view) {
            super(view);
            itemView = view;
            titleTextView = (TextView) view.findViewById(R.id.title_view);
            imaegView = view.findViewById(R.id.floorplan_view);
            beacon_count_textView = (TextView) view.findViewById(R.id.beacon_count_view);
            createdTextView = view.findViewById(R.id.created_view);
        }
    }
}
