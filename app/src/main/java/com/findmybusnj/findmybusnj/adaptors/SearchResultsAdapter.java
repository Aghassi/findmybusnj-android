package com.findmybusnj.findmybusnj.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.findmybusnj.findmybusnj.R;
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
        ResultDataModel result = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result_list_item, parent, false);
        }

        return convertView;
    }
}
