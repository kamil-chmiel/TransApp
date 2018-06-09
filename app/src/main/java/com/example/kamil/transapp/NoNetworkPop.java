package com.example.kamil.transapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.transapp.broadcastreceiver.MyApplication;
import com.transapp.broadcastreceiver.NetworkConnectionReceiver;

public class NoNetworkPop extends AppCompatActivity implements NetworkConnectionReceiver.NetworkListener {

    private NetworkConnectionReceiver ncr = new NetworkConnectionReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_no_internet);
    }

    public void onBackPressed(){


    }

    @Override
    public void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
        this.registerReceiver(ncr, new android.content.IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));


    }

    @Override
    public void onPause() {
        super.onPause();
        // unregister connection status listener
        MyApplication.getInstance().setConnectivityListener(null);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(ncr);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        if(isConnected)
            NoNetworkPop.super.onBackPressed();


    }
}
