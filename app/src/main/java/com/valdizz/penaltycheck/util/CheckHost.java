package com.valdizz.penaltycheck.util;

import android.os.AsyncTask;
import android.util.Log;

import com.valdizz.penaltycheck.PenaltyCheckApplication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckHost extends AsyncTask<Void,Void,Boolean> {

    private Consumer consumer;

    public interface Consumer {
        void accept(Boolean isHostAvailable);
    }

    public CheckHost(Consumer consumer) {
        this.consumer = consumer;
        execute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(CheckPermissionsUtils.MVD_URL_HOST);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Android Application");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(3000);
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (IOException ignored) {
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.d(PenaltyCheckApplication.TAG, "Check host: "+ result);
        super.onPostExecute(result);
        consumer.accept(result);
    }
}
