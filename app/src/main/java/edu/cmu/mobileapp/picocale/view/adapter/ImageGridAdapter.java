package edu.cmu.mobileapp.picocale.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.List;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.model.PicoCaleImage;

/**
 * Created by srikrishnan_suresh on 07/27/2015.
 */
public class ImageGridAdapter extends BaseAdapter {
    private Activity activity;
    private List<PicoCaleImage> locationList;
    private static LayoutInflater inflater = null;

    public ImageGridAdapter(Activity activity, List<PicoCaleImage> locationList) {
        this.activity = activity;
        this.locationList = locationList;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return locationList.size();

    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.item_grid_image, null);

        ImageView image = (ImageView) view.findViewById(R.id.gridImage);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        String filePath = locationList.get(position).getFilePath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        image.setImageBitmap(bitmap);
        return view;
    }
}
