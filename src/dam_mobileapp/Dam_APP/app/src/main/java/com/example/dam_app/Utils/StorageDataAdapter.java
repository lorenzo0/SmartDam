package com.example.dam_app.Utils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dam_app.NetUtils.DataReceived;
import com.example.dam_app.R;

import java.util.ArrayList;

/*
* La recycleView ci permette di visualizzare, in modo dinamico, tutti gli elementi presenti
* nell'arrayList in HTTPRequest. La recycleView crea, attraverso il suo adapter n elementi del layout
* predisposto per il singolo oggetto, dove n è il numero di istanze di DataReceived contenute
* nell'arrayList.
*
* Il ViewHolder ci permette di creare un'inflater nel layout 'fragment_data_history' ed avere
* un'istanza di TextView per ogni dato avente.
*
* Il suo adapted dunque, attraverso il onBindViewHolder, invoca il layout manager che si occupa di creare
* una 'card' (come in fragment_data_history) per ogni istanza e ne setta i valori nei suoi campi di tipo TextView.
*
*/

public class StorageDataAdapter extends RecyclerView.Adapter<StorageDataAdapter.ViewHolder> {

    private ArrayList<DataReceived> historicalData;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView state;
        private TextView angle;
        private TextView level;

        public ViewHolder(View view) {
            super(view);

            date = (TextView) view.findViewById(R.id.date_data);
            state = (TextView) view.findViewById(R.id.state_data);
            angle = (TextView) view.findViewById(R.id.angle_data);
            level = (TextView) view.findViewById(R.id.level_data);
        }

        public TextView getStateTextView() {
            return state;
        }

        public TextView getDateTextView() {
            return date;
        }

        public TextView getAngleTextView() {
            return angle;
        }

        public TextView getLevelView() {
            return level;
        }
    }

    public StorageDataAdapter(ArrayList<DataReceived> historicalData) {
        this.historicalData = historicalData;
    }

    @Override
    public int getItemCount() {
        return historicalData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.fragment_data_history, viewGroup, false);

        return new StorageDataAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if(historicalData.size() >= position) {
            viewHolder.getAngleTextView().setText("Opening Dam: "+historicalData.get(position).getAngle());
            viewHolder.getStateTextView().setText(historicalData.get(position).getState());
            viewHolder.getDateTextView().setText(historicalData.get(position).getTime());
            viewHolder.getLevelView().setText("Water level: "+ Float.toString(historicalData.get(position).getDistance()));
        }
    }

}
