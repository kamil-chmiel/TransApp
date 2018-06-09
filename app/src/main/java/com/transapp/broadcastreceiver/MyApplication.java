package com.transapp.broadcastreceiver;
import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetworkConnectionReceiver.NetworkListener listener) {
        NetworkConnectionReceiver.networkListener = listener;
    }
}