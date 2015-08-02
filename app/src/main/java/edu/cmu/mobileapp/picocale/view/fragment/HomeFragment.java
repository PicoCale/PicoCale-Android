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
        //Getting the radius value from the preferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences("PicoCale", 0);
        String radius = sharedPref.getString("userRadius","2");

        //Getting the images present within the radius boundary
        gridView = (GridView) rootView.findViewById(R.id.galleryGrid);
        new DeviceImageFetcher(getActivity(),gridView).execute("",type);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setSubtitle("Photos within "+ radius + " mile radius");
        return rootView;
    }
}
