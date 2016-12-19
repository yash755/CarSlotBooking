package com.carparking;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


public class ReceiptService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        while(true)
        {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                new DoBackgroundTask().execute();
                e.printStackTrace();
            }
            return START_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();




    }

    private class DoBackgroundTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            String dataToSend = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://www.myprojectshub.com/carbackend/get_slot.php?slotname=" + new UserLocalStore(getApplicationContext()).getslot());

            try {
                httpPost.setEntity(new StringEntity(dataToSend, "UTF-8"));

                // Set up the header types needed to properly transfer JSON
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept-Encoding", "application/json");
                httpPost.setHeader("Accept-Language", "en-US");

                // Execute POST
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity responseEntity = httpResponse.getEntity();
                if (responseEntity != null) {
                    response = EntityUtils.toString(responseEntity);
                } else {
                    response = "{\"NO DATA:\"NO DATA\"}";
                }
            } catch (ClientProtocolException e) {
                response = "{\"ERROR\":" + e.getMessage().toString() + "}";
            } catch (IOException e) {
                response = "{\"ERROR\":" + e.getMessage().toString() + "}";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            System.out.println("Slot");
            new Handler().postDelayed(new Runnable() {

                public void run() {
                    if(!new UserLocalStore(getApplicationContext()).getslot().equals(""))
                    new DoBackgroundTask().execute();
                }
            }, 10000);

            super.onPostExecute(result);
        }
    }
}

