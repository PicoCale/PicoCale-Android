package edu.cmu.mobileapp.picocale.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import edu.cmu.mobileapp.picocale.constants.PicoCaleImageConstants;
import edu.cmu.mobileapp.picocale.model.PicoCaleImage;
import edu.cmu.mobileapp.picocale.util.DistanceUtils;
import edu.cmu.mobileapp.picocale.util.LocationUtils;

/**
 * Created by srikrishnan_suresh on 07/26/2015.
 */
public class DeviceImageServiceImpl implements ImageService {

    public PicoCaleImage picoCaleImage;

    @Override
    public List<PicoCaleImage> getImageList(Activity activity, String requiredLocation) {
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

        List<PicoCaleImage> picoCaleImageList=new ArrayList<PicoCaleImage>();

        int count = cursor.getCount();
        Double latitude, longitude;
        LatLng latLng = null;
        String location = "";
        String imagePath = "";

        //1.//Code to be added------------------------
        double minLatitude, maxLatitude;
        double minLongitude, maxLongitude;
        double latitudeCorrectionFactor, longitudeCorrectionFactor;
        double currentLatitude = 0, currentLongitude = 0;
        double radiusValue;

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

        //1.//Code to be added endss---------------

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
            longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
            latLng = new LatLng(latitude, longitude);
            //1.//Code to be modified---------------
            if ((latitude >=minLatitude && latitude <= maxLatitude) &&
                    (longitude <=minLongitude && longitude>=maxLongitude)) {
                //1.//Code to be modified endss---------------
                location = LocationUtils.getAddressFromLocation(activity, latLng).getAddressLine(0);
                if(location.equals(requiredLocation)) {
                    imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                    picoCaleImage=new PicoCaleImage(imagePath, PicoCaleImageConstants.DEVICE_IMAGE,latitude,longitude);
                    //imageList.add(imagePath);
                    picoCaleImageList.add(picoCaleImage);

                }
            }
        }
        return picoCaleImageList;
    }

    public List<PicoCaleImage> getLocationBasedImageList(Context context,Location location){

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

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                orderBy
        );

        //List for picoCaleImageList
        List<PicoCaleImage> picoCaleImageList=new ArrayList<PicoCaleImage>();

        int count = cursor.getCount();
        Log.i("---BOOL-->", "Inside getLocationBasedImageList");
        //Getting the current Location
//        Location location = LocationUtils.getCurrentLocation(activity);

        if(location!=null) {
            //Getting the current latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
        }
        Log.i("---BOOLAT-->", Double.valueOf(currentLatitude).toString());
        Log.i("---BOOLON-->", Double.valueOf(currentLongitude).toString());
        //Getting the radius value from the preferences
        SharedPreferences sharedPref = context.getSharedPreferences("PicoCale", 0);
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
                //imageList.add(imagePath);
                picoCaleImage=new PicoCaleImage(imagePath,PicoCaleImageConstants.DEVICE_IMAGE,imageLatitude,imageLongitude);
                picoCaleImageList.add(picoCaleImage);
            }
        }
//        Log.i("---BOOLCOUNT-->", Double.valueOf(imageCount).toString());
        //returning the location based image list
        //return imageList;
        return picoCaleImageList;
    }

}
