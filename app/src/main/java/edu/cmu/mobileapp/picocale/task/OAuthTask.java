package edu.cmu.mobileapp.picocale.task;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.auth.Permission;
import com.googlecode.flickrjandroid.oauth.OAuthToken;

import java.net.URL;

import edu.cmu.mobileapp.picocale.util.FlickrHelper;
import edu.cmu.mobileapp.picocale.view.fragment.CloudFragment;

public class OAuthTask extends AsyncTask<Void, Integer, String> {


    private static final Uri OAUTH_CALLBACK_URI = Uri.parse("flickrj-android-sample-oauth" + "://oauth"); //$NON-NLS-1$
    private CloudFragment cloudFragment;
    /**
     * The context.
     */
    //private Context mContext;

    /**
     * The progress dialog before going to the browser.
     */
    private ProgressDialog mProgressDialog;
    private Context mContext;
    /**
     * Constructor.
     *
     * @param fragment,Context
     */
    public OAuthTask(CloudFragment fragment,Context context) {
        super();
        this.cloudFragment=fragment;
        this.mContext=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected String doInBackground(Void... params) {
        try {
            Flickr f = FlickrHelper.getInstance().getFlickr();
            OAuthToken
                    oauthToken = f.getOAuthInterface().getRequestToken(
                    OAUTH_CALLBACK_URI.toString());
            saveTokenSecrent(oauthToken.getOauthTokenSecret());
            URL oauthUrl = f.getOAuthInterface().buildAuthenticationUrl(
                    Permission.READ, oauthToken);
            return oauthUrl.toString();
        } catch (Exception e) {

            return "error:" + e.getMessage(); //$NON-NLS-1$
        }
    }

    /**
     * Saves the oauth token secrent.
     *
     * @param tokenSecret
     */
    private void saveTokenSecrent(String tokenSecret) {
        //act.saveOAuthToken(null, null, null, tokenSecret);
        cloudFragment.saveOAuthToken(null, null, null, tokenSecret);
    }

    @Override
    protected void onPostExecute(String result) {
        final Dialog auth_dialog;
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (result != null && !result.startsWith("error") ) {
            cloudFragment.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                    .parse(result)));

        } else {
            Toast.makeText(cloudFragment.getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

}