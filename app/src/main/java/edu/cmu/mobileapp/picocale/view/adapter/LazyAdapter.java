package edu.cmu.mobileapp.picocale.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.GeoData;
import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.task.ImageDownloadTask;
import edu.cmu.mobileapp.picocale.util.ImageUtils;


public class LazyAdapter extends BaseAdapter {

    private Context context;
    private PhotoList photos;
    private static LayoutInflater inflater=null;
    private String oauthToken;
    private String oauthTokenSecret;

    public LazyAdapter(Context context, PhotoList d,String oauthToken, String oauthTokenSecret) {
       this.context=context;
        photos = d;
        this.oauthToken=oauthToken;
        this.oauthTokenSecret=oauthTokenSecret;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int count=getCount();
        Log.i("Photos count GetView:",Integer.toString(getCount()));
        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.flickrgridview_row, null);
        /*Flickr f = FlickrHelper.getInstance().getFlickrAuthed(oauthToken,
                oauthTokenSecret);*/

        TextView text=(TextView)vi.findViewById(R.id.textView);;
        ImageView image=(ImageView)vi.findViewById(R.id.imageView);
        Photo photo = photos.get(position);

       /* GeoData geoData= null;
        try {
            geoData = f.getGeoInterface().getLocation(photo.getId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FlickrException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("Geodata",Float.toString(geoData.getLatitude()));*/
        //new GetLocationTask(activity,text,oauthToken,oauthTokenSecret).execute(photo.getId());
        if (image != null){
            ImageDownloadTask task = new ImageDownloadTask(image);
            Drawable drawable = new ImageUtils.DownloadedDrawable(task);
            image.setImageDrawable(drawable);
            task.execute(photo.getSmallSquareUrl());
        }


        Log.i("Photo", photo.getSmallUrl());
        //text.setText(photo.getTitle());
        Log.i("Photo", photo.getId());
        //new DownloadImageTask(image).execute(photo.getSmallUrl());
        return vi;
    }
}