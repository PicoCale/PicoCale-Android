package edu.cmu.mobileapp.picocale.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.listener.TabClickListener;
import edu.cmu.mobileapp.picocale.view.activity.MainActivity;


/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class MainFragment extends Fragment {

    private ActionBar actionBar;
    private ActionBar.Tab homeTab, cloudTab, settingsTab;
    private Fragment homeFragment;
    private Fragment cloudFragment;
    private Fragment settingsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initComponents();
        return view;
    }

    private void initComponents() {
        //get action bar reference
        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        //instantiate fragments
        homeFragment = new HomeFragment();
        cloudFragment = new CloudFragment();
        settingsFragment=new SettingsFragment();

        //instantiate the tab items
        homeTab = actionBar.newTab();
        homeTab.setIcon(R.drawable.icon_home);
        cloudTab = actionBar.newTab();
        cloudTab.setIcon(R.drawable.icon_cloud);
        settingsTab=actionBar.newTab();
        settingsTab.setIcon(R.drawable.icon_settings);

        //add listeners to the tab items
        homeTab.setTabListener(new TabClickListener(homeFragment, R.id.fragment_main));
        cloudTab.setTabListener(new TabClickListener(cloudFragment, R.id.fragment_main));
        settingsTab.setTabListener(new TabClickListener(settingsFragment,R.id.fragment_main));

        //add tab items to the action bar
        actionBar.addTab(homeTab);
        actionBar.addTab(cloudTab);
        actionBar.addTab(settingsTab);
    }
}