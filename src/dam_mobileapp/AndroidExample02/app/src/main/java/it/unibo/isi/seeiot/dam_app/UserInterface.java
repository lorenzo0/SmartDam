package it.unibo.isi.seeiot.dam_app;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

import cit.unibo.isi.seeiot.dam_app.R;
import it.unibo.isi.seeiot.dam_app.bluetooth.Bluetooth;
import it.unibo.isi.seeiot.dam_app.netutils.Http;
import it.unibo.isi.seeiot.dam_app.utils.global;
import unibo.btlib.exceptions.BluetoothDeviceNotFound;

public class UserInterface extends AppCompatActivity {

    /*
     * Per testare l'applicazione si fa riferimento al servizio: http://dummy.restapiexample.com/
     */

    Switch modalitySwitch;
    Spinner spinner;
    boolean checkOnCreate;
    String currentState;
    Float currentLevel;
    List<String> spinnerArray = null;
    Bluetooth bluetoothConn;
    Timer timer = new Timer();
    CountDownTimer countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_interface);

        bluetoothConn = new Bluetooth();

        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && !btAdapter.isEnabled()) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), global.bluetooth.ENABLE_BT_REQUEST);
        }

        modalitySwitch = (Switch) findViewById(R.id.switch1);
        spinnerArray = fillChoice(spinnerArray);
        checkOnCreate = true;

        initUI();
    }

    protected void connectBT(){
        try {
            bluetoothConn.connectToBTServer(UserInterface.this);
        } catch (BluetoothDeviceNotFound bluetoothDeviceNotFound) {
            bluetoothDeviceNotFound.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //tryHttpGet();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!(checkOnCreate))
            //tryHttpGet();
        checkOnCreate = false;
        createCountDown(5000);
    }

    private void initUI() {
        findViewById(R.id.info_button).setOnClickListener(v -> {
            final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();

            if(activeNetwork.isConnectedOrConnecting()){
                tryHttpGet();
            }

        });

        modalitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (currentState.equals("ALLARM")) {
                        connectBT();
                        showAlert();
                    }else {
                        showWarningAlert();
                        modalitySwitch.setChecked(false);
                    }
                }
            }
        });
    }

    private void tryHttpGet(){

        Http.get(global.url, response -> {
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

                    ((TextView)findViewById(R.id.angle)).setText(array.getJSONObject(0).getString("open-angle"));
                    ((TextView)findViewById(R.id.level)).setText(array.getJSONObject(0).getString("distance"));
                    ((TextView)findViewById(R.id.state)).setText(array.getJSONObject(0).getString("state"));
                    ((TextView)findViewById(R.id.timestamp)).setText("Ultima rilevazione: \n " +array.getJSONObject(0).getString("time"));

                    currentState = array.getJSONObject(0).getString("state");
                    currentLevel = Float.valueOf(array.getJSONObject(0).getString("distance"));

                    switch(currentState){
                        case "ALLARM":
                            ((TextView)findViewById(R.id.state)).setTextColor(Color.parseColor("#ff0000"));

                            ((TextView)findViewById(R.id.level_water)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.level)).setVisibility(View.VISIBLE);

                            ((TextView)findViewById(R.id.open_angle_dam)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.angle)).setVisibility(View.VISIBLE);
                            break;

                        case "PRE-ALLARM":
                            ((TextView)findViewById(R.id.state)).setTextColor(Color.parseColor("#ff8100"));

                            ((TextView)findViewById(R.id.level_water)).setVisibility(View.VISIBLE);
                            ((TextView)findViewById(R.id.level)).setVisibility(View.VISIBLE);

                            ((TextView)findViewById(R.id.open_angle_dam)).setVisibility(View.GONE);
                            ((TextView)findViewById(R.id.angle)).setVisibility(View.GONE);
                            break;

                        case "NORMAL":
                            ((TextView)findViewById(R.id.state)).setTextColor(Color.parseColor("#00ff11"));

                            ((TextView)findViewById(R.id.level_water)).setVisibility(View.GONE);
                            ((TextView)findViewById(R.id.level)).setVisibility(View.GONE);

                            ((TextView)findViewById(R.id.open_angle_dam)).setVisibility(View.GONE);
                            ((TextView)findViewById(R.id.angle)).setVisibility(View.GONE);

                            break;
                    }

                    switch(Integer.parseInt(array.getJSONObject(array.length()-1).getString("open-angle"))){
                        case 0:
                            ((ImageView)findViewById(R.id.image_dam)).setImageResource(R.drawable.empty_dam);
                            break;

                        case 100:
                            ((ImageView)findViewById(R.id.image_dam)).setImageResource(R.drawable.full_dam);
                            break;

                        default:
                            ((ImageView)findViewById(R.id.image_dam)).setImageResource(R.drawable.working_dam);
                            break;
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void tryHttpPost(String percentageOpening) throws JSONException {

        int finalData = Integer.valueOf(percentageOpening.replace("%", ""));

        Log.d("SendingMessage", "Distance: "+ currentLevel + ", State: "+ currentState + ", Open-Angle: " +finalData);


        final String content = new JSONObject()
                .put("distance", currentLevel)
                .put("state", currentState)
                .put("sender", "APP")
                .put("open-angle", finalData).toString();

        /* Qui bisogna effettuare un controllo in più. Se non c'è la connessione bluethoot, non mando neanche quello http */
        Http.post(global.url, content.getBytes(), response -> {
            if(response.code() == HttpURLConnection.HTTP_OK) {
                Toast.makeText(UserInterface.this, "Dati inviati correttamente al server!", Toast.LENGTH_LONG).show();
                bluetoothConn.sendMessage("New opening: " + finalData);
            }else
                Toast.makeText(UserInterface.this,"Si è verificato un problema, i dati NON sono stati inviati al server!", Toast.LENGTH_LONG).show();
        });
    }

    private void showAlert() {

        LayoutInflater i = UserInterface.this.getLayoutInflater();

        View v = i.inflate(R.layout.dialog_modify,null);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TextView openingDam = (TextView) v.findViewById(R.id.newOpening);

        openingDam.setText("Il livello corrente di acqua è pari a: " + currentLevel + ". " +
                "Selezionare la nuova apertura della diga: ");

        Spinner spinner = (Spinner)v.findViewById(R.id.spinner_modify_dam);

        spinner.setAdapter(adapter);

        AlertDialog.Builder b=  new  AlertDialog.Builder(UserInterface.this)
                .setTitle("Modalità modifica")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // TODO Conferma - qui si deve creare una richiesta bluethoot che comunica il nuovo angolo del servo
                                /* Qui bisogna fare un controllo - se non è ancora connesso a bt allora deve rifiutare la richiesta*/
                                try {
                                    tryHttpPost(spinner.getSelectedItem().toString());
                                    modalitySwitch.setChecked(false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                modalitySwitch.setChecked(false);
                                dialog.dismiss();
                            }
                        }
                );

        b.setView(v);
        b.create().show();
    }

    private void showWarningAlert() {
        final AlertDialog dialog = new AlertDialog.Builder(UserInterface.this)
                .setTitle("Errore")
                .setMessage("E' possibile entrare in modalità modifica solo nel caso in cui lo stato corrente è ALLARME")
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .create();

        dialog.show();
    }

    /*
     *   E' possibile per l'operatore inserire alcuni valori percentili per
     *   l'apertura della diga. (dati dalle specifiche)
    */
    private List<String>  fillChoice(List<String> spinnerArray){
        spinnerArray = new ArrayList<String>();
        spinnerArray.add("0%");
        spinnerArray.add("20%");
        spinnerArray.add("40%");
        spinnerArray.add("60%");
        spinnerArray.add("80%");
        spinnerArray.add("100%");
        return spinnerArray;
    }

    /* CountDown handle */

    public void createCountDown(long numberMilliSec){
        countDown = new CountDownTimer(numberMilliSec, 1000) {

            public void onTick(long millisUntilFinished) {

                //Toast.makeText(getApplicationContext(),"Tic!",Toast.LENGTH_SHORT).show();
            }
            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"Finito!",Toast.LENGTH_LONG).show();
                        //tryHttpGet();
                    }
                });
            }
        };

        countDown.start();
    }

}
