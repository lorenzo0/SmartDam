package it.unibo.isi.seeiot.dam_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cit.unibo.isi.seeiot.dam_app.R;
import it.unibo.isi.seeiot.dam_app.bluetooth.Bluetooth;
import it.unibo.isi.seeiot.dam_app.netutils.DataReceived;
import it.unibo.isi.seeiot.dam_app.netutils.HTTPRequests;
import unibo.btlib.ConnectToBluetoothServerTask;

public class ShowHistoricalData extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<DataReceived> dataReceiveds = new ArrayList<DataReceived>();
    HTTPRequests httpRequests;
    Bluetooth bluetoothConn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_history);

        recyclerView = (RecyclerView) findViewById(R.id.list_data_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            httpRequests = (HTTPRequests) getIntent().getSerializableExtra("httpReq");
            bluetoothConn = (Bluetooth) getIntent().getSerializableExtra("bluetooth");
        }

        recyclerView.setAdapter(new StorageDataAdapter(httpRequests.getDataReceiveds()));

        // The callback can be enabled or disabled here or in handleOnBackPressed()

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
        Bundle bundle = new Bundle();
        bundle.putSerializable("httpReq", httpRequests);
        bundle.putSerializable("bluetooth", bluetoothConn);
        intent.putExtras(bundle);
        return intent;
    }
}
