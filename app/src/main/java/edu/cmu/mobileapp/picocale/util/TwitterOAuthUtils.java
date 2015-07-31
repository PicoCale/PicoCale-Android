package edu.cmu.mobileapp.picocale.util;

import android.content.SharedPreferences;

import edu.cmu.mobileapp.picocale.constants.TwitterOAuthConstants;

/**
 * Created by srikrishnan_suresh on 07/31/2015.
 */
public class TwitterOAuthUtils {
    public static void setInitPreferences(SharedPreferences preference) {
        SharedPreferences.Editor edit = preference.edit();
        edit.putString("CONSUMER_KEY", TwitterOAuthConstants.TWITTER_CONSUMER_KEY);
        edit.putString("CONSUMER_SECRET", TwitterOAuthConstants.TWITTER_CONSUMER_SECRET);
        edit.commit();
    }
}
