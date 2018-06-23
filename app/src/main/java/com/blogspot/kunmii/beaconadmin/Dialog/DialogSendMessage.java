package com.blogspot.kunmii.beaconadmin.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.blogspot.kunmii.beaconadmin.Config;
import com.blogspot.kunmii.beaconadmin.Helpers.Helpers;
import com.blogspot.kunmii.beaconadmin.R;
import com.blogspot.kunmii.beaconadmin.data.Beacon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class DialogSendMessage extends DialogFragment {

    EditText input_title;
    EditText input_body;

    TextView beacon_destination_view;

    List<Beacon> beacons = new ArrayList<>();


    View v = null;

    DialogResultListener saveListener;

    public void setData(DialogResultListener listener, List<Beacon> beacons)
    {
        saveListener = listener;
        this.beacons.addAll(beacons);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );


        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(saveListener!=null)
                {
                        saveListener.dialogResult(beacons, input_title.getText().toString(), input_body.getText().toString());
                        dismiss();
                        return;
                }

                dismiss();
                return;


            }
        });

        dialogBuilder.setTitle("Send Message");

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

            v = inflater.inflate(R.layout.dialog_message, container, false);

            input_title = v.findViewById(R.id.input_title);
            input_body = v.findViewById(R.id.input_text);

            beacon_destination_view = v.findViewById(R.id.destination_view);


            String destination = "";

            for(int i=0; i < beacons.size(); i++)
            {
                Beacon b = beacons.get(i);

                    if (b.getRef() != null && !b.getRef().equals("") && !b.getRef().equals("null")) {

                        if(i<beacons.size()-1) {
                            destination += b.getRef() + " , ";
                        }
                        else
                        {
                            destination += b.getRef();
                        }

                    }

            }


            beacon_destination_view.setText(destination);
        }

        return v;
    }



    public interface DialogResultListener
    {
        void dialogResult(List<Beacon> beacons, String title, String body);
    }
}