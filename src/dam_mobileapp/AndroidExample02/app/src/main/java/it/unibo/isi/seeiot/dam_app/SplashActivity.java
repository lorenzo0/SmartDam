package it.unibo.isi.seeiot.dam_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import it.unibo.isi.seeiot.dam_app.bluetooth.Bluetooth;
import it.unibo.isi.seeiot.dam_app.netutils.HTTPRequests;
import it.unibo.isi.seeiot.dam_app.netutils.HttpResponse;
import it.unibo.isi.seeiot.dam_app.utils.global;

public class SplashActivity extends AppCompatActivity {
    UserInterface userInterface;
    Bluetooth bluetoothConn;
    HTTPRequests httpRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInterface = new UserInterface();

        bluetoothConn = new Bluetooth();
        httpRequests = new HTTPRequests();

        LoadRequestAndBTConn loadingWhileSS = new LoadRequestAndBTConn();
        loadingWhileSS.execute();
    }

    private class LoadRequestAndBTConn extends AsyncTask<Void, Void, Boolean> {

        Intent intent = new Intent(SplashActivity.this, UserInterface.class);

        public LoadRequestAndBTConn(){}

        @Override
        protected Boolean doInBackground(Void... params) {
            needTimeToLoad();
            return true;
        }

        protected void onPostExecute(Boolean ended) {
            if(ended) {
                startActivity(intent);
                finish();
            }
        }
    }

    protected void needTimeToLoad(){
        connectBT();
    }

    protected void connectBT(){
        try {
            bluetoothConn.connectToBTServer(getApplicationContext());
        } catch (unibo.btlib.exceptions.BluetoothDeviceNotFound bluetoothDeviceNotFound) {
            bluetoothDeviceNotFound.printStackTrace();
        }
    }
}
