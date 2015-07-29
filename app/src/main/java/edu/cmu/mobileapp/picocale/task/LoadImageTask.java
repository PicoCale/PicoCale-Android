package edu.cmu.mobileapp.picocale.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.mobileapp.picocale.R;

/**
 * Created by Jayakumaur on 29-07-2015.
 */
public class LoadImageTask extends AsyncTask<String, String, String> {
    private Activity activity;
    private ProgressDialog progress;
    private ImageView imageView;

    public LoadImageTask(Activity activity,ImageView imageView) {
        this.activity = activity;
        this.imageView=imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(activity);
        progress.setMessage("Loading Media...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected String doInBackground(String... params) {

        URL url = null;
        final Bitmap bitmap;
        String URL = params[0];

        try {
            url = new URL(URL);
//            url = new URL("https://www.google.com/search?site=&tbm=isch&source=hp&biw=1366&bih=667&q=book&oq=book&gs_l=img.3...3568.4123.0.4409.4.4.0.0.0.0.0.0..0.0.ckpsrh...0...1.1.64.img..4.0.0.GyxTwyOYdzQ#imgrc=GoDRQc1PXFJb3M%3A");
            Log.i("--->", url.toString());
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    imageView.setImageBitmap(bitmap);
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(String string) {
        progress.dismiss();
    }
}
