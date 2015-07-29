package edu.cmu.mobileapp.picocale.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class LocationUtils {

   /**
     * Fetches the address for a given location and context
     * */
    public static Address getAddressFromLocation(Context context, LatLng latLng) {
        Address address = null;
        Geocoder gCoder = new Geocoder(context);
        try {
            List<Address> addresses = gCoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            address = addresses.get(0);
        }
        catch (IOException exception) {

        }
        return address;
    }
    /**
     * Checks if GPS is on for the given manager
     * */
    public static boolean isGPSOn(LocationManager manager) {
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            return true;
        return false;
    }

    /**
     *
     * Fetches the current location of the user using the best (GPS/NETWORK) provider
     */
    public static Location getCurrentLocation(Activity activity){
        LocationManager lm;
        lm = (LocationManager)activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location location = lm.getLastKnownLocation(provider);
            if (location == null) {
                continue;
            }
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", location);
                bestLocation = location;
            }
        }
        return bestLocation;
    }
}