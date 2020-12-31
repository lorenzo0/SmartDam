package it.unibo.isi.seeiot.dam_app.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import it.unibo.isi.seeiot.dam_app.utils.global;
import unibo.btlib.BluetoothChannel;
import unibo.btlib.BluetoothUtils;
import unibo.btlib.ConnectToBluetoothServerTask;
import unibo.btlib.ConnectionTask;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;

public class Bluetooth extends AppCompatActivity {

    public BluetoothChannel btChannel;

    public Bluetooth(){}

    public void connectToBTServer(Context context) throws BluetoothDeviceNotFound {
        final BluetoothDevice serverDevice = BluetoothUtils.getPairedDeviceByName(global.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);
        /*
         *  decido di non utilizzare il generatore (randomica oppure a partire da una stringa) di UUID
         *  dato che utilizzo il default UUID per gli embeddedDevice. Questo mi è possibile perchè i due device
         *  che devono comunicare NON possono esplicitare e quindi condividere lo stesso UUID.
         *  L'UUID utilizzato è presentato come convenzione ad-hoc per embedded system
        */
        final UUID uuid = BluetoothUtils.getEmbeddedDeviceDefaultUuid();

        new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {

                Toast.makeText(context,"Connected to server on device "+serverDevice.getName(), Toast.LENGTH_LONG).show();
                btChannel = channel;
            }

            @Override
            public void onConnectionCanceled() {
                Toast.makeText(context,"Unable to connect, device " + global.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME + " not found!", Toast.LENGTH_LONG).show();
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