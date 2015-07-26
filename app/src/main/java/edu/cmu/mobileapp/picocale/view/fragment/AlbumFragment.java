package edu.cmu.mobileapp.picocale.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.cmu.mobileapp.picocale.R;
import edu.cmu.mobileapp.picocale.task.LocalImageLocationFetcher;
import edu.cmu.mobileapp.picocale.util.GalleryLocationUtils;

/**
 * Created by srikrishnan_suresh on 25-07-2015.
 */
public class AlbumFragment extends android.support.v4.app.Fragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);

        final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefresh);
        listView = (ListView) rootView.findViewById(R.id.locationListView);

        new LocalImageLocationFetcher(getActivity(), listView).execute();

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new LocalImageLocationFetcher(getActivity(), listView).execute();
                pullToRefresh.setRefreshing(false);
            }
        });

        return rootView;
    }
}
