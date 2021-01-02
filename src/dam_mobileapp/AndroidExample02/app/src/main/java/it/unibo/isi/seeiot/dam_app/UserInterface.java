package it.unibo.isi.seeiot.dam_app;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

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
    handlerAlert alerHandler;
    HTTPRequests httpRequests;
    View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_interface);

        bluetoothConn = new Bluetooth();
        alerHandler = new handlerAlert();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            httpRequests = (HTTPRequests) getIntent().getSerializableExtra("httpReq");
        else
            httpRequests = new HTTPRequests();

        Log.d("TAG-XX", Integer.toString(httpRequests.getDataReceiveds().size()));

        if(getIntent().getSerializableExtra("bluetooth") != null)
            bluetoothConn = (Bluetooth) getIntent().getSerializableExtra("bluetooth");
        else {
            bluetoothConn = new Bluetooth();
            requestOpeningBT();
            //connectBT();
        }

        CountDown countDown = new CountDown(currentView, bluetoothConn);
        countDown.execute();

        modalitySwitch = (Switch) findViewById(R.id.switch1);
        currentView = findViewById(R.id.angle).getRootView();
        checkOnCreate = true;

        requestOpeningBT();
        initUI();

        if (httpRequests.getDataReceiveds().size() != 0)
            httpRequests.modifyItemsOnUI(currentView, bluetoothConn);
        else
            httpRequests.tryHttpGetHData(bluetoothConn);

    }

    /*
    *  Handling Life-Cicly Activity
    */

/*    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(checkOnCreate))
            httpRequests.tryHttpGetHData(bluetoothConn);
        //createCountDown(5000);
        //CountDownTimer();
        checkOnCreate = false;
    }
 */

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
        if (btAdapter != null && !btAdapter.isEnabled()) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), global.bluetooth.ENABLE_BT_REQUEST);
        }
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
                    if (global.currentState.equals("ALLARM")) {
                        if(bluetoothConn.btChannel != null) {
                            bluetoothConn.sendMessage("MOD-OP");
                            global.mod_op = true;
                            alerHandler.showAlert(UserInterface.this, currentView, httpRequests, bluetoothConn);
                        }else
                            alerHandler.showNoBTAlert(UserInterface.this);

                    }else{
                        alerHandler.showWarningAlert(UserInterface.this);
                        modalitySwitch.setChecked(false);
                    }
                }
            }
        });

        findViewById(R.id.info_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowHistoricalData.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("httpReq", httpRequests);
            bundle.putSerializable("bluetooth", bluetoothConn);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    /*public void CountDownTimer() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask AsynTaskData = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            CountDown countDown = new CountDown(currentView, bluetoothConn);
                            Toast.makeText(getApplicationContext(),"Finito!", Toast.LENGTH_SHORT).show();
                            countDown.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(AsynTaskData, 0, 5000);
    }*/
/*
    public void createCountDown(long numberMilliSec) {
        countDownTimer = new CountDownTimer(numberMilliSec, 1000) {

            public void onTick(long millisUntilFinished) {
                Log.d("CURRENT-TIME", Long.toString(millisUntilFinished));
                if(millisUntilFinished<1500)
                    countDown = new CountDown(httpRequests, currentView, bluetoothConn);
                    countDown.execute();
            }

            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //if(!(global.mod_op))
                        //httpRequests.tryHttpGetHData(currentView, bluetoothConn);

                        Toast.makeText(getApplicationContext(),"Finito!", Toast.LENGTH_SHORT).show();
                        createCountDown(numberMilliSec);
                    }
                });
            }
        };
        countDownTimer.start();
    }*/
}
