package com.mygdx.game;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;


public class InternetConnection extends Application {

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
