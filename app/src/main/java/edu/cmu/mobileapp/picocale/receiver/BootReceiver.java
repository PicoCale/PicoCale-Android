package edu.cmu.mobileapp.picocale.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import edu.cmu.mobileapp.picocale.service.LocationService;

/**
 * Created by user on 7/29/2015.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Boot receiver","called");
        Intent myIntent = new Intent(context, LocationService.class);
        context.startService(myIntent);
    }
}