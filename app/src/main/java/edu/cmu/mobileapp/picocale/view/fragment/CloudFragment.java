package edu.cmu.mobileapp.picocale.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import edu.cmu.mobileapp.picocale.util.LocationUtils;
import edu.cmu.mobileapp.picocale.util.NetworkUtils;

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
    LocationManager lm;
    Location location;
    GridView gridView;
    double currentLatitude,currentLongitude;
    Criteria c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cloud, container, false);

        gridView=(GridView)rootView.findViewById(R.id.flickr_Grid);

        //To Check for internet connectivity
        if(!NetworkUtils.isOnline(getActivity())){
            Toast.makeText(getActivity().getApplicationContext(),getString(R.string.NetworkAbsent_Message),Toast.LENGTH_LONG).show();
        }

        //Displaying the alert dialog box for the user
        //to tell about privacy setting
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title
        alertDialogBuilder.setTitle(getString(R.string.alertDialogTitle));

        if(!isLoggedInAlready()) {
            // Getting user permission for
            alertDialogBuilder
                    .setMessage(getString(R.string.app_name)+" "+getString(R.string.alertDialogMessage))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.alertDialogButtonAllow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, proceed to flickr auth
                            //Obtaining the authToken
                            OAuth oauth = getOAuthToken();
                            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.NotLoggedIn_Message), Toast.LENGTH_LONG).show();
                            if (oauth == null || oauth.getUser() == null) {
                                new OAuthTask(CloudFragment.this, getActivity().getApplicationContext()).execute();
                            }
                        }
                    });
                    alertDialogBuilder.setNegativeButton(getString(R.string.alertDialogButtonDontAllow), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });
            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        else {
            OAuth oauth = getOAuthToken();
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.LoggedIn_Message), Toast.LENGTH_SHORT).show();
            load(oauth, gridView);
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
//            Log.i("User id",userId);
            user.setUsername(userName);
            user.setId(userId);
            oauth.setUser(user);
        }
        else
        {
//            Log.i("User id:","null");
        }
        OAuthToken oauthToken = new OAuthToken();
        oauth.setToken(oauthToken);
        oauthToken.setOauthToken(oauthTokenString);
        oauthToken.setOauthTokenSecret(tokenSecret);
        return oauth;
    }
    //Loading the images
    private void load(OAuth oauth,GridView gridView) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("PicoCale", 0);
        String radiusValue = sharedPref.getString("userRadius", "5");
//        Boolean notificationval = sharedPref.getBoolean("notificationSetting",true);
        if (oauth != null) {
            if(gridView==null) {
                Log.i("GridView", "null");
            }
            else
            {
                Log.i("GridView", " not null");
            }

            //Getting the current Location
            location = LocationUtils.getCurrentLocation(getActivity());

            //Obtains the current latitude and longitude
            if(location !=null){
                currentLatitude = location.getLatitude();
                currentLongitude = location.getLongitude();
            }
            else
            {
                //To Display a toast message upon absence of GPS Provider
                Context context = getActivity().getApplicationContext();
                String toastMessageText = getResources().getString(R.string.GPSAbsent_Message);
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context,toastMessageText,duration);
                toast.show();
            }

//            Log.i("-->>/",Double.toString(currentLatitude));
//            Log.i("-->>/",Double.toString(currentLongitude));

            //Loading the Flickr photo stream
            new LoadPhotoStreamTask(getActivity(),getActivity().getApplicationContext(),gridView,location,Double.parseDouble(radiusValue)).execute(oauth);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.i("On", "Resume!");
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
        if(userId!=null) {
            editor.putBoolean(PREF_KEY_LOGIN, true);
        }
        editor.commit();
    }

    public void onOAuthDone(OAuth result) {
        if (result == null) {
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.authorizationFail),Toast.LENGTH_LONG).show();
        } else {
            User user = result.getUser();
            OAuthToken token = result.getToken();
            if (user == null || user.getId() == null || token == null || token.getOauthToken() == null || token.getOauthTokenSecret() == null) {
                Toast.makeText(getActivity().getApplicationContext(), getString(R.string.authorizationFail), Toast.LENGTH_LONG).show();
                return;
            }
//            String message = String.format(Locale.US, "Authorization Succeed: user=%s, userId=%s, oauthToken=%s, tokenSecret=%s",user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            String message = getString(R.string.authorizationSuccess);
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
            saveOAuthToken(user.getUsername(), user.getId(), token.getOauthToken(), token.getOauthTokenSecret());
            load(result,gridView);
        }
    }
}
