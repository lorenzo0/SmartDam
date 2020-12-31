package it.unibo.isi.seeiot.dam_app.netutils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DataReceived implements Comparable<DataReceived>, Parcelable, Serializable {
    private float distance;
    private String state, time;
    private int angle;

    public DataReceived(float distance, String state, String time, int angle) {
        this.distance = distance;
        this.state = state;
        this.time = time;
        this.angle = angle;
    }

    protected DataReceived(Parcel in) {
        distance = in.readFloat();
        state = in.readString();
        time = in.readString();
        angle = in.readInt();
    }

    public static final Creator<DataReceived> CREATOR = new Creator<DataReceived>() {
        @Override
        public DataReceived createFromParcel(Parcel in) {
            return new DataReceived(in);
        }

        @Override
        public DataReceived[] newArray(int size) {
            return new DataReceived[size];
        }
    };

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(distance);
        parcel.writeString(state);
        parcel.writeString(time);
        parcel.writeInt(angle);
    }

    @Override
    public int compareTo(DataReceived dataReceived) {
        return 0;
    }
}
