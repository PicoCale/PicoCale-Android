package edu.cmu.mobileapp.picocale.service;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import java.util.List;

import edu.cmu.mobileapp.picocale.model.PicoCaleImage;

/**
 * Created by srikrishnan_suresh on 07/26/2015.
 */
public interface ImageService {
    public List<PicoCaleImage> getImageList(Activity activity, String location);
    public List<PicoCaleImage> getLocationBasedImageList(Context context, Location location);
}
