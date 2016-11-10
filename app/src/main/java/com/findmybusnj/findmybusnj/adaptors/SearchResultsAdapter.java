package com.findmybusnj.findmybusnj.adaptors;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.findmybusnj.findmybusnj.R;
import com.findmybusnj.findmybusnj.drawable.Circle;
import com.findmybusnj.findmybusnj.models.ResultDataModel;

import java.util.ArrayList;

/**
 * Created by davidaghassi on 10/20/16.
 */

public class SearchResultsAdapter extends ArrayAdapter<ResultDataModel> {
    public SearchResultsAdapter(Context context, ArrayList<ResultDataModel> results) {
        super(context, 0, results);
    }

    /**
     * Takaes in a custom data model and formats the list for the data provided
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data we are handling
        ResultDataModel result = getItem(position);

        // Check if a view already exist so we can reuse it
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_list_item, parent, false);
        }

        TextView routeTitle = (TextView) convertView.findViewById(R.id.routeTitle);
        TextView routeDetails = (TextView) convertView.findViewById(R.id.routeDetails);
        TextView circle = (TextView) convertView.findViewById(R.id.arrivalCircle);

        routeTitle.setText(result.getBusNumber());
        routeDetails.setText(result.getRoute());

        // Format the card for the given time
        int time = result.getTime();
        circle.setTextColor(Color.WHITE);

        if (result.isArriving()) {
            circle.setText("Arriving");
        } else if (result.isDelayed()) {
            circle.setText("Delayed");
        } else {
            circle.setText(new Integer(time).toString() + " min.");
        }

        if (result.isArriving()) {
            circle.setBackgroundColor(Color.parseColor("#43AEF9"));  // powder blue
        } else if (time > 0 && time <= 7) {
            circle.setBackgroundColor(Color.parseColor("#1D9C30"));  // green
        } else if (time > 7 && time <= 14) {
            circle.setBackgroundColor(Color.parseColor("#ED9132"));  // orange
        } else if (time > 14 || result.isDelayed()) {
            circle.setBackgroundColor(Color.parseColor("#CC1924"));  // red
        }

        return convertView;
    }
}
