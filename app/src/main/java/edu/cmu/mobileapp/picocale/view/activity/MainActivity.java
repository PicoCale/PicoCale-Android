package edu.cmu.mobileapp.picocale.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
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

        //Check whether the network is available!
        //If not invoke an alertDialog
        if(!LocationUtils.isNetworkAvailable(this)){
            showAlertDialog("network");
        }
        
        //Check whether the GPS is on!
        //If not invoke an alertDialog
        final LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if(!LocationUtils.isGPSOn(locationManager)){
            showAlertDialog("location");
        }

        /*//Obtaining an instance of imageService to know
        // whether there are images present within
        // the current radius boundary
        ImageService imageService = new DeviceImageServiceImpl();
        int count = imageService.getLocationBasedImageList(this).size();
//        Log.i("---BOOL2CNT-->", Double.valueOf(count).toString());
        Boolean isImageAvailable = false;
        if(count>0)
            isImageAvailable = true;
        else
            isImageAvailable = false;

        //Calling the LocationService Class
        Intent serviceIntent = new Intent("edu.cmu.mobileapp.picocale.service.LocationService");
//        Log.i("---BOOL2AVL-->", isImageAvailable.toString());
        serviceIntent.putExtra("isImageAvailable",isImageAvailable);
        serviceIntent.putExtra("imageCount",count);
        startService(serviceIntent);*/
    }

    protected void showAlertDialog(final String type){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Request "+type+" Access");

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.app_name)+" suggests you to allow your "+type+" to be detected for a better experience!")
                .setCancelable(false)
                .setPositiveButton("SettingsPage", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (type.equals("location")) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        } else if (type.equals("network")) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    }
                })
                .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
