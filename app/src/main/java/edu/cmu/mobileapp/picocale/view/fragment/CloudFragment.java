package edu.cmu.mobileapp.picocale.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cmu.mobileapp.picocale.R;

/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class CloudFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cloud, container, false);

        return rootView;
    }
}
