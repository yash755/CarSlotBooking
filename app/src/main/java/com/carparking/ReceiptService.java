package com.carparking;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
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

               /* // Set up the header types needed to properly transfer JSON
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept-Encoding", "application/json");
                httpPost.setHeader("Accept-Language", "en-US");*/

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

            if(result.contains("0")) {
                new UserLocalStore(getApplicationContext()).updateslot("");
                sendNotification();
            }


            new Handler().postDelayed(new Runnable() {

                public void run() {
                    if(!new UserLocalStore(getApplicationContext()).getslot().equals(""))
                    new DoBackgroundTask().execute();
                }
            }, 10000);

            super.onPostExecute(result);
        }


    }

    private void sendNotification() {

            Intent intent = new Intent(this, SlotBooking.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.go)
                    .setContentTitle("Alert (Receipt Car Parking)")
                    .setContentText("Your have to pay $5")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }
}

