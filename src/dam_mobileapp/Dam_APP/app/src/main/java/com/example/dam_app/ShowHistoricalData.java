package com.example.dam_app;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dam_app.Bluetooth.Bluetooth;
import com.example.dam_app.NetUtils.DataReceived;
import com.example.dam_app.NetUtils.HTTPRequest;
import com.example.dam_app.Utils.StorageDataAdapter;

import java.util.ArrayList;

import unibo.btlib.ConnectToBluetoothServerTask;

public class ShowHistoricalData extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DataReceived> dataReceiveds = new ArrayList<DataReceived>();
    HTTPRequest httpRequests;
    Bluetooth bluetoothConn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_history);

        recyclerView = (RecyclerView) findViewById(R.id.list_data_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dataReceiveds = (ArrayList<DataReceived>) getIntent().getSerializableExtra("httpReq");
            //bluetoothConn = (Bluetooth) getIntent().getSerializableExtra("bluetooth");
        }

        recyclerView.setAdapter(new StorageDataAdapter(dataReceiveds));

        initUI();
    }

    private void initUI() {
        if(dataReceiveds.size() != 0)
            findViewById(R.id.motivation_message).setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        Intent intent = super.getParentActivityIntent();
        /*Bundle bundle = new Bundle();
        bundle.putSerializable("bluetooth", bluetoothConn);
        intent.putExtras(bundle);*/
        return intent;
    }
}
