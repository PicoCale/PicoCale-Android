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

import java.util.HashSet;
import java.util.Set;

import edu.cmu.mobileapp.picocale.util.FlickrHelper;
import edu.cmu.mobileapp.picocale.view.adapter.LazyAdapter;


public class LoadPhotoStreamTask extends AsyncTask<OAuth, Void, PhotoList> {

    private GridView gridView;
    private Activity activity;
    private Context context;
    String oauthToken;
    String oauthTokenSecret;
    ProgressDialog mProgressDialog;

    public LoadPhotoStreamTask(Activity activity,Context context, GridView gridView) {
        this.activity = activity;
        this.context=context;
        this.gridView = gridView;
        if(gridView==null)
        {
            Log.i("Gridview","null");
        }
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
            //Getting the list of photos
            //return f.getPeopleInterface().getPhotos(user.getId(), extras, 20, 1);
            PhotoList locationBasedList=new PhotoList();
            PhotoList photoList=f.getPeopleInterface().getPhotos(user.getId(), extras, 20, 1);
            LocationManager locationManager=(LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("Count",Integer.toString(photoList.size()));
            for(Photo photo:photoList)
            {
                if(photo!=null) {
                    try {
                        GeoData geoData = f.getGeoInterface().getLocation(photo.getId());
                        Log.i("Photo Lat:", Float.toString(geoData.getLatitude()));
//                        Log.i("--------->",String.valueOf(location.getLatitude()));
//                        double maxLatitude = location.getLatitude();
//                        double minLat = location.getLatitude() - 1;
//                        if (geoData.getLatitude() <= maxLatitude) {
                            Log.i("Photo ID Added:", photo.getId());
                            locationBasedList.add(photo);
//                        }
                    }
                    catch (FlickrException e)
                    {
                        Log.i("Exc",e.getMessage()+e.getErrorMessage());
                    }
                }
            }

            return f.getPeopleInterface().getPhotos(user.getId(), extras, 20, 1);
            //return locationBasedList;

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
            mProgressDialog.dismiss();
        }
        else
        {
            Toast.makeText(activity,"No photos available",Toast.LENGTH_LONG).show();
        }
    }

}

