package edu.cmu.mobileapp.picocale.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import edu.cmu.mobileapp.picocale.R;

/**
 * Created by user on 7/25/2015.
 */
public class SettingsFragment extends android.support.v4.app.Fragment {

    private Button saveButton;
    private Switch notificationSwitch;
    private EditText userRadiusText;
    SharedPreferences sharedPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialisations
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        saveButton = (Button) rootView.findViewById(R.id.saveButton);
        notificationSwitch = (Switch) rootView.findViewById(R.id.notificationSwitch);
        userRadiusText = (EditText) rootView.findViewById(R.id.userRadiusValue);

        //default values
        notificationSwitch.setChecked(true);
        userRadiusText.setText("5");

        //Listeners
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("userRadius", Float.parseFloat(userRadiusText.getText().toString()));
                editor.putBoolean("notificationSetting",notificationSwitch.isChecked());
            }
        });

        return rootView;
    }
}
