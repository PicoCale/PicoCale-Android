package edu.cmu.mobileapp.picocale.listener;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.cmu.mobileapp.picocale.model.PicoCaleImage;
import edu.cmu.mobileapp.picocale.task.UpdateTwitterStatusTask;
import edu.cmu.mobileapp.picocale.view.activity.TwitterOAuthActivity;

/**
 * Created by srikrishnan_suresh on 07/31/2015.
 */
public class PostTweetListener implements View.OnClickListener {
    private Activity activity;
    private EditText text;
    private AsyncTask<String,String,String> task;
    public  PicoCaleImage picoCaleImage;

    public PostTweetListener(Activity twitterOAuthActivity, EditText text, PicoCaleImage picoCaleImage) {
        this.activity = twitterOAuthActivity;
        this.text = text;
        this.picoCaleImage=picoCaleImage;
        this.task = new UpdateTwitterStatusTask(activity);
    }

    @Override
    public void onClick(View v) {
        String tweetStatus = String.valueOf(text.getText());
        task.execute(tweetStatus,picoCaleImage.getFilePath(),Double.toString(picoCaleImage.getLatitude()),Double.toString(picoCaleImage.getLongitude()));
    }
}
