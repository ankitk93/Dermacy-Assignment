package com.android.example.dermacy.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.example.dermacy.MainActivity;

/**
 * Created by techie93 on 4/27/2015.
 */
public class InternetReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (isOnline(context)) {
            Toast.makeText(context, "Network Available Do operations", Toast.LENGTH_LONG).show();
           Intent updateIntent = new Intent(context, MainActivity.class);
            context.startService(updateIntent);
        }

    }


    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }
}