package it.unibo.isi.seeiot.dam_app.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cit.unibo.isi.seeiot.dam_app.R;
import it.unibo.isi.seeiot.dam_app.UserInterface;
import it.unibo.isi.seeiot.dam_app.bluetooth.Bluetooth;
import it.unibo.isi.seeiot.dam_app.netutils.HTTPRequests;

public class handlerAlert {

    List<String> spinnerArray = null;


    public handlerAlert(){
        spinnerArray = fillChoice(spinnerArray);
    }

    public void showAlert(Context context, View oldView, HTTPRequests httpRequests, Bluetooth bluetoothConn) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View v = inflater.inflate(R.layout.dialog_modify,null);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TextView openingDam = (TextView) v.findViewById(R.id.newOpening);

        openingDam.setText("Il livello corrente di acqua è pari a: " + global.currentLevel + ". " +
                "Selezionare la nuova apertura della diga: ");

        Spinner spinner = (Spinner)v.findViewById(R.id.spinner_modify_dam);

        spinner.setAdapter(adapter);

        AlertDialog.Builder b=  new  AlertDialog.Builder(context)
                .setTitle("Modalità modifica")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                try {
                                    httpRequests.tryHttpPost(spinner.getSelectedItem().toString(), context, bluetoothConn);
                                    ((Switch)oldView.findViewById(R.id.switch1)).setChecked(false);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //((Switch)oldView.findViewById(R.id.switch1)).setChecked(false);
                                dialog.dismiss();
                            }
                        }
                );

        b.setView(v);
        b.create().show();
    }

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


