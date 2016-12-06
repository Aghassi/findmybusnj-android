package com.findmybusnj.findmybusnj.fragments;

import com.findmybusnj.findmybusnj.R;

import android.Manifest;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.findmybusnj.findmybusnj.models.ResultDataModel;
import com.findmybusnj.findmybusnj.R;
import com.findmybusnj.findmybusnj.handlers.DatabaseHandler;
import com.findmybusnj.findmybusnj.models.Favorite;
import com.goebl.david.Response;
import com.goebl.david.Webb;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


/**
 * Created by Vanya on 11/4/2016.
 *
 * Represents the view fragment for the "Near By" tab
 */

public class NearByTab extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = NearByTab.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private Marker mCurrLocation;

    //private Location mLastLocation;

    private static String FIND_BUS_URL = "https://findmybusnj.com/rest/getPlaces";
    //private TextView lblLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }


        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

      /*  mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);*/



        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.nearby_tab_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);


        mMapView.onResume();

        //lblLocation = (TextView) rootView.findViewById(R.id.lblLocation);

        Log.d(TAG, "onCreateView");
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        setUpMap();

        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        // Because I wanted to disconnect at some point
       /* if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        mMapView.onPause();*/

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();

        Log.d(TAG, "onLowMemory");
    }

    @Override
    public void onConnected(Bundle bundle) {

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }


        } else {
            mGoogleMap.clear();
            handleNewLocation(location);

        }
        Log.d(TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {
        //mLastLocation = location;
        if (mCurrLocation != null) {
            mCurrLocation.remove();
        }
        handleNewLocation(location);

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        Log.d(TAG, "onLocationChanged");
    }

    private void setUpMap() {
        //MapAsync used to be here

        // For old JSON test
        new Thread(new Runnable() {
            public void run() {
                try {
                    retrieveAndAddCities();
                } catch (IOException e) {
                    Log.d("new Thread in Setup","Cannot retrive cities");
                    return;
                }
            }
        }).start();

        mMapView.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission();
                    checkWrittenPermission();
                }
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMapToolbarEnabled(true);

                mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {

                        Log.d("MyMap", "MapClick");

                        //remove previously placed Marker
                        if (mCurrLocation != null) {
                            mCurrLocation.remove();
                        }

                        //place marker where user just clicked
                        mCurrLocation = mGoogleMap.addMarker(new MarkerOptions()
                                .position(point)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

                        Log.d("MyMap", "MapClick After Add Marker");

                        /*try {
                            LatLng test = mCurrLocation.getPosition();
                            Location location = new Location("test");
                            location.setLatitude(test.latitude);
                            location.setLongitude(test.longitude);
                            testNoContent(location);
                        } catch (Exception e) {
                            StringWriter errors = new StringWriter();
                            e.printStackTrace(new PrintWriter(errors));
                            errors.toString();
                            Log.d("test", errors.toString());
                        }*/

                        LatLng test = mCurrLocation.getPosition();

                        String latitude = String.valueOf(test.latitude);
                        String longitude = String.valueOf(test.longitude);
                        new RequestManager().execute(latitude, longitude);

                    }
                });

            }
        });




    }

    /* Where user is initially
     * Disappears on click.
     * RED = initial location
     * CYAN = new "desired" location
     */
    private void handleNewLocation(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("You Are Here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocation = mGoogleMap.addMarker(options);

        //Move map Camera
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mGoogleMap.animateCamera(yourLocation);

    }


    // Permissions
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                //TODO:
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                //(just doing it here for now, note that with this code, no explanation is shown)
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    public boolean checkWrittenPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                //TODO:
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                // Prompt the user once explanation has been shown
                //(just doing it here for now, note that with this code, no explanation is shown)

                // At the moment this crashes the app if the permissions aren't explicitly given in settings
                // However, it will prompt user for permissions.
                // User must restart after.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    // OLD JSON
    protected void retrieveAndAddCities() throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(FIND_BUS_URL);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            Log.d("retrieveAddCities", "Error connecting to service");
            throw new IOException("Error connecting to service");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Create markers for the city data.
        // Must run this on the UI thread since it's a UI operation.
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                try {
                    createMarkersFromJson(json.toString());
                } catch (JSONException e) {
                    Log.d("ruinOnUI", "Error processing JSON");
                }
            }
        });
    }
    void createMarkersFromJson(String json) throws JSONException {
        // De-serialize the JSON string into an array of city objects
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {

            // Create a marker for each city in the JSON data.
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            mGoogleMap.addMarker(new MarkerOptions()
                    .title(jsonObj.getString("name"))
                    .snippet(Integer.toString(jsonObj.getInt("population")))
                    .position(new LatLng(
                            jsonObj.getJSONArray("latlng").getDouble(0),
                            jsonObj.getJSONArray("latlng").getDouble(1)
                    ))
            );
        }
    }


    // New JSON
    class RequestManager extends AsyncTask<String, Void, JSONObject> {

        /**
         * Idea based on http://stackoverflow.com/questions/6343166/how-to-fix-android-os-networkonmainthreadexception
         * Takes in the stop and route and creates a POST request based on those
         * Checks to see if a route is past in to determine endpoint
         * @param params Current Location
         * @return          A JSONArray containing the body of the JSON response
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            Webb webb = Webb.create();

            // Get the parameters
            String latitude = params[0].toString();
            String longitude = params[1].toString();

            return webb.post("https://findmybusnj.com/rest/getPlaces")
                    .param("latitude",latitude)
                    .param("longitude",longitude)
                    .param("radius", "1000")
                    .param("types", "bus_station")
                    .ensureSuccess()
                    .asJsonObject()
                    .getBody();
        }



        // Handles response after the request is made and update map
        protected void onPostExecute(JSONObject response) {
            // Dismiss current activity
            try {
                updateStops(response);
            } catch (JSONException e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                errors.toString();
                Log.d("test", errors.toString());
            }
        }
    }

    /**
     * Parses JSONArray for nearby locations and populates map with google Markers
     * @param response  JSONArray retrieved from the server
     */
    private void updateStops(JSONObject response) throws JSONException{
        //JSONArray jsonArray = new JSONArray(json);
        JSONArray results = response.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {

            // Create a marker for each city in the JSON data.
            JSONObject jsonObj = results.getJSONObject(i);
            double latitude = jsonObj.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            double longitude = jsonObj.getJSONObject("geometry").getJSONObject("location").getDouble("lng");


            LatLng temp = new LatLng(latitude,longitude);

            mGoogleMap.addMarker(new MarkerOptions()
                    .title(jsonObj.getString("name"))
                    .position(temp)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            );

        }
        Log.d("Response: ", response.toString());
    }

}