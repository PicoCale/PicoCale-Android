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
import edu.cmu.mobileapp.picocale.listener.GalleryItemClickListener;
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
    List<String> photoURLList;

    public LoadPhotoStreamTask(Activity activity,Context context, GridView gridView, Location pLocation,double pRadiusValue) {
        this.activity = activity;
        this.context=context;
        this.gridView = gridView;
        if(gridView==null)
        {
            Log.i("Gridview","null");
        }
        this.location = pLocation;
        this.radiusValue = pRadiusValue;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(activity,
                "", "Please wait..."); //$NON-NLS-1$ //$NON-NLS-2$
        mProgressDialog.setCanceledOnTouchOutside(true);
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected PhotoList doInBackground(OAuth... arg0) {
        Log.i("Entered", "BG");
        OAuthToken token = arg0[0].getToken();

        Flickr f = FlickrHelper.getInstance().getFlickrAuthed(token.getOauthToken(),
                token.getOauthTokenSecret());
        oauthToken=token.getOauthToken();
        oauthTokenSecret=token.getOauthTokenSecret();
        Log.i("Token Secret", token.getOauthTokenSecret());
        Set<String> extras = new HashSet<String>();
        extras.add("url_sq"); //$NON-NLS-1$
        extras.add("url_l"); //$NON-NLS-1$
        extras.add("views"); //$NON-NLS-1$
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
            //return f.getPeopleInterface().getPhotos(user.getId(), extras, 20, 1);
            photoURLList = new ArrayList<String>();
            PhotoList locationBasedList=new PhotoList();
            PhotoList photoList=f.getPeopleInterface().getPhotos(user.getId(), extras, 20, 1);
            Log.i("===Count",Integer.toString(photoList.size()));
            for(Photo photo:photoList)
            {
                if(photo!=null) {
                    try {
                        GeoData geoData = f.getGeoInterface().getLocation(photo.getId());
                        photoLatitude = geoData.getLatitude();
                        photoLongitude = geoData.getLongitude();

                        //printing out the values
                        Log.i("===>Photo Lat:", Double.toString(photoLatitude));
                        Log.i("===>Photo Lon:", Double.toString(photoLongitude));
                        Log.i("===>Min Lat:", Double.toString(minLatitude));
                        Log.i("===>Max Lat:", Double.toString(maxLatitude));
                        Log.i("===>Min Lon:", Double.toString(minLongitude));
                        Log.i("===>Max Lon:", Double.toString(maxLongitude));

                        //Deciding whether to display image or not
                        if ((photoLatitude >=minLatitude && photoLatitude <= maxLatitude) &&
                                (photoLongitude <=minLongitude && photoLongitude>=maxLongitude)){
                            Log.i("Photo ID Added:", photo.getId());
                            locationBasedList.add(photo);
                            photoURLList.add(photo.getLarge1600Url());
                        }
                        else{
                            Log.i("Sry!Photo ID Not Added:", photo.getId());
                        }

                    }
                    catch (FlickrException e)
                    {
                        Log.i("Exc",e.getMessage()+e.getErrorMessage());
                    }
                }
            }

//            return f.getPeopleInterface().getPhotos(user.getId(), extras, 20, 1);
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
            Log.i("Size:",Integer.toString(result.size()));
            LazyAdapter adapter = new LazyAdapter(context, result,oauthToken,oauthTokenSecret);
            if(gridView==null)
            {
                Log.i("Gridview","null");
            }
            if(adapter==null)
            {
                Log.i("Adapter","null");
            }
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new GalleryItemClickListener(activity, photoURLList, 2));
            mProgressDialog.dismiss();

        }
        else
        {
            Toast.makeText(activity,"No photos available",Toast.LENGTH_LONG).show();
        }
    }

}

