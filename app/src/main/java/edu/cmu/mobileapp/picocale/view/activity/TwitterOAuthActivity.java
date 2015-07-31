package edu.cmu.mobileapp.picocale.view.activity;

import edu.cmu.mobileapp.picocale.constants.TwitterOAuthConstants;
import edu.cmu.mobileapp.picocale.task.LoadImageTask;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import edu.cmu.mobileapp.picocale.R;

/**
 * Created by srikrishnan_suresh on 07/31/2015.
 */
public class TwitterOAuthActivity extends ActionBarActivity {
    private Twitter twitter;
    private RequestToken requestToken = null;
    private AccessToken accessToken;
    private String oauth_url,oauth_verifier,profile_url;
    private Dialog auth_dialog;
    private WebView web;
    private SharedPreferences pref;
    private Bitmap bitmap;
    private String imagePath;
    private String imageURL;
    private String tweetStatus;
    private String userName="";

    private ImageView previewImage;
    private EditText tweetMessage;
    private TextView handlerName;
    private Button confirmTweetButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_oauth);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();

        pref=getApplicationContext().getSharedPreferences("Pref", 0);
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(pref.getString("CONSUMER_KEY", TwitterOAuthConstants.TWITTER_CONSUMER_KEY), pref.getString("CONSUMER_SECRET", TwitterOAuthConstants.TWITTER_CONSUMER_SECRET));

        initComponents();

        if(!isTwitterLoggedInAlready())
            getConfirmation();
        else
            showComponents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initComponents() {
        previewImage = (ImageView) findViewById(R.id.tweet_preview_image);

        tweetMessage = (EditText) findViewById(R.id.tweet_message);
        confirmTweetButton = (Button) findViewById(R.id.confirm_tweet_button);
        handlerName = (TextView) findViewById(R.id.user_handler_name);

        tweetStatus = "Sent using @PicoCale";
        tweetMessage.setText(tweetStatus);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");
        imageURL = intent.getStringExtra("imageURL");

        if(imagePath!=null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            previewImage.setImageBitmap(bitmap);
        }
        else if(imageURL!=null){
            Log.i("-URL--->", imageURL);
            new LoadImageTask(this, previewImage).execute(imageURL);
        }
    }

    private void getConfirmation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TwitterOAuthActivity.this);
        alertDialogBuilder.setTitle("Request Twitter Access");
        alertDialogBuilder.setMessage("The App would like to access your Twitter Account");


        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new TokenGetTask().execute();
            }
        });
        alertDialogBuilder.setNegativeButton("Don't Allow", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }

        });

        AlertDialog confirmDialog = alertDialogBuilder.create();
        confirmDialog.show();
    }

    private void showComponents() {
        previewImage.setVisibility(View.VISIBLE);
        tweetMessage.setVisibility(View.VISIBLE);
        confirmTweetButton.setVisibility(View.VISIBLE);
        confirmTweetButton.setOnClickListener(null);
        handlerName.setText("@"+pref.getString("SCREEN_NAME",""));
        //confirmTweetButton.setOnClickListener(new ConfirmTweetListener(this, tweetStatus, filePath, mediaType));
    }

    private boolean isTwitterLoggedInAlready() {
        return pref.getBoolean(TwitterOAuthConstants.PREF_KEY_TWITTER_LOGIN, false);
    }

    private class TokenGetTask extends AsyncTask<String, String, String> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(TwitterOAuthActivity.this);
            progress.setMessage("Loading. Please Wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }
        @Override
        protected String doInBackground(String... args) {

            try {
                requestToken = twitter.getOAuthRequestToken();
                oauth_url = requestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return oauth_url;
        }
        @Override
        protected void onPostExecute(String oauth_url) {
            progress.hide();
            if(oauth_url != null){
                auth_dialog = new Dialog(TwitterOAuthActivity.this);
                auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                auth_dialog.setContentView(R.layout.dialog_twitter_auth);
                web = (WebView)auth_dialog.findViewById(R.id.twitter_auth_view);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(oauth_url);
                web.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if (url.contains("oauth_verifier") && !authComplete){
                            authComplete = true;
                            Log.e("Url",url);
                            Uri uri = Uri.parse(url);
                            oauth_verifier = uri.getQueryParameter("oauth_verifier");

                            auth_dialog.dismiss();
                            new AccessTokenTask().execute();
                        }else if(url.contains("denied")){
                            auth_dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "You need to login to Tweet", Toast.LENGTH_SHORT).show();
                            finishActivity(2);

                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setCancelable(true);
            }else{
                Toast.makeText(getApplicationContext(), "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AccessTokenTask extends AsyncTask<String, String, Boolean> {
        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(TwitterOAuthActivity.this);
            progress.setMessage("Loading. Please Wait...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {

            try {
                accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
                SharedPreferences.Editor edit = pref.edit();
                edit.putString("ACCESS_TOKEN", accessToken.getToken());
                edit.putString("ACCESS_TOKEN_SECRET", accessToken.getTokenSecret());
                User user = twitter.showUser(accessToken.getUserId());
                profile_url = user.getOriginalProfileImageURL();
                edit.putString("NAME", user.getName());
                edit.putString("SCREEN_NAME", user.getScreenName());
                edit.putString("IMAGE_URL", user.getOriginalProfileImageURL());
                edit.putBoolean(TwitterOAuthConstants.PREF_KEY_TWITTER_LOGIN, true);
                edit.commit();
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return true;
        }
        @Override
        protected void onPostExecute(Boolean response) {
            progress.hide();
            if(response){
                showComponents();
            }
        }

    }
}
