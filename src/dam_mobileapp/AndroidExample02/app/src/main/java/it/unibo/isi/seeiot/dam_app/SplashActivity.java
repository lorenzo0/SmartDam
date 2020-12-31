package it.unibo.isi.seeiot.dam_app;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

import it.unibo.isi.seeiot.dam_app.bluetooth.Bluetooth;
import it.unibo.isi.seeiot.dam_app.netutils.HTTPRequests;
import it.unibo.isi.seeiot.dam_app.utils.global;
import it.unibo.isi.seeiot.dam_app.utils.handlerAlert;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;

public class SplashActivity extends AppCompatActivity {
    UserInterface userInterface;
    HTTPRequests httpRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInterface = new UserInterface();

        httpRequests = new HTTPRequests();

        Intent intent = new Intent(SplashActivity.this, UserInterface.class);

        Thread background = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    needTimeToLoad();
                }catch (Throwable t)
                {
                    System.err.println("Thread Exception IN Splash Screen->" + t.toString());
                }finally {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("httpReq", httpRequests);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
        background.start();
    }

    protected void needTimeToLoad(){
        httpRequests.tryHttpGetDataSync();
    }

}
