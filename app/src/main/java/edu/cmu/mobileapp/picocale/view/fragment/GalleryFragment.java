package edu.cmu.mobileapp.picocale.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.listener.TabClickListener;
import edu.cmu.mobileapp.picocale.task.DeviceImageFetcher;
import edu.cmu.mobileapp.picocale.view.activity.MainActivity;


/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class GalleryFragment extends Fragment {

    private GridView gridView;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        activity = getActivity();
        gridView = (GridView) view.findViewById(R.id.galleryGrid);

        Intent intent = activity.getIntent();
        String locationString = intent.getStringExtra("location");
        String type = "1";
        ((ActionBarActivity)activity).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((ActionBarActivity)activity).getSupportActionBar().setTitle(R.string.app_name);
        ((ActionBarActivity)activity).getSupportActionBar().setSubtitle(locationString);
        new DeviceImageFetcher(activity,gridView).execute(locationString,type);
        return view;
    }

}