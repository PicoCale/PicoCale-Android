package edu.cmu.mobileapp.picocale.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.util.GalleryLocationUtils;

/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class AlbumFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);
        GalleryLocationUtils.getGalleryLatLngList(getActivity());
        return rootView;
    }
}
