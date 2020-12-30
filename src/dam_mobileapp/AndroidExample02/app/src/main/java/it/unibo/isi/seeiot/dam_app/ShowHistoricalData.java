package it.unibo.isi.seeiot.dam_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cit.unibo.isi.seeiot.dam_app.R;
import it.unibo.isi.seeiot.dam_app.netutils.DataReceived;
import it.unibo.isi.seeiot.dam_app.netutils.HTTPRequests;
import unibo.btlib.ConnectToBluetoothServerTask;

public class ShowHistoricalData extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DataReceived> dataReceiveds = new ArrayList<DataReceived>();
    HTTPRequests httpRequests;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_history);

        recyclerView = (RecyclerView) findViewById(R.id.list_data_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<DataReceived> object = (ArrayList<DataReceived>) args.getSerializable("ARRAYLIST");

        recyclerView.setAdapter(new StorageDataAdapter(object));

        initUI();
    }

    private void initUI() {
        if(dataReceiveds.size() != 0)
            findViewById(R.id.motivation_message).setVisibility(View.GONE);
    }
}
