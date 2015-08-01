package edu.cmu.mobileapp.picocale.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import edu.cmu.mobileapp.picocale.constants.TwitterOAuthConstants;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by srikrishnan_suresh on 07/31/2015.
 */
public class UpdateTwitterStatusTask extends AsyncTask<String, String, String> {
    private Activity activity;
    private ProgressDialog progress;

    public UpdateTwitterStatusTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPostExecute(String string) {
        progress.hide();
        Toast.makeText(activity, "Tweet posted.", Toast.LENGTH_SHORT).show();
        activity.finish();
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog(activity);
        progress.setMessage("Posting Tweet, Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String tweetStatus = params[0];
        String filePath = params[1];
        File file = new File(filePath);
        StatusUpdate update = new StatusUpdate(tweetStatus);
        update.setMedia(file);

        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TwitterOAuthConstants.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TwitterOAuthConstants.TWITTER_CONSUMER_SECRET);

        SharedPreferences preferences = activity.getSharedPreferences("Pref", 0);
        String accessToken = preferences.getString("ACCESS_TOKEN", "");
        String accessTokenSecret = preferences.getString("ACCESS_TOKEN_SECRET", "");

        AccessToken token = new AccessToken(accessToken, accessTokenSecret);
        Twitter twitter = new TwitterFactory(builder.build()).getInstance(token);

        try {
            twitter4j.Status response = twitter.updateStatus(update);
            Log.i("response", response.toString());
        } catch (TwitterException e) {
            e.printStackTrace();
            Log.i("Error Code", e.getErrorCode() + "");
            Log.i("Error Message", e.getErrorMessage() + "");
        }
        return null;
    }
}
