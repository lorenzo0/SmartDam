package it.unibo.isi.seeiot.dam_app.utils;

public class global {

    public static final String APP_LOG_TAG = "BT CLN";
    public static final String url = "http://8162c9d52e9f.ngrok.io/api/data";

    public static float currentLevel;
    public static String currentState;

    public class bluetooth {
        public static final int ENABLE_BT_REQUEST = 1;
        public static final String BT_DEVICE_ACTING_AS_SERVER_NAME = "isi00"; //MODIFICARE QUESTA COSTANTE CON IL NOME DEL DEVICE CHE FUNGE DA SERVER
        public static final String BT_SERVER_UUID = "7ba55836-01eb-11e9-8eb2-f2801f1b9fd1";
    }

}
