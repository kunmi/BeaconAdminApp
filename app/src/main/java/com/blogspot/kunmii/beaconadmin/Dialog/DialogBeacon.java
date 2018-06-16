package com.blogspot.kunmii.beaconadmin.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.TextView;

import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.data.Beacon;

import org.json.JSONException;
import org.json.JSONObject;

public class DialogBeacon extends DialogFragment{

    Beacon beacon;

    TextView addressView;
    TextView proximityView;
    EditText referenceNumber;
    TextView txPower;


    //iBeacon
    TextView uuidView;
    TextView majorView;
    TextView minorview;


    //Eddystone
    TextView namespace;
    TextView instanceId;
    TextView telemetry;

    View v = null;

    DialogResultListener saveListener;


    public void setBeacon(Beacon beacon, DialogResultListener saveListener) {
        this.beacon = beacon;
        this.saveListener = saveListener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
            .setNegativeButton("Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    }
            );


        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String originalRef = beacon.getRef();
                String newRef = referenceNumber.getText().toString();

                    try {
                        JSONObject jsonObject = new JSONObject(beacon.getBeaconData());
                        jsonObject.put(Config.NETWORK_JSON_NODE.BEACON_REF, newRef);

                        beacon.setRef(newRef);
                        beacon.setBeaconData(jsonObject.toString());
                        saveListener.dialogResult(beacon);

                    }
                    catch (JSONException exp)
                    {
                        exp.printStackTrace();
                    }



            }
        });

        // call default fragment methods and set view for dialog
        if(v==null)
            v = onCreateView(getActivity().getLayoutInflater(), null, null);
        onViewCreated(v, null);
        dialogBuilder.setView(v);

        return dialogBuilder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(v==null) {

            if (beacon.getType().toLowerCase().equals("iBeacon".toLowerCase())) {
                v = inflater.inflate(R.layout.dialog_ibeaon, container, false);

                bindDefaultViews(v);

                uuidView = v.findViewById(R.id.uuid_text_view);
                majorView = v.findViewById(R.id.major_text_view);
                minorview = v.findViewById(R.id.minor_text_view);

                try {
                    JSONObject object = new JSONObject(beacon.getBeaconData());

                    if (object.has(Config.NETWORK_JSON_NODE.IBEACON_UUID)) {
                        uuidView.setText(String.valueOf(object.getString(Config.NETWORK_JSON_NODE.IBEACON_UUID)));
                    }

                    if (object.has(Config.NETWORK_JSON_NODE.IBEACON_MAJOR)) {
                        majorView.setText(String.valueOf(object.getString(Config.NETWORK_JSON_NODE.IBEACON_MAJOR)));
                    }

                    if (object.has(Config.NETWORK_JSON_NODE.IBEACON_MINOR)) {
                        minorview.setText(String.valueOf(object.getString(Config.NETWORK_JSON_NODE.IBEACON_MINOR)));
                    }

                } catch (JSONException exp) {
                    exp.printStackTrace();
                }

            } else {

                v = inflater.inflate(R.layout.dialog_eddystone, container, false);

                bindDefaultViews(v);

                namespace = v.findViewById(R.id.namespace_text_view);
                instanceId = v.findViewById(R.id.instanceid_text_view);
                telemetry = v.findViewById(R.id.telemetry_text_view);

                try {
                    JSONObject object = new JSONObject(beacon.getBeaconData());

                    if (object.has(Config.NETWORK_JSON_NODE.EDDY_NAMESPACEID)) {
                        namespace.setText(String.valueOf(object.getString(Config.NETWORK_JSON_NODE.EDDY_NAMESPACEID)));
                    }

                    if (object.has(Config.NETWORK_JSON_NODE.EDDY_INSTANCEID)) {
                        instanceId.setText(String.valueOf(object.getString(Config.NETWORK_JSON_NODE.EDDY_INSTANCEID)));
                    }

                    if (object.has(Config.NETWORK_JSON_NODE.EDDY_TELEMETRY)) {
                        telemetry.setText(String.valueOf(object.getString(Config.NETWORK_JSON_NODE.EDDY_TELEMETRY)));
                    }

                } catch (JSONException exp) {
                    exp.printStackTrace();
                }


            }
        }

        return v;
    }


    void bindDefaultViews(View v)
    {
        referenceNumber = v.findViewById(R.id.ref_number);
        txPower = v.findViewById(R.id.txpower_text_view);

        String ref = beacon.getRef();

        if(ref!=null)
        {
            if(ref.equals("null"))
                ref="";
            referenceNumber.setText(String.valueOf(ref));

        }


        txPower.setText(String.valueOf(beacon.getTxpower()));
    }


    public interface DialogResultListener
    {
        void dialogResult(Beacon beacon);
    }

}
