package com.blogspot.kunmii.beaconadmin.Dialog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blogspot.kunmii.beaconadmin.ApplicationViewModel;
import com.blogspot.kunmii.beaconadmin.Helpers.BeaconHelper;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.adapters.EddyListAdapter;
import com.blogspot.kunmii.beaconadmin.adapters.IBeaconAdapter;
import com.blogspot.kunmii.beaconadmin.adapters.ProjectAdapter;
import com.blogspot.kunmii.beaconadmin.data.Project;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IEddystoneDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabbedFragment {

    static public class IBeaconFragment extends Fragment
    {

        RecyclerView recyclerView;
        IBeaconAdapter adapter;
        private ApplicationViewModel viewModel;

        IBeaconAdapter.IbeaconListClickListener listener;


        public static IBeaconFragment getInstance(IBeaconAdapter.IbeaconListClickListener listener)
        {
            IBeaconFragment instance = new IBeaconFragment();
            instance.listener = listener;

            return instance;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fragment_beacons, container,false);

            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
            adapter = new IBeaconAdapter(listener, new ArrayList<>());

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.setAdapter(adapter);


            return v;
        }

        @Override
        public void onResume() {
            super.onResume();


        }

        public void setItems(List<IBeaconDevice> data) {
            adapter.addItems(data);
        }
    }

    static public class EddyFragment extends Fragment
    {
        RecyclerView recyclerView;
        EddyListAdapter adapter;
        private ApplicationViewModel viewModel;

        EddyListAdapter.EddyListClickListener listener;

        public static EddyFragment getInstance(EddyListAdapter.EddyListClickListener clickListener)
        {
            EddyFragment fragment = new EddyFragment();
            fragment.listener = clickListener;
            return fragment;
        }


        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View v = inflater.inflate(R.layout.fragment_beacons, container,false);

            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
            adapter = new EddyListAdapter(listener, new ArrayList<>());

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            recyclerView.setAdapter(adapter);

            return v;
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        public void setItems(List<IEddystoneDevice> data) {
            adapter.addItems(data);
        }

    }
}
