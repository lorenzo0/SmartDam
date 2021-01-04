package com.example.dam_app.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_app.Utils.Global.Global;

import java.io.Serializable;
import java.util.UUID;

import unibo.btlib.BluetoothChannel;
import unibo.btlib.BluetoothUtils;
import unibo.btlib.ConnectToBluetoothServerTask;
import unibo.btlib.ConnectionTask;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;

/*
*   La classe Bluetooth sfrutta le librerie presenti in Java/com/bt-lib.
*   Grazie a queste, è possibile istanziare un nuovo oggetto di tipo BluetoothChannel
*   e sfruttare tutte le sue potenzialità.
*
*   Decido di non invocare il generatore (randomico oppure a partire da una stringa) di UUID
*   dato che utilizzo il default UUID per gli embeddedDevice. Questo mi è possibile perchè i due device
*   che devono comunicare NON possono esplicitare e quindi condividere lo stesso UUID.
*   L'UUID utilizzato è presentato come convenzione ad-hoc per embedded system.
*/

public class Bluetooth extends AppCompatActivity implements Serializable {

    public BluetoothChannel btChannel;
    public Bluetooth(){}

    public void connectToBTServer(Context context) throws BluetoothDeviceNotFound {
        final BluetoothDevice serverDevice = BluetoothUtils.getPairedDeviceByName(Global.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);
        final UUID uuid = BluetoothUtils.getEmbeddedDeviceDefaultUuid();

        new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {
                Toast.makeText(context,"Connected to server on device "+serverDevice.getName(), Toast.LENGTH_LONG).show();
                btChannel = channel;
            }

            @Override
            public void onConnectionCanceled() {
                Toast.makeText(context,"Unable to connect, device " + Global.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME + " not found!", Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    public void closeConnection(){
        btChannel.close();
    }
    public void sendMessage(String message){
        btChannel.sendMessage(message);
    }
}