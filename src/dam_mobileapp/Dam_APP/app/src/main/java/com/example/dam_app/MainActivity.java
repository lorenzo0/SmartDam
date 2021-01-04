package com.example.dam_app;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.dam_app.Bluetooth.Bluetooth;
import com.example.dam_app.NetUtils.HTTPRequest;
import com.example.dam_app.NetUtils.ModifyUI;
import com.example.dam_app.Utils.AlertHandler;
import com.example.dam_app.Utils.Global.Global;

public class MainActivity extends AppCompatActivity {

    HTTPRequest httpRequest;
    CountDownTimer countDownTimer;
    Switch modalitySwitch;
    boolean checkOnCreate;
    Bluetooth bluetoothConn;
    AlertHandler alerHandler;
    View currentView;
    ModifyUI modifyItemsOnUI;

    /*
     * La main Acivity viene caricata, subito dopo la SplashActivity. Viene dunque controllato se gli
     * extra caricati nell'intent Splash - Main sono stati correttamente ottenuti, se questo è avvenuto
     * con successo, allora non è necessario creare una nuova istanza dell'oggetto HTTPRequest, altrimenti si.
     *
     * Una volta trovati i dati necessari, andiamo a riempire i campi attraverso la classe ModifyUI.
     * Viene richiesta, se non attiva, una connessione bluetooth (se viene rifiutata, gli verrà richiesto
     * ogni 5000 millisecondi, quindi 5 secondi). Una volta istaurata, viene cercato il dispositivo specificato
     * nel file Utils/Global/Global tramite il BT_DEVICE_ACTING_AS_SERVER_NAME.
     *
     * Viene successivamente gestita il ciclo di vita dell'applicazione, a partire da OnCreate,
     * la quale viene invocata anche a seguito del passaggio da un'activity ad un'altra.
     * Esempio; quando ci si trova in ShowHistoricalData, quindi nello storico di tutti i dati
     * e si vuole tornare nella parentActivity (Manifest), la MainActivity viene ri-generata.
     *
     * L
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createCountDown(5000);
        httpRequest = new HTTPRequest();
        modifyItemsOnUI = new ModifyUI();

        bluetoothConn = new Bluetooth();
        alerHandler = new AlertHandler();

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            httpRequest = (HTTPRequest) getIntent().getSerializableExtra("httpReq");
        else
            httpRequest = new HTTPRequest();

        modalitySwitch = (Switch) findViewById(R.id.switch1);
        currentView = findViewById(R.id.angle).getRootView();
        checkOnCreate = true;

        initUI();

        if (httpRequest.getStorageData().size() != 0)
            modifyItemsOnUI.modifyItemsOnUI(currentView, bluetoothConn, httpRequest);
        else
            httpRequest.tryHTTPGet(MainActivity.this, currentView, bluetoothConn, httpRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(checkOnCreate))
            httpRequest.tryHTTPGet(MainActivity.this, currentView, bluetoothConn, httpRequest);
        checkOnCreate = false;
    }

    /*
    * Nel caso in cui l'app si trovi in uno stato di Pausa (Un'altra APP si trova in foreground)
    * allora viene chiusa la connessione bluetooth, che verrò ri-istanziata quando Dam_APP tornerà
    * ad essere in primo piano nello smartphone dell'utente.
    */

    @Override
    protected void onPause() {
        super.onPause();
        if(bluetoothConn.btChannel != null)
            bluetoothConn.closeConnection();
        checkOnCreate = true;
    }

    /*
     * OnCreate di UserInterface viene invocato anche nel caso si passi da ShowHistoricalData,
     * quindi se non riesce a mantenere la connessione, allora ri-chiedo la connessione
     * all'embedded system.
     */

    protected void requestOpeningBT(){
        final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter != null && !btAdapter.isEnabled())
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), Global.bluetooth.ENABLE_BT_REQUEST);

    }

    protected void connectBT(){
        try {
            if(bluetoothConn.btChannel==null)
                bluetoothConn.connectToBTServer(getApplicationContext());
        } catch (unibo.btlib.exceptions.BluetoothDeviceNotFound bluetoothDeviceNotFound) {
            bluetoothDeviceNotFound.printStackTrace();
        }
    }

    /*
    * Viene gestita la casistica nella quale l'utente voglia utilizzare la modalità modifica
    * (riservata all'operatore - ma senza login e/o registrazione) tramite degli alert.
    * Secondo specifiche, l'utente può diventare utente operatore solo in una situazione, con
    * determinati pre-requisiti;
    *   - Lo stato corrente del sistema deve essere uguale ad ALLARME.
    *   - Deve essere istaurata una connessione con il dispositivo specificato nel file Utils/Global/Global.
    *
    * Nel caso in cui questi pre-requisiti vengano rispettati, allora verrà visualizzata una dialog
    * riservata alla modifica dell'ampiezza della diga, ignorando i parametri indicati da controller (ARDUINO).
    *
    * Viceversa, se non è presente una connessione bluetooth, viene presentata una dialog - No BT connection.
    * Ultima casistica, se è presente una connessione bluetooth con il corretto dispositivo ma il sistema
    * non si trova nello stato di ALLARM, viene presentata una dialog - No ALLARM state.
    */
    private void initUI() {
        modalitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if (Global.currentState.equals("ALLARM")) {
                        if(bluetoothConn.btChannel != null) {
                            bluetoothConn.sendMessage("MOD-OP");
                            Global.mod_op = true;
                            alerHandler.showAlert(MainActivity.this, currentView, httpRequest, bluetoothConn);
                        }else {
                            alerHandler.showNoBTAlert(MainActivity.this);
                            modalitySwitch.setChecked(false);
                        }

                    }else{
                        alerHandler.showWarningAlert(MainActivity.this);
                        modalitySwitch.setChecked(false);
                    }
                }
            }
        });

        findViewById(R.id.info_button).setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowHistoricalData.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("httpReq", httpRequest.getStorageData());
            intent.putExtras(bundle);
            if(bluetoothConn.btChannel != null)
                bluetoothConn.closeConnection();
            startActivity(intent);
        });

        requestOpeningBT();
        if(bluetoothConn.btChannel == null)
            connectBT();
    }

    /*
    * Viene utilizzata la classe CountDownTimer, messa a disposizione da Android. Questa,
    * si occupa di istanziare un worker thread per il corretto funzionamento del timer (onTick
    * viene eseguito da un thread parallelo al MainThread). Alla conclusione del countDown
    * viene invocata la procedura onFinish(), che esegue sul thread principale una richiesta
    * get dei dati al server.
    *
    * tryHTTPGet crea una queue di richieste asincrone al server che vengono
    * quindi eseguite su di un thread parallelo al principale, non andando a creare un
    * android.os.NetworkOnMainThreadException.
    */
    public void createCountDown(long numberMilliSec) {
        countDownTimer = new CountDownTimer(numberMilliSec, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        httpRequest.tryHTTPGet(MainActivity.this, currentView, bluetoothConn, httpRequest);
                        createCountDown(5000);
                    }
                });
            }
        };
        countDownTimer.start();
    }
}