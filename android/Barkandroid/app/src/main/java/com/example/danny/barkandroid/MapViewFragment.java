package com.example.danny.barkandroid;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by assaf on 29/04/2017.
 */

public class MapViewFragment extends Fragment {

    MapView mMapView;
    //private GoogleMap googleMap;
    private GoogleMap mMap;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Map");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
          /*
                googleMap = mMap;

                // For showing a move to my location button
               // googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(-34, 151);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            */

                /////////////

                mMap = googleMap;

                // Add a marker in Sydney and move the camera
                LatLng sacher_park  = new LatLng(31.780600, 35.207700);
                mMap.addMarker(new MarkerOptions().position(sacher_park).title("Marker in Sacher Park"));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(31.781600, 35.208700))
                        .title("[Usr-Gershon] "+ DistanceFromPoint(31.781600,35.208700,31.780600,35.207700)));
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(31.780400, 35.209325))
                        .title("[Usr-Danny] "+ DistanceFromPoint(31.780400,35.209325,31.780600,35.207700)));
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(31.784600, 35.207107))
                        .title("[Usr-Moshe] "+ DistanceFromPoint(31.784600,35.207107,31.780600,35.207700)));



                double zoomLevel = 16.0; //This goes up to 21
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sacher_park, (float) zoomLevel));

                ////////////
            }
        });

        return rootView;
    }

    ///Return distance from the point in meter
    private String DistanceFromPoint(double latA, double lngA,double latB, double lngB)
    {
        Location locationA = new Location("");
        Location locationB = new Location("");
        locationA.setLatitude(latA); locationA.setLongitude(lngA); //
        locationB.setLatitude(latB); locationB.setLongitude(lngB); //
        float distance = locationA.distanceTo(locationB);

        return ((int)distance+"m From you");
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}