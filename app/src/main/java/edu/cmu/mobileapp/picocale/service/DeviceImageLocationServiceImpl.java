package edu.cmu.mobileapp.picocale.service;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.cmu.mobileapp.picocale.util.LocationUtils;

/**
 * Created by srikrishnan_suresh on 07/26/2015.
 */
public class DeviceImageLocationServiceImpl implements ImageLocationService {
    @Override
    public List<String> getLocationAddressList(Activity activity) {
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
                location = LocationUtils.getAddressFromLocation(activity, latLng).getAddressLine(0);
                if(!locationList.contains(location))
                    locationList.add(location);
            }
        }
        Collections.sort(locationList, String.CASE_INSENSITIVE_ORDER);
        return locationList;
    }
}
