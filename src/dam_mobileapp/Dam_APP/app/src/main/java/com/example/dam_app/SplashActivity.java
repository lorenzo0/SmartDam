package com.example.dam_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dam_app.NetUtils.HTTPRequest;

/*
* La SplashActivity viene prevista per un carimento pre-mainActivity.
* Questa, viene pensata perchè il doInBackground() della richiesta GET asincrona dei dati
* dal server non è sempre immediata e potrebbe richiede fino ad alcuni secondi (in caso di response code 200).
*
* Viene dunque istanziato un nuovo thread che effettua una richiesta di tipo
* SINCRONO. L'applicazione entra quindi in uno stato di freeze fino al momento in cui la richiesta
* consegna una risposta. Per evitare un deadlock della risposta, viene previsto un timeout massimo.
* (Per più dettagli, consultare HTTPRequest).
*
* Una volta finito il retrieve dei dati dal server, viene instanziato un nuovo intent, che consentirà
* di spostarsi nella mainActivity, richiamando il suo onCreate. Vengono inseriti nell'intent degli Extras.
* Questi Extras derivano da una serializzazione dell'oggetto httpRequests. Per serializzazione si intende
* una conversione in byte[] del dato, per poi ri-convertirlo in oggetto utilizzabile nella prossima activity.
* Facendo così, abbiamo la possibilità di utilizzare sempre lo stesso oggetto nelle diverse activity.
*
* Questo, per il corretto funzionamento dell'applicazione è indispensabile, perchè in HTTPRequest si ha
* l'arrayList contenente tutte le informazioni ottenute del server, che ci consente di non richiedere i dati
* nuovamente nel caso della mainActivity (nel suo onCreate) oppure in ShowHistoricalData.
*/
public class SplashActivity extends AppCompatActivity {
    HTTPRequest httpRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        httpRequests = new HTTPRequest();

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

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
        httpRequests.fillInArray(httpRequests.HTTPSync());
    }

}

