package com.findmybusnj.findmybusnj.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.findmybusnj.findmybusnj.R;
import com.findmybusnj.findmybusnj.models.Favorite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by davidaghassi on 11/7/16.
 */

public class FavoritesAdapter extends ArrayAdapter<Favorite> {

    public FavoritesAdapter(Context context, List<Favorite> results) {
        super(context, 0, results);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the favorite we are handling
        Favorite favorite = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.favorite_list_item, parent, false);
        }

        TextView stopLabel = (TextView) convertView.findViewById(R.id.stopText);
        TextView routeLabel = (TextView) convertView.findViewById(R.id.routeText);

        stopLabel.setText(favorite.getStop());
        routeLabel.setText(favorite.getRoute());

        return convertView;
    }

    public void sort() {
        Collections.sort((List<Favorite>) this, new Comparator<Favorite>() {
            @Override
            public int compare(Favorite item1, Favorite item2) {
                return Integer.compare(item1.getFrequency(), item2.getFrequency());
            }
        });
    }
}
