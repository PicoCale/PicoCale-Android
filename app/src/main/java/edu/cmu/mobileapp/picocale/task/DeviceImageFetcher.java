package edu.cmu.mobileapp.picocale.task;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.GridView;

import java.util.List;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.listener.GalleryItemClickListener;
import edu.cmu.mobileapp.picocale.service.DeviceImageServiceImpl;
import edu.cmu.mobileapp.picocale.service.ImageService;
import edu.cmu.mobileapp.picocale.view.adapter.ImageGridAdapter;
import edu.cmu.mobileapp.picocale.view.fragment.HomeFragment;

/**
 * Created by srikrishnan_suresh on 07/26/2015.
 */
public class DeviceImageFetcher extends AsyncTask<String, Void, List<String>> {
    private Activity activity;
    private GridView gridView;
    private ProgressDialog progress;
    private ImageService imageService = new DeviceImageServiceImpl();
    public DeviceImageFetcher(Activity activity, GridView gridView) {
        this.activity = activity;
        this.gridView = gridView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(activity);
        progress.setMessage(activity.getString(R.string.progressLoadingText));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected List<String> doInBackground(String... params) {
        String location = params[0];
        String type = params[1];
        if(type.equals("1"))
            return imageService.getImageList(activity, location);
        else if(type.equals("2"))
            return imageService.getLocationBasedImageList(activity);
        return null;
    }

    @Override
    protected void onPostExecute(List<String> locationList) {
        super.onPostExecute(locationList);
        gridView.setAdapter(new ImageGridAdapter(activity, locationList));
        gridView.setOnItemClickListener(new GalleryItemClickListener(activity, locationList, 1));
        progress.dismiss();
    }

    /*public void timerDelayRemoveDialog(long time, final ProgressDialog d){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                d.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                d.hide();
            }
        }, time);
    }*/
}
