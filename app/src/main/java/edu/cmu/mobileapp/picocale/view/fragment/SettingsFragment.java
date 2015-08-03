package edu.cmu.mobileapp.picocale.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.service.DeviceImageServiceImpl;
import edu.cmu.mobileapp.picocale.service.ImageService;
import edu.cmu.mobileapp.picocale.service.LocationService;
import edu.cmu.mobileapp.picocale.util.LocationUtils;

/**
 * Created by user on 7/25/2015.
 */
public class SettingsFragment extends android.support.v4.app.Fragment {

    private Button saveButton;
    private Switch notificationSwitch;
    private EditText userRadiusText;
    private SharedPreferences.Editor editor;
    private String userRadiusSettingValue;
    private boolean notificationSettingValue;
    SharedPreferences sharedPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initializations
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        sharedPref = getActivity().getSharedPreferences("PicoCale", 0);
        saveButton = (Button) rootView.findViewById(R.id.saveButton);

        //Setting up the elements
        notificationSwitch = (Switch) rootView.findViewById(R.id.notificationSwitch);
        userRadiusText = (EditText) rootView.findViewById(R.id.userRadiusValue);

        //default values for the UI elements
        notificationSettingValue = sharedPref.getBoolean("notificationSetting", true);
        notificationSwitch.setChecked(notificationSettingValue);
        userRadiusSettingValue = sharedPref.getString("userRadius","5");
        userRadiusText.setText(userRadiusSettingValue);

        //Listeners
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                notificationSettingValue = sharedPref.getBoolean("notificationSetting", true);
                boolean notifVal = notificationSwitch.isChecked();

                //Saving the values to preference
                editor = sharedPref.edit();
                editor.putString("userRadius", userRadiusText.getText().toString());
                editor.putBoolean("notificationSetting", notifVal);
                editor.commit();

                //Deciding what to do
                if(notificationSettingValue==true && notifVal == false){
                    //Notification Setting turned from ON to OFF
                    //Stop Location service
                    getActivity().stopService(new Intent(getActivity(), LocationService.class));
                }
                else if(notificationSettingValue==false && notifVal == true){
                    //Notification Setting turned from OFF to ON
                    //Start Location service
                    getActivity().startService(new Intent(getActivity(), LocationService.class));
                }
                if (rootView != null) {
                    //After activities completed, bring down soft keyboard
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                }

                //Acknowledgement Toast message
                Toast.makeText(getActivity().getApplicationContext(),"Settings Saved",Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
