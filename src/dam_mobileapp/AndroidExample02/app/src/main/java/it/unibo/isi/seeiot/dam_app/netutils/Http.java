package it.unibo.isi.seeiot.dam_app.netutils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Http {

    public interface Listener{
        void onHttpResponseAvailable(final HttpResponse response);
    }

    public static void get(final String url, final Listener listener){
        new AsyncTask<Void, Void, HttpResponse>(){
            @Override
            protected HttpResponse doInBackground(Void... voids) {
                final HttpURLConnection connection;
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");

                    return new HttpResponse(connection.getResponseCode(), connection.getInputStream());

                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(HttpResponse response) {
                listener.onHttpResponseAvailable(response);
            }
        }.execute();
    }

    public static void post(final String url, final byte[] payload, final Listener listener){
        new AsyncTask<Void, Void, HttpResponse>(){
            @Override
            protected HttpResponse doInBackground(Void... voids) {
                final HttpURLConnection connection;
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(payload);

                    return new HttpResponse(connection.getResponseCode(), connection.getInputStream());
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(HttpResponse response) {
                listener.onHttpResponseAvailable(response);
            }
        }.execute();
    }

    public static HttpResponse HTTPSync(String url) {
            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;
            StringBuffer jsonText = new StringBuffer();

            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.connect();

                int code = connection.getResponseCode();

                if (code == 200)
                    return new HttpResponse(connection.getResponseCode(), connection.getInputStream());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

}
