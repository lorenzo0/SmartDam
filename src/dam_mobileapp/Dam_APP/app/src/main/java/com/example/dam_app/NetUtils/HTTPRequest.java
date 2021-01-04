package com.example.dam_app.NetUtils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dam_app.Bluetooth.Bluetooth;
import com.example.dam_app.Utils.Global.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HTTPRequest implements Serializable {

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String url = "https://c5c6ce2e7eff.ngrok.io/api/data";
    String urlLog = "https://c5c6ce2e7eff.ngrok.io/api/log";
    ArrayList<DataReceived> storageData = new ArrayList<DataReceived>();
    DataReceived sampleData;
    ModifyUI modifyItemsOnUI;

    public HTTPRequest(){modifyItemsOnUI = new ModifyUI();}

    /*
    * Per effettuare la richiesta HTTP in GET e POST, viene utilizzata la classe Volley messa
    * a disposizione da Android. Questa, ha un funzionamento molto interessante. Tutte le richieste
    * che partono da questa classe sono ASINCRONE, indipendenti quindi dal mainThread.
    *
    * La sua efficienza nel rilasciare le informazioni è data da una Queue creata per memorizzare le
    * richieste che vengono fatte (Post e Get condividono la stessa RequestQueue).
    * Quando viene aggiunta una nuova richiesta, è prima analizzata dal cache tread (worker thread),
    * se questa è disponibile e può occuparsi della richiesta, la esegue subito.
    * Se il cache thread non è disponibile momentaneamente oppure non è in grado di soddisfare la richiesta,
    * allora quest'ultima viene aggiunta in una RequestQueue.
    *
    * Il primo network thread (worker thread) disponibile, prende in carico la richiesta e la manda in esecuzione.
    * Come prima menzionato, tutti gli worker thread sono indipendenti dal main thread.
    *
    * Alla conclusione della richiesta (sia in esito positivo, sia in esito negativo), sia il cache thread,
    * sia il network thread, consegna la risposta al main thread, che nel nostro caso, invocherà (con esito
    * positivo, modifyUI).
    *
    */

    public void tryHTTPGet(Context context, View oldView, Bluetooth bluetoothConn, HTTPRequest httpRequest) {

        mRequestQueue = Volley.newRequestQueue(context);
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    trySendLog(context, "ESP requested succesfully data by GET method.");
                    fillInArray(response, oldView, bluetoothConn, httpRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("TAG-ErrorResponseVolley","Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);
    }

    public void trySendLog(Context context, String message) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject postData = new JSONObject();
        try {
            postData.put("sender", "APP");
            postData.put("message", message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlLog, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void tryHTTPost(String percentageOpening, Bluetooth bluetooth, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int finalData = Integer.valueOf(percentageOpening.replace("%", ""));

        JSONObject postData = new JSONObject();
        try {
            postData.put("distance", Global.currentLevel);
            postData.put("state", Global.currentState);
            postData.put("sender", "APP");
            postData.put("open-angle", finalData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                trySendLog(context, "ESP added succesfully data by POST method.");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public String HTTPSync() {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuffer jsonText = new StringBuffer();

        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == 200)
                return readStream(connection.getInputStream());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String readStream(final InputStream in) throws IOException {
        final StringBuilder response = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    public void fillInArray(String response, View oldView, Bluetooth bluetoothConn, HTTPRequest httpRequest){
        try {
            storageData = new ArrayList<DataReceived>();
            JSONArray array = new JSONArray(response);

            Global.currentState = array.getJSONObject(0).getString("state");
            Global.currentLevel = Float.valueOf(array.getJSONObject(0).getString("distance"));

            for(int i=0;i<array.length();i++)
            {
                sampleData = new DataReceived(Float.valueOf(array.getJSONObject(i).getString("distance")),
                        array.getJSONObject(i).getString("state"),
                        array.getJSONObject(i).getString("time"),
                        array.getJSONObject(i).getInt("open-angle"));

                storageData.add(sampleData);
            }

            modifyItemsOnUI.modifyItemsOnUI(oldView, bluetoothConn, httpRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void fillInArray(String response){
        try {
            JSONArray array = new JSONArray(response);

            Global.currentState = array.getJSONObject(0).getString("state");
            Global.currentLevel = Float.valueOf(array.getJSONObject(0).getString("distance"));

            for(int i=0;i<array.length();i++)
            {
                sampleData = new DataReceived(Float.valueOf(array.getJSONObject(i).getString("distance")),
                        array.getJSONObject(i).getString("state"),
                        array.getJSONObject(i).getString("time"),
                        array.getJSONObject(i).getInt("open-angle"));

                storageData.add(sampleData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DataReceived> getStorageData() {
        return storageData;
    }
}
