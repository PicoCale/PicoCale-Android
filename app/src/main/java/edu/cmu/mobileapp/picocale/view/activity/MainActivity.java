package edu.cmu.mobileapp.picocale.view.activity;

import android.app.Activity;
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
import edu.cmu.mobileapp.picocale.constants.NotificationConstants;
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

//        ---------------------------
        ImageService imageService = new DeviceImageServiceImpl();
        Activity activity = this;
        int count = imageService.getLocationBasedImageList(activity.getApplicationContext(), LocationUtils.getCurrentLocation(activity)).size();
        Boolean isImageAvailable = false;
        if(count>0)
            isImageAvailable = true;
        else
            isImageAvailable = false;

        //Getting the radius value from the preferences
        SharedPreferences sharedPref = this.getSharedPreferences("PicoCale", 0);
        boolean notificationSetting = sharedPref.getBoolean("notificationSetting", true);
//        Log.i("--NOTIF1-->", notificationSetting + "--" + NotificationConstants.notificationFlag);
        Intent serviceIntent=null;
        if(notificationSetting) {
            //Calling the LocationService Class
//            Log.d("--CHK123--->", "InsideMainActivity");
//            Log.i("--NOTIF2-->", "Inside TRUE");
            serviceIntent = new Intent("edu.cmu.mobileapp.picocale.service.LocationService");
//            serviceIntent.putExtra("isImageAvailable", isImageAvailable);
//            serviceIntent.putExtra("imageCount", count);
//            serviceIntent.putExtra("notificationSetting",notificationSetting);
//            Log.d("--CHK123--->", "InsideMainActivity:ImgAvail" + isImageAvailable);
            NotificationConstants.notificationFlag = 1;
            this.startService(serviceIntent);
//            Log.i("--NOTIFLAG2-->", NotificationConstants.notificationFlag+"");
        }
        else if(!notificationSetting && NotificationConstants.notificationFlag==1 && serviceIntent!=null){
//            Log.i("--NOTIF3-->","Inside FALSE");
            this.stopService(serviceIntent);
            NotificationConstants.notificationFlag=0;
        }
//      --------------------------------
    }

    protected void showAlertDialog(final String type){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Request "+type+" Access");

        // set dialog message
        alertDialogBuilder
                .setMessage(getString(R.string.app_name)+" suggests you to allow your "+type+" to be detected for a better experience!")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
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
