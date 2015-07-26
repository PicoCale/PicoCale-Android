package edu.cmu.mobileapp.picocale.listener;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

import java.util.List;

/**
 * Created by srikrishnan_suresh on 07/26/2015.
 */
public class DeviceImageLocationItemClickListener implements AdapterView.OnItemClickListener {

    private Activity activity;
    private List<String> locationList;
    public DeviceImageLocationItemClickListener(Activity activity, List<String> locationList) {
        this.activity = activity;
        this.locationList = locationList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String location = locationList.get(position);


    }
}
