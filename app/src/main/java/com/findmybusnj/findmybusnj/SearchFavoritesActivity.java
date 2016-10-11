package com.findmybusnj.findmybusnj;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.net.HttpURLConnection;

public class SearchFavoritesActivity extends AppCompatActivity {
    // Used when search button is pressed
    private View.OnClickListener searchListener = new View.OnClickListener() {
        public void onClick(View v) {
            makePostRequest();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // adds back arrow to nav bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button search = (Button)findViewById(R.id.search_button);
        search.setOnClickListener(searchListener);
    }

    /**
     * Makes a post request to the server and notifies the ListView it needs to update with new data
     */
    private void makePostRequest() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // will contain the raw JSON repsonse as a string
        String busTimes = null;

        // Gets stop textfield along with contents
        TextView stop_input = (TextView) findViewById(R.id.stop_number_input);
        String stop = stop_input.getText().toString();

        if (stop.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("Stop Required")
                    .setMessage("Please enter a stop number before searching")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // no-op
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
