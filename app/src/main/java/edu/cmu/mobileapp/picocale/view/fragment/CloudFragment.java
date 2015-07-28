package edu.cmu.mobileapp.picocale.view.fragment;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

import edu.cmu.mobileapp.picocale.task.GetOAuthTokenTask;
import edu.cmu.mobileapp.picocale.task.LoadPhotoStreamTask;
import java.util.Locale;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.task.OAuthTask;

/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class CloudFragment extends android.support.v4.app.Fragment {

    public static final String CALLBACK_SCHEME = "flickrj-android-sample-oauth"; //$NON-NLS-1$
    public static final String PREFS_NAME = "flickrj-android-sample-pref"; //$NON-NLS-1$
    public static final String KEY_OAUTH_TOKEN = "flickrj-android-oauthToken"; //$NON-NLS-1$
    public static final String KEY_TOKEN_SECRET = "flickrj-android-tokenSecret"; //$NON-NLS-1$
    public static final String KEY_USER_NAME = "flickrj-android-userName"; //$NON-NLS-1$
    public static final String KEY_USER_ID = "flickrj-android-userId"; //$NON-NLS-1$
    static final String PREF_KEY_LOGIN = "isLoggedIn";
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cloud, container, false);

        gridView=(GridView)rootView.findViewById(R.id.flickr_Grid);

        OAuth oauth = getOAuthToken();

        if(!isLoggedInAlready()) {

            Toast.makeText(getActivity().getApplicationContext(),"Not logged in",Toast.LENGTH_LONG).show();
            if (oauth == null || oauth.getUser() == null) {
                new OAuthTask(this,getActivity().getApplicationContext()).execute();
            }
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(),"Logged in",Toast.LENGTH_LONG).show();
            load(oauth,gridView);
        }
        return rootView;
    }
    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     * */
    private boolean isLoggedInAlready() {
        // return twitter login status from Shared Preferences
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(PREF_KEY_LOGIN, false);
    }

    public OAuth getOAuthToken() {
        //Restore preferences
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
        String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);

        if (oauthTokenString == null && tokenSecret == null) {
            return null;
        }

        OAuth oauth = new OAuth();
        String userName = settings.getString(KEY_USER_NAME, null);
        String userId = settings.getString(KEY_USER_ID, null);

        if (userId != null) {
            User user = new User();
            Log.i("User id",userId);
            user.setUsername(userName);
            user.setId(userId);
            oauth.setUser(user);
        }
        else
        {
            Log.i("User id:","null");
        }
        OAuthToken oauthToken = new OAuthToken();
        oauth.setToken(oauthToken);
        oauthToken.setOauthToken(oauthTokenString);
        oauthToken.setOauthTokenSecret(tokenSecret);
        return oauth;
    }
    //Loading the images
    private void load(OAuth oauth,GridView gridView) {
        if (oauth != null) {
            if(gridView==null) {
                Log.i("GridView", "null");
            }
            else
            {
                Log.i("GridView", " not null");
            }
            Log.i("Oauth",oauth.getToken().toString());
            Log.i("Oauth",oauth.getToken().toString());
            new LoadPhotoStreamTask(getActivity(),getActivity().getApplicationContext(),gridView).execute(oauth);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("On", "Resume!");
        Intent intent = getActivity().getIntent();
        String scheme = intent.getScheme();
        OAuth savedToken = getOAuthToken();
        if (CALLBACK_SCHEME.equals(scheme) && (savedToken == null || savedToken.getUser() == null)) {
            Uri uri = intent.getData();
            String query = uri.getQuery();
            String[] data = query.split("&");
            if (data != null && data.length == 2) {
                String oauthToken = data[0].substring(data[0].indexOf("=") + 1);
                String oauthVerifier = data[1]
                        .substring(data[1].indexOf("=") + 1);
                OAuth oauth = getOAuthToken();
                if (oauth != null && oauth.getToken() != null && oauth.getToken().getOauthTokenSecret() != null) {
                    GetOAuthTokenTask task = new GetOAuthTokenTask(this);
                    task.execute(oauthToken, oauth.getToken().getOauthTokenSecret(), oauthVerifier);
                }
            }
        }

    }

    public void saveOAuthToken(String userName, String userId, String token, String tokenSecret) {

        SharedPreferences sp = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_OAUTH_TOKEN, token);
        editor.putString(KEY_TOKEN_SECRET, tokenSecret);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_ID, userId);
        editor.putBoolean(PREF_KEY_LOGIN, true);
        editor.commit();
    }

    public void onOAuthDone(OAuth result) {
        Log.i("OAD","called");
        if (result == null) {
            Toast.makeText(getActivity().getApplicationContext(), "Authorization failed",Toast.LENGTH_LONG).show();
        } else {
            User user = result.getUser();
            OAuthToken token = result.getToken();
            if (user == null || user.getId() == null || token == null || token.getOauthToken() == null || token.getOauthTokenSecret() == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Authorization failed", Toast.LENGTH_LONG).show();
                return;
            }
            String message = String.format(Locale.US, "Authorization Succeed: user=%s, userId=%s, oauthToken=%s, tokenSecret=%s",user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
            saveOAuthToken(user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            load(result,gridView);
        }
    }
}
