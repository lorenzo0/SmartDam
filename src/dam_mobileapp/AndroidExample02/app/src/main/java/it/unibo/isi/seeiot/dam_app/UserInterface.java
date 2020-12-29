package it.unibo.isi.seeiot.dam_app;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import cit.unibo.isi.seeiot.dam_app.R;
import it.unibo.isi.seeiot.dam_app.bluetooth.Bluetooth;
import it.unibo.isi.seeiot.dam_app.netutils.HTTPRequests;
import it.unibo.isi.seeiot.dam_app.netutils.Http;
import it.unibo.isi.seeiot.dam_app.utils.global;
import it.unibo.isi.seeiot.dam_app.utils.handlerAlert;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;

public class UserInterface extends AppCompatActivity {

    /*
     * Per testare l'applicazione si fa riferimento al servizio: http://dummy.restapiexample.com/
     */

    Switch modalitySwitch;
    boolean checkOnCreate;
    Bluetooth bluetoothConn;
    CountDownTimer countDown;
    handlerAlert alerHandler;
    HTTPRequests httpRequests;
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_interface);

        bluetoothConn = new Bluetooth();
        alerHandler = new handlerAlert();
        httpRequests = new HTTPRequests();

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && !btAdapter.isEnabled()) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), global.bluetooth.ENABLE_BT_REQUEST);
        }

        modalitySwitch = (Switch) findViewById(R.id.switch1);
        currentView = findViewById(R.id.angle).getRootView();
        checkOnCreate = true;

        initUI();
    }

    protected void connectBT(){
        try {
            bluetoothConn.connectToBTServer(UserInterface.this);
        } catch (BluetoothDeviceNotFound bluetoothDeviceNotFound) {
            bluetoothDeviceNotFound.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        httpRequests.tryHttpGet(currentView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!(checkOnCreate))
            httpRequests.tryHttpGet(currentView);
        checkOnCreate = false;
        createCountDown(5000);
    }

    private void initUI() {
        findViewById(R.id.info_button).setOnClickListener(v -> {
            final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        });

        modalitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (global.currentState.equals("ALLARM")) {
                        connectBT();
                        alerHandler.showAlert(UserInterface.this, currentView, httpRequests, bluetoothConn);
                    }else {
                        alerHandler.showWarningAlert(UserInterface.this);
                        modalitySwitch.setChecked(false);
                    }
                }
            }
        });
    }



    /* CountDown handle */

    public void createCountDown(long numberMilliSec){
        countDown = new CountDownTimer(numberMilliSec, 1000) {

            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"Finito!",Toast.LENGTH_SHORT).show();
                        httpRequests.tryHttpGet(currentView);
                        createCountDown(numberMilliSec);
                    }
                });
            }
        };

        countDown.start();
    }

}
