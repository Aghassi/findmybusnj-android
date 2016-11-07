package com.findmybusnj.findmybusnj.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.findmybusnj.findmybusnj.R;
import com.findmybusnj.findmybusnj.handlers.DatabaseHandler;
import com.findmybusnj.findmybusnj.models.Favorite;
import com.goebl.david.Webb;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

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

        Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(searchListener);

        ListView listView = (ListView) findViewById(R.id.favorite_list_view);
        DatabaseHandler handler = new DatabaseHandler(this);
        List<Favorite> favoriteArrayList = handler.getAllFavorites();
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteArrayList);
        listView.setAdapter(adapter);
    }

    /**
     * And internal class specific for handling network requests when the search button is pressed
     */
    class RequestManager extends AsyncTask<String, Void, JSONArray> {

        /**
         * Idea based on http://stackoverflow.com/questions/6343166/how-to-fix-android-os-networkonmainthreadexception
         * Takes in the stop and route and creates a POST request based on those
         * Checks to see if a route is past in to determine endpoint
         * @param params    An array of string parameters. [0] and [1] are assumed to be stop and
         *                  respectively
         * @return          A JSONArray containing the body of the JSON response
         */
        @Override
        protected JSONArray doInBackground(String... params) {
            Webb webb = Webb.create();
            webb.setBaseUri("https://findmybusnj.com/rest");

            // Get the parameters
            String stop = params[0].toString();
            String route = params[1].toString();

            // Make actual URL based on inputs
            if (route.isEmpty()) {
                return webb.post("/stop")
                        .param("stop", stop)
                        .ensureSuccess()
                        .asJsonArray()
                        .getBody();
            } else {
                return webb.post("/stop/byRoute")
                        .param("stop", stop)
                        .param("route", route)
                        .ensureSuccess()
                        .asJsonArray()
                        .getBody();
            }
        }

        // Handles response after the request is made and update list
        protected void onPostExecute(JSONArray response) {
            // Dismiss current activity
            updateList(response);
        }
    }


    /**
     * Makes a post request to the server and notifies the ListView it needs to update with new data
     */
    private void makePostRequest() {
        // Gets stop textfield along with contents
        TextView stop_input = (TextView) findViewById(R.id.stop_number_input);
        TextView route_input = (TextView) findViewById(R.id.route_input);
        String stop = stop_input.getText().toString();
        String route = route_input.getText().toString();

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
        } else {
            // Make network request on background thread
            new RequestManager().execute(stop, route);
        }
    }

    /**
     * Sends the JSONArray back to the parent of this activity via Intent extras
     * @param response  JSONArray retrieved from the server
     */
    private void updateList(JSONArray response) {
        TextView stop_input = (TextView) findViewById(R.id.stop_number_input);
        TextView route_input = (TextView) findViewById(R.id.route_input);

        Intent intent = getIntent().putExtra("ResponseArray", response.toString());
        intent.putExtra("Stop", stop_input.getText().toString());
        intent.putExtra("Route", route_input.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
        Log.d("Response: ", response.toString());
    }
}
