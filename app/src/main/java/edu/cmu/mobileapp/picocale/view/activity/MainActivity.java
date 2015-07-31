package edu.cmu.mobileapp.picocale.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.service.DeviceImageServiceImpl;
import edu.cmu.mobileapp.picocale.service.ImageService;
import edu.cmu.mobileapp.picocale.util.LocationUtils;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtaining an instance of imageService to know
        // whether there are images present within
        // the current radius boundary
        ImageService imageService = new DeviceImageServiceImpl();
        int count = imageService.getLocationBasedImageList(this).size();
        Log.i("---BOOL2CNT-->", Double.valueOf(count).toString());
        Boolean isImageAvailable = false;
        if(count>0)
            isImageAvailable = true;
        else
            isImageAvailable = false;

        //Calling the LocationService Class
        Intent serviceIntent = new Intent("edu.cmu.mobileapp.picocale.service.LocationService");
        Log.i("---BOOL2AVL-->", isImageAvailable.toString());
        serviceIntent.putExtra("isImageAvailable",isImageAvailable);
        serviceIntent.putExtra("imageCount",count);
        startService(serviceIntent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //this is very important, otherwise you would get a null Scheme in the onResume later on.
        Log.i("Activity", "On resume");
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
