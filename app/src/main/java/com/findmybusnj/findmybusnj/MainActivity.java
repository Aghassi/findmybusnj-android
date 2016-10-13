package com.findmybusnj.findmybusnj;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Declaring Your View and Variables

    ViewPager pager;
    MainViewAdaptor adapter;
    TabLayout tabs;
    CharSequence Titles[] = {"Search","Near By"};
    int Numboftabs = 2;

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
                Intent searchFavoriteActivityIntent = new Intent(this, SearchFavoritesActivity.class);
                searchFavoriteActivityIntent.putExtra("ResponseArray", new JSONArray().toString());
                final int result = 1;
                startActivityForResult(searchFavoriteActivityIntent, result);
                return true;
            case R.id.save_button:
                System.out.print("saved tapped");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String responseData = data.getStringExtra("ResponseArray");
        try {
            JSONArray response = new JSONArray(responseData);
            ListView listView = (ListView) pager.findViewById(R.id.stop_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
