package com.tech.arinzedroid.starchoice.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tech.arinzedroid.starchoice.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle("Make Payment");
    }
}
