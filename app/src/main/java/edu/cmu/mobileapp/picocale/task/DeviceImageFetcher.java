package edu.cmu.mobileapp.picocale.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import java.util.List;

import edu.cmu.mobileapp.picocale.listener.DeviceImageLocationItemClickListener;
import edu.cmu.mobileapp.picocale.service.DeviceImageLocationServiceImpl;
import edu.cmu.mobileapp.picocale.service.DeviceImageServiceImpl;
import edu.cmu.mobileapp.picocale.service.ImageLocationService;
import edu.cmu.mobileapp.picocale.service.ImageService;
import edu.cmu.mobileapp.picocale.view.adapter.LocationListItemAdapter;

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
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(activity);
        progress.setMessage("Loading...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected void onPostExecute(List<String> locationList) {
        super.onPostExecute(locationList);
        progress.hide();
        Log.i("images>>>", locationList.toString());
    }

    @Override
    protected List<String> doInBackground(String... params) {
        String location = params[0];
        return imageService.getImageList(activity, location);
    }
}
