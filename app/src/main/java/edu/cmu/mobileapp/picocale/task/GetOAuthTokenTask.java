package edu.cmu.mobileapp.picocale.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthInterface;

import edu.cmu.mobileapp.picocale.util.FlickrHelper;
import edu.cmu.mobileapp.picocale.view.fragment.CloudFragment;


/**
 * @author Sivaraman
 *
 */
public class GetOAuthTokenTask extends AsyncTask<String, Integer, OAuth> {



    private CloudFragment fragment;

    public GetOAuthTokenTask(CloudFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    protected OAuth doInBackground(String... params) {
        Log.i("Background","entered");
        String oauthToken = params[0];
        String oauthTokenSecret = params[1];
        String verifier = params[2];

        Flickr f = FlickrHelper.getInstance().getFlickr();
        OAuthInterface oauthApi = f.getOAuthInterface();
        try {
            return oauthApi.getAccessToken(oauthToken, oauthTokenSecret,
                    verifier);
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    protected void onPostExecute(OAuth result) {
        if (fragment != null) {
            fragment.onOAuthDone(result);
            //CloudFragment.getActivity().getApplication().oO
        }
    }


}
