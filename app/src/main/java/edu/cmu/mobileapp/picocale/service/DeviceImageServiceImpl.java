package edu.cmu.mobileapp.picocale.service;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.provider.MediaStore;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.cmu.mobileapp.picocale.util.DistanceUtils;
import edu.cmu.mobileapp.picocale.util.LocationUtils;

/**
 * Created by srikrishnan_suresh on 07/26/2015.
 */
public class DeviceImageServiceImpl implements ImageService {
    @Override
    public List<String> getImageList(Activity activity, String requiredLocation) {
        List<String> imageList = new ArrayList<String>();
        String[] columns = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.LATITUDE,
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
        String location = "";
        String imagePath = "";
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
            longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
            latLng = new LatLng(latitude, longitude);
            if(latitude != 0.0 && longitude != 0.0) {
                location = LocationUtils.getAddressFromLocation(activity, latLng).getAddressLine(0);
                if(location.equals(requiredLocation)) {
                    imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    imageList.add(imagePath);
                }
            }
        }
        return imageList;
    }

    public List<String> getLocationBasedImageList(Activity activity){

        double imageLatitude, imageLongitude;
        double minLatitude, maxLatitude;
        double minLongitude, maxLongitude;
        double latitudeCorrectionFactor, longitudeCorrectionFactor;
        double currentLatitude = 0, currentLongitude = 0;
        double radiusValue;
        int imageCount = 0;
        String imagePath;

        List<String> imageList = new ArrayList<String>();

        String[] columns = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.LATITUDE,
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
        Log.i("---BOOL-->", "Inside getLocationBasedImageList");
        //Getting the current Location
        Location location = LocationUtils.getCurrentLocation(activity);

        if(location!=null) {
            //Getting the current latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }
        Log.i("---BOOLAT-->", Double.valueOf(currentLatitude).toString());
        Log.i("---BOOLON-->", Double.valueOf(currentLongitude).toString());
        //Getting the radius value from the preferences
        SharedPreferences sharedPref = activity.getSharedPreferences("PicoCale", 0);
        radiusValue = Double.parseDouble(sharedPref.getString("userRadius", "5"));

        //Getting the min, max latitude values
        latitudeCorrectionFactor = DistanceUtils.getLatitudeCorrectionFactor(radiusValue);
        minLatitude = currentLatitude - latitudeCorrectionFactor;
        maxLatitude = currentLatitude + latitudeCorrectionFactor;

        //Getting the min, max latitude values
        longitudeCorrectionFactor = DistanceUtils.getLongitudeCorrectionFactor(radiusValue,minLatitude);
        minLongitude = currentLongitude + longitudeCorrectionFactor;
        maxLongitude = currentLongitude - longitudeCorrectionFactor;

        //iterating through the device images
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            imageLatitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
            imageLongitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));

            //Deciding whether to add the image or not
            if ((imageLatitude >=minLatitude && imageLatitude <= maxLatitude) &&
                    (imageLongitude <=minLongitude && imageLongitude>=maxLongitude)) {
                //Adding to the count of images within the radius boundary
                imageCount++;

                //For populating the images within the radius boundary
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                imageList.add(imagePath);
            }
        }
//        Log.i("---BOOLCOUNT-->", Double.valueOf(imageCount).toString());
        //returning the location based image list
        return imageList;
    }

}
