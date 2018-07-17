package com.blogspot.kunmii.beaconadmin.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.R;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IBeaconAdapter extends RecyclerView.Adapter<IBeaconAdapter.IbeaconViewHolder>{
    List<IBeaconDevice> beaconWrappers;
    IbeaconListClickListener listClickListener = null;


    public IBeaconAdapter(IbeaconListClickListener listener, List<IBeaconDevice> data)
    {
        listClickListener = listener;
        beaconWrappers = data;
        setHasStableIds(true);
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
    public IbeaconViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IBeaconAdapter.IbeaconViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beacon_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull IbeaconViewHolder holder, int position) {
        IBeaconDevice beacon = beaconWrappers.get(position);

        if(beacon.getName()!=null) {
            holder.name.setText(beacon.getName());
        }
        else
        {
            holder.name.setText("");
        }
        holder.uuid.setText(String.valueOf(beacon.getProximityUUID().toString()));
        holder.major.setText(String.valueOf(beacon.getMajor()));
        holder.minor.setText(String.valueOf(beacon.getMinor()));
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listClickListener.onClick(beacon);
            }
        });

        holder.proximity.setText(beacon.getProximity().toString());
        holder.address.setText(beacon.getAddress());
    }

    @Override
    public int getItemCount() {
        return beaconWrappers.size();
    }

    public void addItems(List<IBeaconDevice> data) {
        this.beaconWrappers = data;

        Collections.sort(beaconWrappers, new Comparator<IBeaconDevice>() {
            @Override
            public int compare(IBeaconDevice o1, IBeaconDevice o2) {
                return o1.getProximity().compareTo(o2.getProximity());
            }
        });

        notifyDataSetChanged();
    }


    public class IbeaconViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView uuid;
        TextView major;
        TextView minor;
        TextView proximity;
        TextView address;

        View v;



        public IbeaconViewHolder(View itemView) {
            super(itemView);

            v = itemView;
            name = itemView.findViewById(R.id.name_text_view);
            uuid = itemView.findViewById(R.id.uuid_text_view);
            major = itemView.findViewById(R.id.major_text_view);
            minor = itemView.findViewById(R.id.minor_text_view);
            proximity = itemView.findViewById(R.id.proximity_text_view);
            address = itemView.findViewById(R.id.mac_text_view);


        }
    }


   public interface IbeaconListClickListener{
        void onClick(IBeaconDevice item);
   }
}
