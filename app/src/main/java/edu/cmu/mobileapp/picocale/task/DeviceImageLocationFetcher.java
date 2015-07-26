package edu.cmu.mobileapp.picocale.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ListView;

import java.util.List;

import edu.cmu.mobileapp.picocale.service.DeviceImageLocationServiceImpl;
import edu.cmu.mobileapp.picocale.service.ImageLocationService;
import edu.cmu.mobileapp.picocale.view.adapter.LocationListItemAdapter;

/**
 * Created by srikrishnan_suresh on 07/26/2015.
 */
public class DeviceImageLocationFetcher extends AsyncTask<String, Void, List<String>> {
    private Activity activity;
    private ListView listView;
    private ProgressDialog progress;
    ImageLocationService locationService = new DeviceImageLocationServiceImpl();
    public DeviceImageLocationFetcher(Activity activity, ListView listView) {
        this.activity = activity;
        this.listView = listView;
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
        listView.setAdapter(new LocationListItemAdapter(activity, locationList));
//        listView.setOnItemClickListener(new TweetItemClickListener(activity, locationList));
    }

    @Override
    protected List<String> doInBackground(String... params) {
        return locationService.getLocationAddressList(activity);
    }
}
