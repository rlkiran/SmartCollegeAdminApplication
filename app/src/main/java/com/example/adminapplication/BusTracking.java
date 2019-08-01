package com.example.adminapplication;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import java.util.List;

public class BusTracking extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener {

    private MapView mapView;
    MapboxMap map;
    LocationEngine locationEngine;
    LocationLayerPlugin locationLayerPlugin;
    PermissionsManager permissionsManager;
    Location originLocation;
    Button bus1,bus2,bus3,bus4,stop_bt;
    GeoPoint userLoc;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.activity_bus_tracking);
        bus1 = findViewById(R.id.bus1);
        bus2 = findViewById(R.id.bus2);
        bus3 = findViewById(R.id.bus3);
        bus4 = findViewById(R.id.bus4);
        stop_bt = findViewById(R.id.stop_bt);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser userDname = mAuth.getCurrentUser();
        assert userDname != null;
        name = userDname.getDisplayName();

        bus1.setOnClickListener(v -> {
            bus2.setEnabled(false);
            bus3.setEnabled(false);
            bus4.setEnabled(false);
            stop_bt.setVisibility(View.VISIBLE);

            userLoc = new GeoPoint(originLocation.getLatitude(),originLocation.getLongitude());
            DocumentReference busref = db.collection("Buses").document("BusOne");

            busref
                    .update("geoLocation", userLoc,
                            "status","online",
                            "user",name)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Location sharing Successful", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Location sharing Failed", Toast.LENGTH_SHORT).show();
                        enableAll();
                    });

        });
        bus2.setOnClickListener(v -> {
            bus1.setEnabled(false);
            bus3.setEnabled(false);
            bus4.setEnabled(false);
            stop_bt.setVisibility(View.VISIBLE);
            userLoc = new GeoPoint(originLocation.getLatitude(),originLocation.getLongitude());
            DocumentReference busref2 = db.collection("Buses").document("BusTwo");

            busref2
                    .update("geoLocation", userLoc,
                            "status","online",
                            "user",name)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Location sharing Successful", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Location sharing Failed", Toast.LENGTH_SHORT).show();
                        enableAll();
                    });
        });
        bus3.setOnClickListener(v -> {
            bus2.setEnabled(false);
            bus1.setEnabled(false);
            bus4.setEnabled(false);
            stop_bt.setVisibility(View.VISIBLE);
            userLoc = new GeoPoint(originLocation.getLatitude(),originLocation.getLongitude());
            DocumentReference busref3 = db.collection("Buses").document("BusThree");

            busref3
                    .update("geoLocation", userLoc,
                            "status","online",
                            "user",name)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Location sharing Successful", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Location sharing Failed", Toast.LENGTH_SHORT).show();
                        enableAll();
                    });
        });
        bus4.setOnClickListener(v -> {
            bus2.setEnabled(false);
            bus3.setEnabled(false);
            bus1.setEnabled(false);
            stop_bt.setVisibility(View.VISIBLE);
            userLoc = new GeoPoint(originLocation.getLatitude(),originLocation.getLongitude());
            DocumentReference busref4 = db.collection("Buses").document("BusFour");

            busref4
                    .update("geoLocation", userLoc,
                            "status","online",
                            "user",name)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Location sharing Successful", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Location sharing Failed", Toast.LENGTH_SHORT).show();
                        enableAll();
                    });
        });
        stop_bt.setOnClickListener(v -> enableAll());
    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        enableLocation();

    }

    private void enableLocation() {
        if(PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine();
            initializeLocationLayer();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocationEngine() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }

    }

    @SuppressLint("MissingPermission")
    private void initializeLocationLayer() {
        locationLayerPlugin = new LocationLayerPlugin(mapView,map,locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);

    }

    private void setCameraPosition(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 13));

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        stopSharing();
    }

    private void stopSharing() {
        DocumentReference busRef = db.collection("Buses").document("BusOne");
        busRef.update("geoLocation", null, "status","offline","user",name);
        DocumentReference busRef2 = db.collection("Buses").document("BusTwo");
        busRef2.update("geoLocation", null, "status","offline","user",name);
        DocumentReference busRef3 = db.collection("Buses").document("BusThree");
        busRef3.update("geoLocation", null, "status","offline","user",name);
        DocumentReference busRef4 = db.collection("Buses").document("BusFour");
        busRef4.update("geoLocation", null, "status","offline","user",name);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void enableAll() {
        bus1.setEnabled(true);
        bus2.setEnabled(true);
        bus3.setEnabled(true);
        bus4.setEnabled(true);
        stop_bt.setVisibility(View.GONE);
        stopSharing();
    }
}
