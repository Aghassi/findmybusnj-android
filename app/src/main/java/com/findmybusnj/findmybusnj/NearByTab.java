package com.findmybusnj.findmybusnj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by davidaghassi on 9/30/16.
 *
 * Represents the view fragment for the "Near By" tab
 */

public class NearByTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nearby_tab_fragment, container, false);
        return view;
    }
}
