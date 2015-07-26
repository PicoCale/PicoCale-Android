package edu.cmu.mobileapp.picocale.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
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
public class GalleryLocationUtils {

    public static List<String> getGalleryLatLngList(Activity activity) {
        List<String> locationList = new ArrayList<String>();
        String[] columns = { MediaStore.Images.ImageColumns.LATITUDE,
                MediaStore.Images.ImageColumns.LONGITUDE
        };

        final String orderBy = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC";

        Cursor cursor = activity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                orderBy
        );

        int count = cursor.getCount();
        Double latitude, longitude;
        LatLng latLng = null;
        String location = null;
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
            longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
            latLng = new LatLng(latitude, longitude);
            if(latitude != 0.0 && longitude != 0.0) {
                location = getAddressFromLocation(activity, latLng).getAddressLine(0);
                if(!locationList.contains(location))
                    locationList.add(location);
            }
        }
        return locationList;
    }

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
}