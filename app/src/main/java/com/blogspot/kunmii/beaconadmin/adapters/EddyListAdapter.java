package com.blogspot.kunmii.beaconadmin.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.R;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneNamespace;
import com.kontakt.sdk.android.common.profile.RemoteBluetoothDevice;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EddyListAdapter extends RecyclerView.Adapter<EddyListAdapter.EddyViewHolder>{
    List<IEddystoneDevice> beaconWrappers;
    EddyListClickListener listClickListener = null;


    public EddyListAdapter(EddyListClickListener listener, List<IEddystoneDevice> data)
    {
        setHasStableIds(true);
        listClickListener = listener;
        beaconWrappers = data;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public EddyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EddyListAdapter.EddyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.eddystone_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EddyViewHolder holder, int position) {

        IEddystoneDevice beacon = beaconWrappers.get(position);

        String name = beacon.getName();
        String model = beacon.getModel().toString();

        if(beacon.getName()!=null) {
            holder.name.setText(beacon.getName());
        }

        if(beacon.getNamespace()!=null)
            holder.eddyNameSpace.setText(beacon.getNamespace());

        if(beacon.getInstanceId()!=null)
        holder.instanceId.setText(beacon.getInstanceId());

        holder.proximity.setText(beacon.getProximity().toString());
        holder.telemetry.setText(beacon.getTelemetry()!=null?"YES": "NO");
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClickListener.onClick(beacon);
            }
        });

        holder.address.setText(beacon.getAddress());
    }

    @Override
    public int getItemCount() {
        return beaconWrappers.size();
    }

    public void addItems(List<IEddystoneDevice> data) {
        this.beaconWrappers = data;

        Collections.sort(this.beaconWrappers, new Comparator<IEddystoneDevice>() {
            @Override
            public int compare(IEddystoneDevice o1, IEddystoneDevice o2) {
                return o1.getProximity().compareTo(o2.getProximity());
            }
        });

        notifyDataSetChanged();
    }


    public class EddyViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        TextView eddyNameSpace;
        TextView instanceId;
        TextView proximity;
        TextView telemetry;
        TextView address;

        View v;

        public EddyViewHolder(View itemView) {
            super(itemView);

            v = itemView;


            name = itemView.findViewById(R.id.name_text_view);

            eddyNameSpace = itemView.findViewById(R.id.namespace_text_view_main);
            instanceId = itemView.findViewById(R.id.instanceid_text_view);
            proximity = itemView.findViewById(R.id.proximity_text_view);
            telemetry = itemView.findViewById(R.id.has_telemetry);
            address = itemView.findViewById(R.id.mac_text_view);
        }
    }


   public interface EddyListClickListener{
        void onClick(IEddystoneDevice item);
   }
}
