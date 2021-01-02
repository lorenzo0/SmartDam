package it.unibo.isi.seeiot.dam_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cit.unibo.isi.seeiot.dam_app.R;
import it.unibo.isi.seeiot.dam_app.netutils.DataReceived;

public class StorageDataAdapter extends RecyclerView.Adapter<StorageDataAdapter.ViewHolder> {

    private ArrayList<DataReceived> historicalData;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        @Override
        public void onClick(View view) {}
    }

    public StorageDataAdapter(ArrayList<DataReceived> historicalData) {
        //httpRequests = new HTTPRequests();
        //dataStorage = httpRequests.tryHttpGetHData();
        this.historicalData = historicalData;
    }

    public ArrayList<DataReceived> getList(){
        return  this.historicalData;
    }

    @Override
    public int getItemCount() {
        return historicalData.size();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.fragment_data_history, viewGroup, false);

        return new StorageDataAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String x = "ciao";
        viewHolder.getDateTextView().setText(x);
        if(historicalData.size() >= position) {
            viewHolder.getAngleTextView().setText("Opening Dam: "+Integer.toString(historicalData.get(position).getAngle()));
            viewHolder.getStateTextView().setText(historicalData.get(position).getState());
            viewHolder.getDateTextView().setText(historicalData.get(position).getTime());
            viewHolder.getLevelView().setText("Water level: "+Float.toString(historicalData.get(position).getDistance()));
        }
    }

}
