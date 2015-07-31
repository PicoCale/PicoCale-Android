package edu.cmu.mobileapp.picocale.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.service.DeviceImageServiceImpl;
import edu.cmu.mobileapp.picocale.service.ImageService;
import edu.cmu.mobileapp.picocale.task.DeviceImageFetcher;

/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class HomeFragment extends android.support.v4.app.Fragment {
    List<String> imagesWithinBoundary;
    GridView gridView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imagesWithinBoundary = new ArrayList<String>();
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        String type = "2";
        //Getting the images present within the radius boundary
        gridView = (GridView) rootView.findViewById(R.id.galleryGrid);
        new DeviceImageFetcher(getActivity(),gridView).execute("",type);
        return rootView;
    }
}
