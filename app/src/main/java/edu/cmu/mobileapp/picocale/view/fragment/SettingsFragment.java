package edu.cmu.mobileapp.picocale.view.fragment;

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

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.service.DeviceImageServiceImpl;
import edu.cmu.mobileapp.picocale.service.ImageService;
import edu.cmu.mobileapp.picocale.service.LocationService;

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
        //initialisations
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
//                sharedPref = PreferenceManager.getDefaultSharedPreferences();
                ImageService imageService = new DeviceImageServiceImpl();
                int count = imageService.getLocationBasedImageList(getActivity()).size();
                Boolean isImageAvailable = false;
                if(count>0)
                    isImageAvailable = true;
                else
                    isImageAvailable = false;
                notificationSettingValue = sharedPref.getBoolean("notificationSetting", true);
                boolean notifVal = notificationSwitch.isChecked();
                editor = sharedPref.edit();
                editor.putString("userRadius", userRadiusText.getText().toString());
                editor.putBoolean("notificationSetting", notifVal);
                editor.commit();
                Log.d("--CHK123--->", "B4:"+notificationSettingValue+"AFTER:"+ notifVal);
                if(notificationSettingValue==true && notifVal == false){
                    Log.d("--CHK123--->", "onClick: stoping srvice:"+notifVal);
                    getActivity().stopService(new Intent(getActivity(), LocationService.class));
                }
                else if(notificationSettingValue==false && notifVal == true){
                    Log.d("--CHK123--->", "onClick: starting srvice:"+notifVal);
                    getActivity().startService(new Intent(getActivity(), LocationService.class)
                            .putExtra("isImageAvailable", isImageAvailable)
                            .putExtra("imageCount", count)
                            .putExtra("notificationSetting",notifVal));
                }
                if (rootView != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                }
            }
        });

        return rootView;
    }
}
