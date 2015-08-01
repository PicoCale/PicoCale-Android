package edu.cmu.mobileapp.picocale.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.task.LoadImageTask;
import edu.cmu.mobileapp.picocale.util.FlushedInputStream;
import edu.cmu.mobileapp.picocale.util.TwitterOAuthUtils;
import edu.cmu.mobileapp.picocale.view.activity.TwitterOAuthActivity;

/**
 * Created by srikrishnan_suresh on 07/27/2015.
 */
public class ViewImageFragment extends android.support.v4.app.Fragment {
    private SharedPreferences preference;
    String imagePath;
    String imageURL;
    double imageLatitude;
    double imageLongitude;

    private static Bitmap downloadBitmap(String stringUrl) {
        URL url = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(true);
            inputStream = connection.getInputStream();

            return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
        } catch (Exception e) {
            Log.w("--->>", "Error while retrieving bitmap from " + stringUrl, e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_image, container, false);

        Intent i = getActivity().getIntent();

        imagePath = i.getStringExtra("imagePath");
        imageURL = i.getStringExtra("imageURL");
        imageLatitude=i.getDoubleExtra("imageLatitude",0.0);
        imageLongitude=i.getDoubleExtra("imageLongitude",0.0);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.full_image_view);

        if(imagePath!=null) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }
        else if(imageURL!=null){
            Log.i("-URL--->", imageURL);
            new LoadImageTask(getActivity(),imageView).execute(imageURL);
        }

        setHasOptionsMenu(true);
        return rootView;
    }
    @Override
    public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_view_image, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.getActivity().finish();
                return true;
            case R.id.tweet_image:
                Activity activity = getActivity();
                preference=activity.getApplicationContext().getSharedPreferences("MyPref", 0);
                TwitterOAuthUtils.setInitPreferences(preference);
                Intent twitterIntent = new Intent(activity.getApplicationContext(), TwitterOAuthActivity.class);
                twitterIntent.putExtra("imagePath",imagePath);
                twitterIntent.putExtra("imageURL",imageURL);
                twitterIntent.putExtra("imageLatitude",imageLatitude);
                twitterIntent.putExtra("imageLongitude",imageLongitude);
                activity.startActivity(twitterIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}