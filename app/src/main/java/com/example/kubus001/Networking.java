package com.example.kubus001;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Networking {
    public static Boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfoWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting())
            return true;
        else
            return false;
    }
}
