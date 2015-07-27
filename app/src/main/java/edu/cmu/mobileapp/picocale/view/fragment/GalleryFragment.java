package edu.cmu.mobileapp.picocale.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        gridView = (GridView) view.findViewById(R.id.galleryGrid);

        Intent intent = getActivity().getIntent();
        String locationString = intent.getStringExtra("location");
        new DeviceImageFetcher(getActivity(),gridView).execute(locationString);
        return view;
    }

}