package com.networkConnectivityHandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.*;

public class NetworkConnectionReceiver extends BroadcastReceiver {

    public NetworkConnectionReceiver(){
        super();
    }

    public static NetworkListener networkListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if(networkListener != null)
            networkListener.onNetworkConnectionChanged(isConnected);
    }


    public interface NetworkListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
