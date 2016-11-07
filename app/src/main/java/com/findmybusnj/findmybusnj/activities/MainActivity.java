package com.findmybusnj.findmybusnj.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.findmybusnj.findmybusnj.adaptors.MainViewAdaptor;
import com.findmybusnj.findmybusnj.R;
import com.findmybusnj.findmybusnj.fragments.SearchTab;
import com.findmybusnj.findmybusnj.handlers.DatabaseHandler;
import com.findmybusnj.findmybusnj.models.Favorite;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    // Declaring Your View and Variables

    ViewPager pager;
    MainViewAdaptor adapter;
    TabLayout tabs;
    CharSequence Titles[] = {"Search","Near By"};
    int Numboftabs = 2;

    String currentStop = "";
    String currentRoute = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new MainViewAdaptor(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (TabLayout) findViewById(R.id.tabBar);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_button:
                // Set the expected result number
                final int result = 1;

                // Create an intent, attach extras to be passed back and forth, start the activity
                Intent searchFavoriteActivityIntent = new Intent(this, SearchFavoritesActivity.class);
                searchFavoriteActivityIntent.putExtra("ResponseArray", new JSONArray().toString());
                searchFavoriteActivityIntent.putExtra("Stop", "");
                searchFavoriteActivityIntent.putExtra("Route", "");
                startActivityForResult(searchFavoriteActivityIntent, result);

                return true;
            case R.id.save_button:
                if (currentStop.isEmpty()) {
                    new AlertDialog.Builder(this)
                            .setTitle("Stop Required")
                            .setMessage("Please enter a stop number before saving")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // no-op
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                DatabaseHandler handler = new DatabaseHandler(this);
                Favorite favorite = new Favorite(currentStop, currentRoute, 0);
                handler.addFavorite(favorite);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // retrieves the data passed from the server response, passes it to the fragment to handle updating
        String responseData = data.getStringExtra("ResponseArray");
        currentRoute = data.getStringExtra("Route");
        currentStop = data.getStringExtra("Stop");
        SearchTab tab = (SearchTab) adapter.getRegisteredFragment(0);
        tab.updateListView(responseData);
    }
}
