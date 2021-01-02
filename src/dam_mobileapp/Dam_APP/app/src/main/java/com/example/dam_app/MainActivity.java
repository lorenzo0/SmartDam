package com.example.dam_app;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.dam_app.Bluetooth.Bluetooth;
import com.example.dam_app.NetUtils.HTTPRequest;
import com.example.dam_app.NetUtils.ModifyUI;
import com.example.dam_app.Utils.AlertHandler;
import com.example.dam_app.Utils.Global.Global;

public class MainActivity extends AppCompatActivity {

    HTTPRequest httpRequest;
    CountDownTimer countDownTimer;
    Switch modalitySwitch;
    boolean checkOnCreate;
    Bluetooth bluetoothConn;
    AlertHandler alerHandler;
    View currentView;
    ModifyUI modifyItemsOnUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createCountDown(5000);
        httpRequest = new HTTPRequest();
        modifyItemsOnUI = new ModifyUI();

        bluetoothConn = new Bluetooth();
        alerHandler = new AlertHandler();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            httpRequest = (HTTPRequest) getIntent().getSerializableExtra("httpReq");
        else
            httpRequest = new HTTPRequest();

        /*if(getIntent().getSerializableExtra("bluetooth") != null)
            bluetoothConn = (Bluetooth) getIntent().getSerializableExtra("bluetooth");
        else {
            bluetoothConn = new Bluetooth();
        }*/

        modalitySwitch = (Switch) findViewById(R.id.switch1);
        currentView = findViewById(R.id.angle).getRootView();
        checkOnCreate = true;

        initUI();

        if (httpRequest.getStorageData().size() != 0)
            modifyItemsOnUI.modifyItemsOnUI(currentView, bluetoothConn, httpRequest);
        else
            httpRequest.tryHTTPGet(MainActivity.this, currentView, bluetoothConn, httpRequest);

    }

    /*
     *  Handling Life-Cicly Activity
     */

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(checkOnCreate))
            httpRequest.tryHTTPGet(MainActivity.this, currentView, bluetoothConn, httpRequest);
        checkOnCreate = false;

        requestOpeningBT();
        if(bluetoothConn.btChannel == null)
            connectBT();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(bluetoothConn.btChannel != null)
            bluetoothConn.closeConnection();
        checkOnCreate = true;
    }

    /*
     * OnCreate di UserInterface viene invocato anche nel caso si passi da ShowHistoricalData,
     * quindi se non riesce a mantenere la connessione, allora ri-chiedo la connessione
     * all'embedded system.
     */

    protected void requestOpeningBT(){
        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && !btAdapter.isEnabled())
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), Global.bluetooth.ENABLE_BT_REQUEST);

    }

    protected void connectBT(){
        try {
            if(bluetoothConn.btChannel==null)
                bluetoothConn.connectToBTServer(getApplicationContext());
        } catch (unibo.btlib.exceptions.BluetoothDeviceNotFound bluetoothDeviceNotFound) {
            bluetoothDeviceNotFound.printStackTrace();
        }
    }

    private void initUI() {
        modalitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (Global.currentState.equals("ALLARM")) {
                        if(bluetoothConn.btChannel != null) {
                            bluetoothConn.sendMessage("MOD-OP");
                            Global.mod_op = true;
                            alerHandler.showAlert(MainActivity.this, currentView, httpRequest, bluetoothConn);
                        }else
                            alerHandler.showNoBTAlert(MainActivity.this);

                    }else{
                        alerHandler.showWarningAlert(MainActivity.this);
                        modalitySwitch.setChecked(false);
                    }
                }
            }
        });

        findViewById(R.id.info_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowHistoricalData.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("httpReq", httpRequest.getStorageData());
            intent.putExtras(bundle);
            if(bluetoothConn.btChannel != null)
                bluetoothConn.closeConnection();
            startActivity(intent);
        });
    }


    public void createCountDown(long numberMilliSec) {
        countDownTimer = new CountDownTimer(numberMilliSec, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        httpRequest.tryHTTPGet(MainActivity.this, currentView, bluetoothConn, httpRequest);
                        createCountDown(5000);
                    }
                });
            }
        };
        countDownTimer.start();
    }
}