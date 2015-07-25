package edu.cmu.mobileapp.picocale.listener;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;



/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class TabClickListener implements ActionBar.TabListener {
    private android.support.v4.app.Fragment fragment;
    private int viewId;

    public TabClickListener(android.support.v4.app.Fragment fragment, int viewId) {
        this.fragment = fragment;
        this.viewId = viewId;

    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.replace(viewId, fragment);
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(fragment);
    }
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }
}