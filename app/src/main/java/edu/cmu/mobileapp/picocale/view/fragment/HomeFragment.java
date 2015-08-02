package edu.cmu.mobileapp.picocale.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.constants.NotificationConstants;
import edu.cmu.mobileapp.picocale.service.DeviceImageServiceImpl;
import edu.cmu.mobileapp.picocale.service.ImageService;
import edu.cmu.mobileapp.picocale.task.DeviceImageFetcher;

/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class HomeFragment extends android.support.v4.app.Fragment {
    List<String> imagesWithinBoundary;
    GridView gridView;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imagesWithinBoundary = new ArrayList<String>();
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        String type = "2";
//        ---------------------------
        ImageService imageService = new DeviceImageServiceImpl();
        int count = imageService.getLocationBasedImageList(getActivity()).size();
        Boolean isImageAvailable = false;
        if(count>0)
            isImageAvailable = true;
        else
            isImageAvailable = false;

        //Getting the radius value from the preferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("PicoCale", 0);
        //editor = sharedPref.edit();
        boolean notificationSetting = sharedPref.getBoolean("notificationSetting", true);
//        Log.i("--NOTIF1-->", notificationSetting + "--" + sharedPref1.getString("notificationFlag", "DEF"));
        Log.i("--NOTIF1-->", notificationSetting + "--" + NotificationConstants.notificationFlag);
        Intent serviceIntent=null;
        if(notificationSetting) {
            //Calling the LocationService Class
            Log.i("--NOTIF2-->","Inside TRUE");
            serviceIntent = new Intent("edu.cmu.mobileapp.picocale.service.LocationService");
            serviceIntent.putExtra("isImageAvailable", isImageAvailable);
            serviceIntent.putExtra("imageCount", count);
            serviceIntent.putExtra("notificationSetting",notificationSetting);
//            editor.putString("notificationFlag", "1");
            NotificationConstants.notificationFlag = 1;
            getActivity().startService(serviceIntent);
//            Log.i("--NOTIFLAG2-->", sharedPref1.getString("notificationFlag", "DEF"));
            Log.i("--NOTIFLAG2-->", NotificationConstants.notificationFlag+"");
        }
//        else if(!notificationSetting && sharedPref1.getString("notificationFlag","0").equals("1")){
        else if(!notificationSetting && NotificationConstants.notificationFlag==1 && serviceIntent!=null){
            Log.i("--NOTIF3-->","Inside FALSE");
            getActivity().stopService(serviceIntent);
            NotificationConstants.notificationFlag=0;
        }
//      --------------------------------
        String radius = sharedPref.getString("userRadius","2");

        //Getting the images present within the radius boundary
        gridView = (GridView) rootView.findViewById(R.id.galleryGrid);
        new DeviceImageFetcher(getActivity(),gridView).execute("",type);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle("Photos within "+ radius + " mile radius");
        return rootView;
    }
}
