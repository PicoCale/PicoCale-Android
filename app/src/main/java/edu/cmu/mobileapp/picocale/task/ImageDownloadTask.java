package edu.cmu.mobileapp.picocale.task;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import edu.cmu.mobileapp.picocale.util.ImageCache;
import edu.cmu.mobileapp.picocale.util.ImageUtils;


/**
 * Represents the image download task which takes an image url as the parameter,
 * after the download, set the bitmap to an associated <code>ImageView</code>.
 *
 * @author charles
 */
public class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
    //private static final Logger logger = LoggerFactory.getLogger(ImageDownloadTask.class);
    private WeakReference<ImageView> imgRef = null;
    private String mUrl;


    public ImageDownloadTask(ImageView imageView) {
        this.imgRef = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        mUrl = params[0];
        Bitmap image = ImageCache.getFromCache(mUrl);
        if (image != null) {
            return image;
        }
        return ImageUtils.downloadImage(mUrl);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (this.isCancelled()) {
            result = null;
            return;
        }

        ImageCache.saveToCache(this.mUrl, result);
        if (imgRef != null) {
            ImageView imageView = imgRef.get();
            ImageDownloadTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
            // Change bitmap only if this process is still associated with it
            // Or if we don't use any bitmap to task association
            // (NO_DOWNLOADED_DRAWABLE mode)
            if (this == bitmapDownloaderTask && bitmapDownloaderTask != null ) {
                imageView.setImageBitmap(result);
            }
        }

    }

    /**
     * This method name should be changed later, for sometimes, it will return
     * photo id.
     *
     * @return
     */
    public String getUrl() {
        return this.mUrl;
    }

    /**
     * @param imageView
     *            Any imageView
     * @return Retrieve the currently active download task (if any) associated
     *         with this imageView. null if there is no such task.
     */
    private ImageDownloadTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof ImageUtils.DownloadedDrawable) {
                ImageUtils.DownloadedDrawable downloadedDrawable = (ImageUtils.DownloadedDrawable) drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }
}
