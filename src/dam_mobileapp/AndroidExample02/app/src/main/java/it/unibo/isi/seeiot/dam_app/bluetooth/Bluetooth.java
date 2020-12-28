package it.unibo.isi.seeiot.dam_app.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

import cit.unibo.isi.seeiot.dam_app.R;
import it.unibo.isi.seeiot.dam_app.utils.global;
import unibo.btlib.BluetoothChannel;
import unibo.btlib.BluetoothUtils;
import unibo.btlib.ConnectToBluetoothServerTask;
import unibo.btlib.ConnectionTask;
import unibo.btlib.RealBluetoothChannel;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;

public class Bluetooth extends AppCompatActivity {

    private BluetoothChannel btChannel;

    public void connectToBTServer(Context context) throws BluetoothDeviceNotFound {
        final BluetoothDevice serverDevice = BluetoothUtils.getPairedDeviceByName(global.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);

        // !!! UTILIZZARE IL CORRETTO VALORE DI UUID
        final UUID uuid = BluetoothUtils.getEmbeddedDeviceDefaultUuid();
        //final UUID uuid = BluetoothUtils.generateUuidFromString(C.bluetooth.BT_SERVER_UUID);

        new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {

                Toast.makeText(context,"Connected to server on device "+serverDevice.getName(), Toast.LENGTH_LONG).show();

                btChannel = channel;
                btChannel.registerListener(new RealBluetoothChannel.Listener() {
                    @Override
                    public void onMessageReceived(String receivedMessage) {
                        /*((TextView) findViewById(R.id.chatLabel)).append(String.format("> [RECEIVED from %s] %s\n",
                                btChannel.getRemoteDeviceName(),
                                receivedMessage));>*/
                    }

                    @Override
                    public void onMessageSent(String sentMessage) {
                        /*((TextView) findViewById(R.id.chatLabel)).append(String.format("> [SENT to %s] %s\n",
                                btChannel.getRemoteDeviceName(),
                                sentMessage));*/
                    }
                });
            }

            @Override
            public void onConnectionCanceled() {
                Toast.makeText(context,"Unable to connect, device " + global.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME + " not found!", Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

    public void sendMessage(String message){
        btChannel.sendMessage(message);
    }
}
