package edu.cmu.mobileapp.picocale.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.view.activity.MainActivity;

/**
 * Created by Jayakumaur on 29-07-2015.
 */
public class LocationService extends Service {

    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES =  2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    private NotificationManager mNM;
    Intent intent;
    int counter = 0;
    Boolean isImageAvailable = false;
    Boolean notificationSetting = false;
    int imageCount = 0;
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i("-->>Service created", "Yes");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        intent = new Intent(BROADCAST_ACTION);
        // Display a notification about us starting. We put an icon in the
        // status bar.
        //showNotification();
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        Log.i("->>Service started", "Yes");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, listener);
    }

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the
        // expanded notification
//        CharSequence text = getText(R.string.local_service_started);
//        CharSequence text = getText(R.string.service_notification_1)+" "+Integer.valueOf(imageCount).toString()+" "+getText(R.string.service_notification_2);
        CharSequence text = getText(R.string.service_notification_1)+" "+getText(R.string.service_notification_2);
        Log.i("Text",text.toString());

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.mipmap.ic_launcher,
                text, System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.service_name),
                text, contentIntent);

        // Send the notification
        // We use a layout id because it is a unique number. We use it later to
        // cancel.
        mNM.notify(R.string.app_name, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.v("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        Log.i("Perform on Background T","Started");
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    //Location listener for listening location updates

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {

            Log.i("*********", "Location changed");

            // if (isBetterLocation(loc, previousBestLocation)) {
            loc.getLatitude();
            loc.getLongitude();
            Log.i("Latitude", Double.toString(loc.getLatitude()));
            intent.putExtra("Latitude", loc.getLatitude());
            intent.putExtra("Longitude", loc.getLongitude());
            intent.putExtra("Provider", loc.getProvider());
            Log.i("--CHK123--->", "InsideLocService:NOTIF-" + notificationSetting + "Image-" + isImageAvailable);
            
            //checking for any device images within the current radius boundary
            if(isImageAvailable && notificationSetting)
                showNotification();
            sendBroadcast(intent);
            //}
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        protected boolean isBetterLocation(Location location, Location currentBestLocation) {
            if (currentBestLocation == null) {
                // A new location is always better than no location
                return true;
            }

            // Check whether the new location fix is newer or older
            long timeDelta = location.getTime() - currentBestLocation.getTime();
            boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
            boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
            boolean isNewer = timeDelta > 0;

            // If it's been more than two minutes since the current location, use the new location
            // because the user has likely moved
            if (isSignificantlyNewer) {
                return true;
                // If the new location is more than two minutes older, it must be worse
            } else if (isSignificantlyOlder) {
                return false;
            }

            // Check whether the new location fix is more or less accurate
            int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
            boolean isLessAccurate = accuracyDelta > 0;
            boolean isMoreAccurate = accuracyDelta < 0;
            boolean isSignificantlyLessAccurate = accuracyDelta > 200;

            // Check if the old and new location are from the same provider
            boolean isFromSameProvider = isSameProvider(location.getProvider(),
                    currentBestLocation.getProvider());

            // Determine location quality using a combination of timeliness and accuracy
            if (isMoreAccurate) {
                return true;
            } else if (isNewer && !isLessAccurate) {
                return true;
            } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                return true;
            }
            return false;
        }

        /**
         * Checks whether two providers are the same
         */
        private boolean isSameProvider(String provider1, String provider2) {
            if (provider1 == null) {
                return provider2 == null;
            }
            return provider1.equals(provider2);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        //showNotification();
        /*CharSequence text = getText(R.string.local_service_started);
        Log.i("Text",text.toString());
        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.icon_cloud,
                text, System.currentTimeMillis());
        // The PendingIntent to launch our activity if the user selects this
        // notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, getText(R.string.service_name),
                text, contentIntent);
        // Send the notification.
        // We use a layout id because it is a unique number. We use it later to
        // cancel.
        mNM.notify(R.string.local_service_started, notification);
*/
        if(intent!=null) {
            Log.d("--CHK123--->", "InsideOnStartCommand");
            isImageAvailable = intent.getBooleanExtra("isImageAvailable", false);
            imageCount = intent.getIntExtra("imageCount", 0);
            notificationSetting = intent.getBooleanExtra("notificationSetting", false);
            Log.i("--CHK123--->", "InsideOnStartCommand"+isImageAvailable);
            Log.i("---BOOL3AVL-->", isImageAvailable.toString());
        }
        return super.onStartCommand(intent,flags,startId);
    }
}