package com.carparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DummyReceipt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_receipt);
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}
