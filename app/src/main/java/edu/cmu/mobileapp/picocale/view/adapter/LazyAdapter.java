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
        View vi = convertView;
        if(convertView == null)
            vi = inflater.inflate(R.layout.item_grid_image, null);
        ImageView image=(ImageView)vi.findViewById(R.id.gridImage);
        Photo photo = photos.get(position);
        if (image != null){
            ImageDownloadTask task = new ImageDownloadTask(image);
            Drawable drawable = new ImageUtils.DownloadedDrawable(task);
            image.setImageDrawable(drawable);
            task.execute(photo.getMediumUrl());
        }
        return vi;
    }
}