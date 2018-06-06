package com.blogspot.kunmii.beaconadmin.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.R;
import com.kontakt.sdk.android.ble.device.EddystoneDevice;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EddyListAdapter extends RecyclerView.Adapter<EddyListAdapter.EddyViewHolder>{
    List<BeaconHelper.EddystoneWrapper> beaconWrappers;
    EddyListClickListener listClickListener = null;


    public EddyListAdapter(EddyListClickListener listener, List<BeaconHelper.EddystoneWrapper> data)
    {
        listClickListener = listener;
        beaconWrappers = data;
    }

    @NonNull
    @Override
    public EddyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EddyListAdapter.EddyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eddystone_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull EddyViewHolder holder, int position) {
        BeaconHelper.EddystoneWrapper beaconWrapper = beaconWrappers.get(position);

        IEddystoneNamespace region = beaconWrapper.namespace;
        IEddystoneDevice beacon = beaconWrapper.device;

        holder.namespace.setText(region.getIdentifier());
        holder.instanceId.setText(beacon.getInstanceId());
        holder.proximity.setText(beacon.getProximity().toString());
        holder.telemetry.setText(beacon.getTelemetry()!=null?"YES": "NO");
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClickListener.onClick(beaconWrapper);
            }
        });

        holder.address.setText(beacon.getAddress());



    }

    @Override
    public int getItemCount() {
        return beaconWrappers.size();
    }

    public void addItems(List<BeaconHelper.EddystoneWrapper> data) {
        this.beaconWrappers = data;

        Collections.sort(this.beaconWrappers, new Comparator<BeaconHelper.EddystoneWrapper>() {
            @Override
            public int compare(BeaconHelper.EddystoneWrapper o1, BeaconHelper.EddystoneWrapper o2) {
                return o1.device.getProximity().compareTo(o2.device.getProximity());
            }
        });

        notifyDataSetChanged();
    }


    public class EddyViewHolder extends RecyclerView.ViewHolder{

        TextView namespace;
        TextView instanceId;
        TextView proximity;
        TextView telemetry;
        TextView address;

        View v;

        public EddyViewHolder(View itemView) {
            super(itemView);

            v = itemView;
            namespace = itemView.findViewById(R.id.namespace_text_view);
            instanceId = itemView.findViewById(R.id.instanceid_text_view);
            proximity = itemView.findViewById(R.id.proximity_text_view);
            telemetry = itemView.findViewById(R.id.has_telemetry);
            address = itemView.findViewById(R.id.mac_text_view);
        }
    }


   public interface EddyListClickListener{
        void onClick(BeaconHelper.EddystoneWrapper item);
   }
}
