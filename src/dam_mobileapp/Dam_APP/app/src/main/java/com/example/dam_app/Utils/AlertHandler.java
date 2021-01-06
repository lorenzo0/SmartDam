package com.example.dam_app.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.dam_app.Bluetooth.Bluetooth;
import com.example.dam_app.NetUtils.HTTPRequest;
import com.example.dam_app.R;
import com.example.dam_app.Utils.Global.Global;

import java.util.ArrayList;
import java.util.List;

public class AlertHandler {

    List<String> spinnerArray = null;

    public AlertHandler(){
        spinnerArray = fillChoice(spinnerArray);
    }

    /*
    * Alert nel caso in cui il sistema si trovi in uno stato di ALLARM e sia presente una connessione bluetooth
    *
    * Lo spinner viene riempito nel costruttore della classe AlertHandler e viene contestualizzato nell'alert
    * grazie ad un adapter che colloca l'oggetto a seguito di un layoutInflater nel layout ricercato.
    *
    */
    public void showAlert(Context context, View oldView, HTTPRequest httpRequests, Bluetooth bluetoothConn) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.dialog_modify,null);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TextView openingDam = (TextView) v.findViewById(R.id.newOpening);

        openingDam.setText("Il livello corrente di acqua è pari a: " + Global.currentLevel + ". " +
                "Selezionare la nuova apertura della diga: ");

        Spinner spinner = (Spinner)v.findViewById(R.id.spinner_modify_dam);

        spinner.setAdapter(adapter);

        AlertDialog.Builder b=  new  AlertDialog.Builder(context)
                .setTitle("Modalità modifica")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                httpRequests.tryHTTPost(spinner.getSelectedItem().toString(), bluetoothConn, context, false);
                                ((Switch)oldView.findViewById(R.id.switch1)).setChecked(false);
                                httpRequests.tryHTTPGet(context, oldView, bluetoothConn, httpRequests);
                                Global.mod_op = false;
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((Switch)oldView.findViewById(R.id.switch1)).setChecked(false);
                                Global.mod_op = false;
                                dialog.dismiss();
                            }
                        }
                );

        b.setView(v);
        b.create().show();
    }

    /* Alert nel caso in cui il sistema NON si trovi in uno stato di ALLARM (è indifferente che sia o no presente una connessione bluetooth) */
    public void showWarningAlert(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
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

    /* Alert nel caso in cui NON è istauranta una connesione bluetooth (è indifferente che il corrente stato del sistema è ALLARM) */
    public void showNoBTAlert(Context context) {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Errore")
                .setMessage("E' possibile entrare in modalità modifica solo se il dispositivo è connesso al HC-06 tramite bluetooth")
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
    private List<String> fillChoice(List<String> spinnerArray){
        spinnerArray = new ArrayList<String>();
        spinnerArray.add("0%");
        spinnerArray.add("20%");
        spinnerArray.add("40%");
        spinnerArray.add("60%");
        spinnerArray.add("80%");
        spinnerArray.add("100%");
        return spinnerArray;
    }
}
