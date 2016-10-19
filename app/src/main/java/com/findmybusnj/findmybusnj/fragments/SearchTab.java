package com.findmybusnj.findmybusnj.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.findmybusnj.findmybusnj.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by davidaghassi on 9/30/16.
 *
 * Represents the view fragment for the "Search" tab
 */

public class SearchTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_tab_fragment, container, false);

        String[] listItems = new String[1];
        listItems[0] = "Tap on the search icon to begin";

        // Set the adaptor on the list
        ArrayAdapter adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, listItems);
        ListView resultList = (ListView) view.findViewById(R.id.stop_list);
        resultList.setAdapter(adapter);

        return view;
    }

    public void updateListView(String response) {
        JSONArray array= null;
        try {
            array = new JSONArray(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Convert JSONArray to list of special objects

        // Update list
        ListView resultList = (ListView) getView().findViewById(R.id.stop_list);
        ArrayAdapter adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1);
        resultList.setAdapter(adapter);
    }
}
