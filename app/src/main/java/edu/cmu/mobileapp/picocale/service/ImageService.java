package edu.cmu.mobileapp.picocale.service;

import android.app.Activity;

import java.util.List;

/**
 * Created by srikrishnan_suresh on 07/26/2015.
 */
public interface ImageService {
    public List<String> getImageList(Activity activity, String location);
}
