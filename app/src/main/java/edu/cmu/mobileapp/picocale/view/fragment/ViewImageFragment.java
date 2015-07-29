package edu.cmu.mobileapp.picocale.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.task.DownloadImageTask;
import edu.cmu.mobileapp.picocale.task.ImageDownloadTask;
import edu.cmu.mobileapp.picocale.task.LoadImageTask;
import edu.cmu.mobileapp.picocale.util.FlushedInputStream;
import edu.cmu.mobileapp.picocale.util.ImageUtils;

/**
 * Created by srikrishnan_suresh on 07/27/2015.
 */
public class ViewImageFragment extends android.support.v4.app.Fragment {
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

        String imagePath = i.getStringExtra("imagePath");
        String imageURL = i.getStringExtra("imageURL");
        ImageView imageView = (ImageView) rootView.findViewById(R.id.full_image_view);

        if(imagePath!=null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        }
        else if(imageURL!=null){
            Log.i("-URL--->", imageURL);
            new LoadImageTask(getActivity(),imageView).execute(imageURL);
        }
        return rootView;
    }
}