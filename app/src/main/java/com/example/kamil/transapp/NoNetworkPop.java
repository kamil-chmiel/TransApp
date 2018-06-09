package com.example.kamil.transapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.transapp.broadcastreceiver.NetworkConnectionReceiver;

public class NoNetworkPop extends AppCompatActivity implements NetworkConnectionReceiver.NetworkListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_no_internet);
    }

    public void onBackPressed(){


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected)
            NoNetworkPop.super.onBackPressed();

    }
}
