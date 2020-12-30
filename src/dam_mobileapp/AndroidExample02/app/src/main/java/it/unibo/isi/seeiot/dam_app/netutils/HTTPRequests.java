package it.unibo.isi.seeiot.dam_app.netutils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import cit.unibo.isi.seeiot.dam_app.R;
import it.unibo.isi.seeiot.dam_app.bluetooth.Bluetooth;
import it.unibo.isi.seeiot.dam_app.utils.global;

public class HTTPRequests implements Serializable {

    DataReceived sampleData;
    ArrayList<DataReceived> dataReceiveds = new ArrayList<DataReceived>();

    public HTTPRequests(){
    }

    public void tryHttpPost(String percentageOpening, Context context, Bluetooth bluetoothConn) throws JSONException {

        int finalData = Integer.valueOf(percentageOpening.replace("%", ""));

        Log.d("SendingMessage", "Distance: "+ global.currentLevel + ", State: "+ global.currentState + ", Open-Angle: " +finalData);


        final String content = new JSONObject()
                .put("distance", global.currentLevel)
                .put("state", global.currentState)
                .put("sender", "APP")
                .put("open-angle", finalData).toString();

        /* Qui bisogna effettuare un controllo in più. Se non c'è la connessione bluethoot, non mando neanche quello http */
        Http.post(global.url, content.getBytes(), response -> {
            if(response.code() == HttpURLConnection.HTTP_OK) {
                Toast.makeText(context, "Dati inviati correttamente al server!", Toast.LENGTH_LONG).show();
                //bluetoothConn.sendMessage("New opening: " + finalData);
            }else
                Toast.makeText(context,"Si è verificato un problema, i dati NON sono stati inviati al server!", Toast.LENGTH_LONG).show();
        });
    }

    public void tryHttpGetUI(View oldView){

        Http.get(global.url, response -> {
            if(response.code() == HttpURLConnection.HTTP_OK){
                try {
                    JSONArray array = new JSONArray(response.contentAsString());
                    Log.d("resp1", array.toString());

                    /*for(int i=0;i<array.length();i++) Questo per avere tutti i record!
                    {
                        String time = array.getJSONObject(i).getString("time");
                        String value = array.getJSONObject(i).getString("value");
                        String place = array.getJSONObject(i).getString("place");

                        Toast.makeText(getApplicationContext(),time+" - "+value+" - "+place,Toast.LENGTH_LONG).show();
                    }*/

                    for(int i=0;i<array.length();i++)
                    {
                        sampleData = new DataReceived(Float.valueOf(array.getJSONObject(i).getString("distance")),
                                array.getJSONObject(i).getString("state"),
                                array.getJSONObject(i).getString("time"),
                                array.getJSONObject(i).getInt("open-angle"));

                        dataReceiveds.add(sampleData);
                    }

                    ((TextView)oldView.findViewById(R.id.angle)).setText(array.getJSONObject(0).getString("open-angle"));
                    ((TextView)oldView.findViewById(R.id.level)).setText(array.getJSONObject(0).getString("distance"));
                    ((TextView)oldView.findViewById(R.id.state)).setText(array.getJSONObject(0).getString("state"));
                    ((TextView)oldView.findViewById(R.id.timestamp)).setText("Ultima rilevazione: \n " +array.getJSONObject(0).getString("time"));

                    global.currentState = array.getJSONObject(0).getString("state");
                    global.currentLevel = Float.valueOf(array.getJSONObject(0).getString("distance"));

                    switch(global.currentState){
                        case "ALLARM":
                            ((TextView)oldView.findViewById(R.id.state)).setTextColor(Color.parseColor("#ff0000"));

                            ((TextView)oldView.findViewById(R.id.level_water)).setVisibility(View.VISIBLE);
                            ((TextView)oldView.findViewById(R.id.level)).setVisibility(View.VISIBLE);

                            ((TextView)oldView.findViewById(R.id.open_angle_dam)).setVisibility(View.VISIBLE);
                            ((TextView)oldView.findViewById(R.id.angle)).setVisibility(View.VISIBLE);
                            break;

                        case "PRE-ALLARM":
                            ((TextView)oldView.findViewById(R.id.state)).setTextColor(Color.parseColor("#ff8100"));

                            ((TextView)oldView.findViewById(R.id.level_water)).setVisibility(View.VISIBLE);
                            ((TextView)oldView.findViewById(R.id.level)).setVisibility(View.VISIBLE);

                            ((TextView)oldView.findViewById(R.id.open_angle_dam)).setVisibility(View.GONE);
                            ((TextView)oldView.findViewById(R.id.angle)).setVisibility(View.GONE);
                            break;

                        case "NORMAL":
                            ((TextView)oldView.findViewById(R.id.state)).setTextColor(Color.parseColor("#00ff11"));

                            ((TextView)oldView.findViewById(R.id.level_water)).setVisibility(View.GONE);
                            ((TextView)oldView.findViewById(R.id.level)).setVisibility(View.GONE);

                            ((TextView)oldView.findViewById(R.id.open_angle_dam)).setVisibility(View.GONE);
                            ((TextView)oldView.findViewById(R.id.angle)).setVisibility(View.GONE);

                            break;
                    }

                    switch(Integer.parseInt(array.getJSONObject(array.length()-1).getString("open-angle"))){
                        case 0:
                            ((ImageView)oldView.findViewById(R.id.image_dam)).setImageResource(R.drawable.empty_dam);
                            break;

                        case 100:
                            ((ImageView)oldView.findViewById(R.id.image_dam)).setImageResource(R.drawable.full_dam);
                            break;

                        default:
                            ((ImageView)oldView.findViewById(R.id.image_dam)).setImageResource(R.drawable.working_dam);
                            break;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void tryHttpGetUIX(){

        Http.get(global.url, response -> {
            if(response.code() == HttpURLConnection.HTTP_OK){
                try {
                    JSONArray array = new JSONArray(response.contentAsString());

                    global.currentState = array.getJSONObject(0).getString("state");
                    global.currentLevel = Float.valueOf(array.getJSONObject(0).getString("distance"));

                    Log.d("resp1", array.toString());

                    for(int i=0;i<array.length();i++)
                    {
                        sampleData = new DataReceived(Float.valueOf(array.getJSONObject(i).getString("distance")),
                                array.getJSONObject(i).getString("state"),
                                array.getJSONObject(i).getString("time"),
                                array.getJSONObject(i).getInt("open-angle"));

                        dataReceiveds.add(sampleData);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void tryHttpGetHData(){

        Http.get(global.url, response -> {
            if (response.code() == HttpURLConnection.HTTP_OK) {
                try {
                    JSONArray array = new JSONArray(response.contentAsString());
                    Log.d("resp1-HD", array.toString());

                    global.currentState = array.getJSONObject(0).getString("state");
                    global.currentLevel = Float.valueOf(array.getJSONObject(0).getString("distance"));

                    for(int i=0;i<array.length();i++)
                    {
                        sampleData = new DataReceived(Float.valueOf(array.getJSONObject(i).getString("distance")),
                                                        array.getJSONObject(i).getString("state"),
                                                        array.getJSONObject(i).getString("time"),
                                                        array.getJSONObject(i).getInt("open-angle"));

                        dataReceiveds.add(sampleData);
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Log.d("resp1-HD", Integer.toString(dataReceiveds.size()));
    }

    public ArrayList<DataReceived> getDataReceiveds() {
        return dataReceiveds;
    }
}
