package edu.cmu.mobileapp.picocale.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import edu.cmu.mobileapp.picocale.R;

/**
 * Created by srikrishnan_suresh on 07/26/2015.
 */
public class LocationListAdapter extends BaseAdapter {

    private Activity activity;
    private List<String> locationList;
    private static LayoutInflater inflater = null;

    public LocationListAdapter(Activity activity, List<String> locationList) {
        this.activity = activity;
        this.locationList = locationList;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return locationList.size();

    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.item_list_location, null);
        TextView fullNameText = (TextView)view.findViewById(R.id.locationNameText);
        String locationName = locationList.get(position);
        fullNameText.setText(locationName);
        return view;
    }
}
