package com.findmybusnj.findmybusnj.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.findmybusnj.findmybusnj.R;
import com.findmybusnj.findmybusnj.adaptors.SearchResultsAdapter;
import com.findmybusnj.findmybusnj.models.ResultDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
            if (response.isEmpty()) {
                ResultDataModel responseData = new ResultDataModel();
                responseData.isNoPrediction();
                array = new JSONArray(responseData);
            } else {
                array = new JSONArray(response);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Convert JSONArray to list of special objects
        ArrayList<ResultDataModel> results = new ArrayList<ResultDataModel>(array.length());
        for (int i = 0; i < array.length(); i++) {
            ResultDataModel responseData = new ResultDataModel();

            try {
                JSONObject jsonObject = (JSONObject) array.get(i);

                responseData.setBusNumber(jsonObject.get("rd").toString());
                responseData.setRoute(jsonObject.get("fd").toString());

                // Get the time, or determine otherwise
                if (!jsonObject.get("pu").toString().equals("MINUTES")) {
                    switch (jsonObject.get("pu").toString()){
                        case "APPROACHING":
                            responseData.setArriving(true);
                        case "DELAYED":
                            responseData.setDelayed(true);
                    }
                } else {
                    responseData.setTime(new Integer(jsonObject.get("pt").toString()));
                }

                results.add(responseData);
                Log.d("Response object:", responseData.getBusNumber() + ", " + responseData.getRoute() + ", " + "time: " + responseData.getTime());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        // Update list
        ListView resultList = (ListView) getView().findViewById(R.id.stop_list);
        // Create custom array adaptor
        SearchResultsAdapter adapter = new SearchResultsAdapter(this.getContext(), results);
        resultList.setAdapter(adapter);
    }
}
