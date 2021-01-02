package it.unibo.isi.seeiot.dam_app;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.TimerTask;

import it.unibo.isi.seeiot.dam_app.bluetooth.Bluetooth;
import it.unibo.isi.seeiot.dam_app.netutils.HTTPRequests;
import it.unibo.isi.seeiot.dam_app.netutils.Http;
import it.unibo.isi.seeiot.dam_app.utils.global;

public class CountDown extends AsyncTask<Object, Integer, Void> {

    HTTPRequests httpRequests = new HTTPRequests();
    View oldView;
    Bluetooth bluetooth;


    public CountDown( View oldView, Bluetooth bluetooth){
        this.oldView = oldView;
        this.bluetooth = bluetooth;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("TAG-XX", "Starting...");
    }

    @Override
    protected Void doInBackground(Object... objs) {
        Log.d("TAG-XX", "Requesting...");
        Http.get(global.url, response -> {
            if(response.code() == HttpURLConnection.HTTP_OK){
                //httpRequests.fillArrayWithNewData(response);
                Log.d("HTTP-GET", Integer.toString(httpRequests.getDataReceiveds().size()));
            }
        });
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... value) {
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        /*if(httpRequests.getDataReceiveds().size() != 0) {
            Log.d("TAG-XX", "finito1!");
            httpRequests.modifyItemsOnUI(oldView, bluetooth);
        }
        Log.d("TAG-XX", "finito!");*/
    }
}