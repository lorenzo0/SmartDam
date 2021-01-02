package com.example.dam_app.NetUtils;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dam_app.Bluetooth.Bluetooth;
import com.example.dam_app.R;
import com.example.dam_app.Utils.Global.Global;

import java.io.Serializable;
import java.util.ArrayList;

public class ModifyUI implements Serializable {

    ArrayList<DataReceived> dataReceiveds;

    public void modifyItemsOnUI(View oldView, Bluetooth bluetooth, HTTPRequest httpRequest) {
        dataReceiveds = httpRequest.getStorageData();

        if(bluetooth.btChannel != null)
            bluetooth.sendMessage(dataReceiveds.get(0).getState());

        Log.d("PROVA-HTTP", dataReceiveds.get(0).getTime());

        ((TextView) oldView.findViewById(R.id.angle)).setText(Integer.toString(dataReceiveds.get(0).getAngle()));
        ((TextView) oldView.findViewById(R.id.level)).setText(Float.toString(dataReceiveds.get(0).getDistance()));
        ((TextView) oldView.findViewById(R.id.state)).setText(dataReceiveds.get(0).getState());
        ((TextView) oldView.findViewById(R.id.timestamp)).setText("Ultima rilevazione: \n " + dataReceiveds.get(0).getTime());

        switch (Global.currentState) {
            case "ALLARM":
                ((TextView) oldView.findViewById(R.id.state)).setTextColor(Color.parseColor("#ff0000"));

                ((TextView) oldView.findViewById(R.id.level_water)).setVisibility(View.VISIBLE);
                ((TextView) oldView.findViewById(R.id.level)).setVisibility(View.VISIBLE);

                ((TextView) oldView.findViewById(R.id.open_angle_dam)).setVisibility(View.VISIBLE);
                ((TextView) oldView.findViewById(R.id.angle)).setVisibility(View.VISIBLE);
                break;

            case "PRE-ALLARM":
                ((TextView) oldView.findViewById(R.id.state)).setTextColor(Color.parseColor("#ff8100"));

                ((TextView) oldView.findViewById(R.id.level_water)).setVisibility(View.VISIBLE);
                ((TextView) oldView.findViewById(R.id.level)).setVisibility(View.VISIBLE);

                ((TextView) oldView.findViewById(R.id.open_angle_dam)).setVisibility(View.GONE);
                ((TextView) oldView.findViewById(R.id.angle)).setVisibility(View.GONE);
                break;

            case "NORMAL":
                ((TextView) oldView.findViewById(R.id.state)).setTextColor(Color.parseColor("#00ff11"));

                ((TextView) oldView.findViewById(R.id.level_water)).setVisibility(View.GONE);
                ((TextView) oldView.findViewById(R.id.level)).setVisibility(View.GONE);

                ((TextView) oldView.findViewById(R.id.open_angle_dam)).setVisibility(View.GONE);
                ((TextView) oldView.findViewById(R.id.angle)).setVisibility(View.GONE);

                break;
        }

        switch (Integer.valueOf(dataReceiveds.get(0).getAngle())) {
            case 0:
                ((ImageView) oldView.findViewById(R.id.image_dam)).setImageResource(R.drawable.empty_dam);
                break;

            case 100:
                ((ImageView) oldView.findViewById(R.id.image_dam)).setImageResource(R.drawable.full_dam);
                break;

            default:
                ((ImageView) oldView.findViewById(R.id.image_dam)).setImageResource(R.drawable.working_dam);
                break;
        }
    }
}
