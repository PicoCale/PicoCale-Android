package edu.cmu.mobileapp.picocale.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import edu.cmu.mobileapp.picocale.view.activity.ViewImageActivity;

/**
 * Created by srikrishnan_suresh on 07/27/2015.
 */
public class GalleryItemClickListener implements OnItemClickListener  {

    private Activity activity;
    private List<String> locationList;

    public GalleryItemClickListener(Activity activity, List<String> locationList) {
        this.activity = activity;
        this.locationList = locationList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent viewMediaIntent = new Intent(activity.getApplicationContext(), ViewImageActivity.class);
        viewMediaIntent.putExtra("imagePath", locationList.get(position));
        activity.startActivity(viewMediaIntent);
    }
}
