package edu.cmu.mobileapp.picocale.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.GeoData;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.constants.PicoCaleImageConstants;
import edu.cmu.mobileapp.picocale.listener.GalleryItemClickListener;
import edu.cmu.mobileapp.picocale.model.PicoCaleImage;
import edu.cmu.mobileapp.picocale.util.DistanceUtils;
import edu.cmu.mobileapp.picocale.util.FlickrHelper;
import edu.cmu.mobileapp.picocale.view.adapter.LazyAdapter;


public class LoadPhotoStreamTask extends AsyncTask<OAuth, Void, PhotoList> {

    private GridView gridView;
    private Activity activity;
    private Context context;
    String oauthToken;
    String oauthTokenSecret;
    ProgressDialog mProgressDialog;
    Location location;
    double currentLatitude, currentLongitude;
    double latitudeCorrectionFactor, longitudeCorrectionFactor;
    double minLatitude, maxLatitude;
    double minLongitude, maxLongitude;
    double radiusValue;
    double photoLatitude, photoLongitude;
    List<PicoCaleImage> photoURLList=new ArrayList<PicoCaleImage>();
    public PicoCaleImage picoCaleImage;

    public LoadPhotoStreamTask(Activity activity,Context context, GridView gridView, Location pLocation,double pRadiusValue) {
        this.activity = activity;
        this.context=context;
        this.gridView = gridView;
        this.location = pLocation;
        this.radiusValue = pRadiusValue;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(activity, "", activity.getString(R.string.progressLoadingText));
        mProgressDialog.setCanceledOnTouchOutside(true);
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected PhotoList doInBackground(OAuth... arg0) {
        OAuthToken token = arg0[0].getToken();

        Flickr f = FlickrHelper.getInstance().getFlickrAuthed(token.getOauthToken(), token.getOauthTokenSecret());
        oauthToken=token.getOauthToken();
        oauthTokenSecret=token.getOauthTokenSecret();
        Set<String> extras = new HashSet<String>();
        extras.add("url_sq");
        extras.add("url_l");
        extras.add("views");
        User user = arg0[0].getUser();
        try {

            //Getting the current Location
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            //Getting the min, max location values
            latitudeCorrectionFactor = DistanceUtils.getLatitudeCorrectionFactor(radiusValue);
            minLatitude = currentLatitude - latitudeCorrectionFactor;
            maxLatitude = currentLatitude + latitudeCorrectionFactor;

            longitudeCorrectionFactor = DistanceUtils.getLongitudeCorrectionFactor(radiusValue,minLatitude);
            minLongitude = currentLongitude + longitudeCorrectionFactor;
            maxLongitude = currentLongitude - longitudeCorrectionFactor;

            //Getting the list of photos
            PhotoList locationBasedList=new PhotoList();
            if(user!=null) {
                PhotoList photoList = f.getPeopleInterface().getPhotos(user.getId(), extras, 20, 1);

                for (Photo photo : photoList) {
                    if (photo != null) {
                        try {
                            GeoData geoData = f.getGeoInterface().getLocation(photo.getId());
                            photoLatitude = geoData.getLatitude();
                            photoLongitude = geoData.getLongitude();

                            //Deciding whether to display image or not
                            if ((photoLatitude >= minLatitude && photoLatitude <= maxLatitude) &&
                                    (photoLongitude <= minLongitude && photoLongitude >= maxLongitude)) {
                                locationBasedList.add(photo);
                                picoCaleImage = new PicoCaleImage(photo.getLargeUrl(), PicoCaleImageConstants.URL_IMAGE, photoLatitude, photoLongitude);
                                photoURLList.add(picoCaleImage);
                            }
                        } catch (FlickrException e) {
                            Log.i("Exc", e.getMessage() + e.getErrorMessage());
                        }
                    }
                }
            }
            return locationBasedList;

        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(PhotoList result) {
        if (result != null) {
            LazyAdapter adapter = new LazyAdapter(context, result,oauthToken,oauthTokenSecret);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new GalleryItemClickListener(activity,photoURLList, 2));
        }
        mProgressDialog.dismiss();
    }

}

