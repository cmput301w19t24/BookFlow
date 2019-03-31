package com.example.bookflow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Show google map with your current location (if allowed)
 * If in selecting mode, long click to add marker, long click marker to drag, press done to finish
 * If in viewing mode, show marker
 */
public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMyLocationButtonClickListener{

    private FusedLocationProviderClient fusedLocationClient;
    private int markerCount = 0;
    private Marker meetingPlace;
    private GoogleMap mMap;
    private LatLng myLoc;


    private int mode;   // 0 for select location and 1 for view

    private String bookId, requestId;
    private double lat, lon;

    /**
     * onCreate method
     * get parameters from calling activity (latitude,longitude) or (bookId, requestId)
     * and decide whether it's viewing mode or selecting mode.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        ArrayList<String> infos = getIntent().getStringArrayListExtra("parameter");

        /* get parameters from calling activity, bookId and requestId are needed in selecting mode
         * (mode = 0), latitude and longitude are needed in viewing mode (mode = 1) */
        if (null != infos) {
            bookId = infos.get(1);
            requestId = infos.get(3);
            mode = 0;
        } else {
            Button doneButton = findViewById(R.id.googlemap_done);
            doneButton.setVisibility(View.GONE);
            infos = getIntent().getStringArrayListExtra("lat_lon");
            lat = Float.parseFloat(infos.get(0));
            lon = Float.parseFloat(infos.get(1));
            mode = 1;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) throws SecurityException {
        mMap = googleMap;

        // enable long click on google map
        mMap.setOnMapLongClickListener(this);
        /* Check if the app has location permission
        * if yes then find the user location, move and zoom the camera to there
        * if no then ask for permission*/
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng myLatLon = new LatLng(location.getLatitude(), location.getLongitude());
                    myLoc = myLatLon;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLon,14.0f));
                }
            });
        }
        else {
            String permissions[] = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(MapsActivity.this, permissions, 1);
        }

        // if it's viewing mode, then just simply add a non-draggable marker
        if (mode == 1) {
            LatLng point = new LatLng(lat, lon);
            meetingPlace = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title("Meeting Place"));
        }

    }

    /**
     * Ask for the location using permission from the user
     * If the user allows location permission,
     * find user location, move and zoom camera to there.
     * @param requestCode 1
     * @param permissions the permission type
     * @param GrantResults the permission result
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] GrantResults) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    LatLng myLatLon = new LatLng(location.getLatitude(), location.getLongitude());
                    myLoc = myLatLon;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLon,14.0f));
                }
            });
        }
    }

    /**
     * When the user long click a position on the map,
     * if it's selecting mode and there are no markers existing
     * add a marker at the clicked position
     * @param point: the location being long clicked
     */
    @Override
    public void onMapLongClick(LatLng point) {
        // check if there is already a marker
        // check if it is location selecting mode
        // if conditions satisfied then add marker
        if (markerCount != 1) {
            if (mode == 0) {
                meetingPlace = mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("Meeting Place")
                        .draggable(true));
                meetingPlace.setTag("Drag To Move");
                markerCount = 1;
            }
        }
    }

    /**
     * When my location button on the top right corner is clicked
     * the camera go to my location
     * @return true
     */
    @Override
    public boolean onMyLocationButtonClick() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc,16.0f));
        return true;
    }

    /**
     * On click method of button "done"
     * Up load the latitude and longitude to firebase
     * If no location is selected, do nothing
     */
    public void upload(View view) {
        // if no location selected, do nothing
        if (null == meetingPlace) {
            return;
        }
        // else, get the lat and lon of marker
        lat = meetingPlace.getPosition().latitude;
        lon = meetingPlace.getPosition().longitude;

        // upload latitude and longitude to firebase
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("RequestsReceivedByBook").child(bookId)
                .child(requestId)
                .child("latitude")
                .setValue(lat);
        dbRef.child("RequestsReceivedByBook").child(bookId)
                .child(requestId)
                .child("longitude")
                .setValue(lon);

        // go to book detail activity
        Intent intent = new Intent(MapsActivity.this, BookDetailActivity.class);
        intent.putExtra("book_id", bookId);
        startActivity(intent);
    }
}
