package com.carparking;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SlotBooking extends AppCompatActivity implements View.OnClickListener {

    String uri = "@drawable/go";
    String uri1 = "@drawable/wait";
    String uri2 = "@drawable/stop";
    String status;
    ImageView i1,i2,i3,i4;
    ArrayList<String> parkingslot = new ArrayList<String>();;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        i1= (ImageView)findViewById(R.id.slot1);
        i2= (ImageView)findViewById(R.id.slot2);
        i3= (ImageView)findViewById(R.id.slot3);
        i4= (ImageView)findViewById(R.id.slot4);


        getparkdata();

        i1.setOnClickListener(this);
        i2.setOnClickListener(this);
        i3.setOnClickListener(this);
        i4.setOnClickListener(this);

        startService();


        
    }

    void getparkdata(){

        new Util().getparkingdata(this, new GetResult() {

            @Override
            public void done(JSONObject jsonObject) {
                for (int i = 1; i <= jsonObject.length(); i++) {
                    try {
                        String id = jsonObject.getString("slot"+i);
                        parkingslot.add(id);
                    } catch (JSONException e) {
                    }


                }

                setparkinglot();
            }

        });


    }

    void setparkinglot(){


        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        int imageResource1 = getResources().getIdentifier(uri1, null, getPackageName());
        int imageResource2 = getResources().getIdentifier(uri2, null, getPackageName());


        Drawable res = getResources().getDrawable(imageResource);
        Drawable res1 = getResources().getDrawable(imageResource1);
        Drawable res2 = getResources().getDrawable(imageResource2);


        for(int i=0;i<=parkingslot.size();i++){

            if(i == 0) {
                if (parkingslot.get(i).equals("0")) {
                    i1.setImageDrawable(res);
                } else if (parkingslot.get(i).equals("1")) {
                    i1.setImageDrawable(res1);
                } else {
                    i1.setImageDrawable(res2);
                }
            }else if (i == 1){
                if (parkingslot.get(i).equals("0")) {
                    i2.setImageDrawable(res);
                } else if (parkingslot.get(i).equals("1")) {
                    i2.setImageDrawable(res1);
                } else {
                    i2.setImageDrawable(res2);
                }
            }else if (i == 2){
                if (parkingslot.get(i).equals("0")) {
                    i3.setImageDrawable(res);
                } else if (parkingslot.get(i).equals("1")) {
                    i3.setImageDrawable(res1);
                } else {
                    i3.setImageDrawable(res2);
                }
            }else if (i == 3){
                if (parkingslot.get(i).equals("0")) {
                    i4.setImageDrawable(res);
                } else if (parkingslot.get(i).equals("1")) {
                    i4.setImageDrawable(res1);
                } else {
                    i4.setImageDrawable(res2);
                }
            }
        }


    }

    @Override
    public void onClick(View view) {

        if(view == i1){
            String id = parkingslot.get(0);
            if(id.equals("1") || id.equals("2")){
                Toast.makeText(this, "You  can't book now",
                        Toast.LENGTH_SHORT).show();
            }else{
                if(new UserLocalStore(getApplicationContext()).getslot().equals(""))
                booknow("slot1");
                else
                    Toast.makeText(this, "You  already book " + new UserLocalStore(getApplicationContext()).getslot(),
                            Toast.LENGTH_SHORT).show();
            }

        }else if (view == i2){
            String id = parkingslot.get(1);
            if(id.equals("1") || id.equals("2")){
                Toast.makeText(this, "You  can't book now",
                        Toast.LENGTH_SHORT).show();

            }else{
                if(new UserLocalStore(getApplicationContext()).getslot().equals(""))
                    booknow("slot2");
                else
                    Toast.makeText(this, "You  already book " + new UserLocalStore(getApplicationContext()).getslot(),
                            Toast.LENGTH_SHORT).show();
            }

        }else if (view == i3){
            String id = parkingslot.get(2);
            if(id.equals("1") || id.equals("2")){
                Toast.makeText(this, "You  can't book now",
                        Toast.LENGTH_SHORT).show();

            }else{
                if(new UserLocalStore(getApplicationContext()).getslot().equals(""))
                    booknow("slot3");
                else
                    Toast.makeText(this, "You  already book " + new UserLocalStore(getApplicationContext()).getslot(),
                            Toast.LENGTH_SHORT).show();
            }

        }else{
            String id = parkingslot.get(3);
            if(id.equals("1") || id.equals("2")){
                Toast.makeText(this, "You  can't book now",
                        Toast.LENGTH_SHORT).show();

            }else{
                if(new UserLocalStore(getApplicationContext()).getslot().equals(""))
                    booknow("slot4");
                else
                    Toast.makeText(this, "You  already book " + new UserLocalStore(getApplicationContext()).getslot(),
                            Toast.LENGTH_SHORT).show();
            }

        }
    }

    void booknow(final String slot){

        Map<String, String> params = new HashMap<String, String>();
        params.put("slotname",slot);
        params.put("update_value","1");

        new Util().slotbook(params,this, new GetResult() {

            @Override
            public void done(JSONObject jsonObject) {
                try {
                    status = jsonObject.getString("status");

                    if(status.equals("success")){
                        UserLocalStore userLocalStore =new UserLocalStore(getApplicationContext());
                        userLocalStore.updateslot(slot);
                        finish();
                        startActivity(getIntent());

                    }else{

                        new Util().showerrormessage(SlotBooking.this,"Something Went Wrong Try again !!!!");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });


    }

    public void startService() {
        startService(new Intent(getBaseContext(), ReceiptService.class));
    }
}
