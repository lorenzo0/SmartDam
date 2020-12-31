package it.unibo.isi.seeiot.dam_app;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Objects;

import cit.unibo.isi.seeiot.dam_app.R;
import it.unibo.isi.seeiot.dam_app.bluetooth.Bluetooth;
import it.unibo.isi.seeiot.dam_app.netutils.HTTPRequests;
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

    /*
    *  Handling Life-Cicly Activity
    */

    @Override
    protected void onStart() {
        super.onStart();
        httpRequests.tryHttpGetHData(currentView, bluetoothConn);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(checkOnCreate))
            httpRequests.tryHttpGetHData(currentView, bluetoothConn);
        checkOnCreate = false;
        createCountDown(5000);
        connectBT();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bluetoothConn.closeConnection();
        checkOnCreate = true;
    }

    private void initUI() {
        modalitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (global.currentState.equals("ALLARM")) {
                        bluetoothConn.sendMessage("MOD-OP");
                        global.mod_op = true;
                        alerHandler.showAlert(UserInterface.this, currentView, httpRequests, bluetoothConn);
                    }else{
                        alerHandler.showWarningAlert(UserInterface.this);
                        modalitySwitch.setChecked(false);
                    }
                }
            }
        });

        findViewById(R.id.info_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowHistoricalData.class);
            Bundle args = new Bundle();
            args.putSerializable("ARRAYLIST",(Serializable)httpRequests.getDataReceiveds());
            intent.putExtra("BUNDLE",args);
            startActivity(intent);
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
                        Log.d("PROVA-TEST", Boolean.toString(global.mod_op));
                        if(!(global.mod_op))
                            httpRequests.tryHttpGetHData(currentView, bluetoothConn);
                        createCountDown(numberMilliSec);
                    }
                });
            }
        };

        countDown.start();
    }

}
