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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.data.Beacon;

import org.json.JSONException;
import org.json.JSONObject;

public class DialogAddBeacon extends DialogFragment{


    RadioButton iBeaconRadioButton;
    RadioButton eddyRadioButton;

    //iBeacon
    EditText input_uuid;
    EditText input_major;
    EditText input_minor;
    LinearLayout iBeaconLayout;

    //Eddystone
    EditText input_namespace;
    EditText input_uniqueid;
    LinearLayout eddystoneLayout;

    //Both
    EditText input_ref;

    View v = null;

    DialogResultListener saveListener;

    public void setSaveListener(DialogResultListener listener)
    {
        saveListener = listener;
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
                if(saveListener!=null)
                {
                    try {
                        JSONObject beaconJson = null;
                        if (iBeaconRadioButton.isChecked()) {
                            beaconJson = Helpers.createIBeaconJSON(getActivity().getApplication());

                            beaconJson.put(Config.NETWORK_JSON_NODE.IBEACON_UUID, input_uuid.getText().toString());
                            beaconJson.put(Config.NETWORK_JSON_NODE.IBEACON_MAJOR, input_major.getText().toString());
                            beaconJson.put(Config.NETWORK_JSON_NODE.IBEACON_MINOR, input_minor.getText().toString());
                        }
                        else {
                            beaconJson = Helpers.createEddystoneJson(getActivity().getApplication());

                            beaconJson.put(Config.NETWORK_JSON_NODE.EDDY_NAMESPACEID, input_namespace.getText().toString());
                            beaconJson.put(Config.NETWORK_JSON_NODE.EDDY_INSTANCEID, input_uniqueid.getText().toString());
                        }


                        beaconJson.put(Config.NETWORK_JSON_NODE.BEACON_REF, input_ref.getText().toString());

                        saveListener.dialogResult(beaconJson.toString());
                        dismiss();
                        return;

                    }
                    catch (JSONException exp)
                    {
                        exp.printStackTrace();
                    }
                }

                saveListener.dialogResult(null);
                dismiss();
                return;


            }
        });

        dialogBuilder.setTitle("Add Beacon");

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

                v = inflater.inflate(R.layout.dialog_add_beacon_manually, container, false);

                iBeaconRadioButton = v.findViewById(R.id.radio_ibeacon);
                eddyRadioButton = v.findViewById(R.id.radio_eddy);

                input_uuid = v.findViewById(R.id.input_uuid);
                input_major = v.findViewById(R.id.input_major);
                input_minor = v.findViewById(R.id.input_minor);

                iBeaconLayout = v.findViewById(R.id.ibeacon_layout);


                input_namespace = v.findViewById(R.id.input_namespace);
                input_uniqueid = v.findViewById(R.id.input_instanceid);

                eddystoneLayout = v.findViewById(R.id.eddy_layout);

                input_ref = v.findViewById(R.id.input_ref);

                View.OnClickListener radioClickListener = this::onRadioButtonClicked;

                iBeaconRadioButton.setOnClickListener(radioClickListener);
                eddyRadioButton.setOnClickListener(radioClickListener);
        }

        return v;
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_ibeacon:
                if (checked)
                {
                    eddystoneLayout.setVisibility(View.GONE);
                    iBeaconLayout.setVisibility(View.VISIBLE);
                }
                    // Pirates are the best
                    break;
            case R.id.radio_eddy:
                if (checked)
                {
                    eddystoneLayout.setVisibility(View.VISIBLE);
                    iBeaconLayout.setVisibility(View.GONE);
                }
                    // Ninjas rule
                    break;
        }
    }


    public interface DialogResultListener
    {
        void dialogResult(String beacon);
    }
}
