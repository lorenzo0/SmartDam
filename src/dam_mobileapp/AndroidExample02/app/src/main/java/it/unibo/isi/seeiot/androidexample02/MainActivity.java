package it.unibo.isi.seeiot.androidexample02;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Objects;

import it.unibo.isi.seeiot.androidexample02.R;
import it.unibo.isi.seeiot.androidexample02.netutils.Http;

public class MainActivity extends AppCompatActivity {

    /*
     * Per testare l'applicazione si fa riferimento al servizio: http://dummy.restapiexample.com/
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        findViewById(R.id.info_button).setOnClickListener(v -> {
            final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            final NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();

            if(activeNetwork.isConnectedOrConnecting()){
                tryHttpGet();
            }
        });

        /*findViewById(R.id.postBtn).setOnClickListener(v -> {
            try {
                tryHttpPost();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });*/
    }

    private void tryHttpGet(){

        //final String url = "http://dummy.restapiexample.com/api/v1/employee/1";
        final String url = "http://d6ae797a516d.ngrok.io/api/data";

        Http.get(url, response -> {
            if(response.code() == HttpURLConnection.HTTP_OK){
                try {
                    //JSONObject jsonObject = new JSONObject(response.contentAsString()); //Here reponse is the yours server response
                    JSONArray array = new JSONArray(response.contentAsString());
                    Log.d("resp1", array.toString());

                    /*for(int i=0;i<array.length();i++) Questo per avere tutti i record!
                    {
                        String time = array.getJSONObject(i).getString("time");
                        String value = array.getJSONObject(i).getString("value");
                        String place = array.getJSONObject(i).getString("place");

                        Toast.makeText(getApplicationContext(),time+" - "+value+" - "+place,Toast.LENGTH_LONG).show();
                    }*/

                    //((TextView)findViewById(R.id.angle)).setText(response.contentAsString()); //Ancora devo fare la get
                    ((TextView)findViewById(R.id.level)).setText(array.getJSONObject(array.length()-1).getString("value"));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*private void tryHttpPost() throws JSONException {

        final String url = "http://dummy.restapiexample.com/api/v1/create";
        final String content = new JSONObject()
                .put("name","test" + new Random().nextLong())
                .put("salary","1000")
                .put("age","30").toString();

        Http.post(url, content.getBytes(), response -> {
            if(response.code() == HttpURLConnection.HTTP_OK){
                try {
                    ((TextView)findViewById(R.id.angle)).setText(response.contentAsString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}
