package com.example.danny.barkandroid;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private JSONObject obj;
    ////marker style
    private BitmapDescriptor iconStyle;
    private String getMapData_url = "http://192.168.1.23:8000/getUsersWithinDistance";

    private String UpdaeUser_url = "http://192.168.1.23:8000/change_info";

    final long MIN_TIME_FOR_UPDATE = 1000;
    final float MIN_DIS_FOR_UPFATE = 0.01f;
    private GoogleApiClient mGoogleApiClient;
    private Location myLocation;
    private Marker myMarkerLocation;
    private boolean mapFlag = false;
    private LocationManager locationManager;
    private Calendar c ;
    private int seconds ;
    private int secondCounter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        iconStyle = BitmapDescriptorFactory.fromResource(R.mipmap.map_icon_dog);
        mapFlag =true;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        FileInputStream fin;
        int c;
        String tempkey = "";

        try {
            fin = openFileInput("MyJsonObj");


            while ((c = fin.read()) != -1) {
                tempkey = tempkey + Character.toString((char) c);
            }
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            obj = new JSONObject(tempkey);
        } catch (JSONException e) {
            e.printStackTrace();
        }




        ///////////////////// permission REQUEST//////////////////////////////
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_FOR_UPDATE, MIN_DIS_FOR_UPFATE, (android.location.LocationListener) locationListener);

        }
        else{
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, REQUEST_CODE_ASK_PERMISSIONS);
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, REQUEST_CODE_ASK_PERMISSIONS);

        }


    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {


        }


    };


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
    public void onMapReady(GoogleMap googleMap) {


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                //  Log.e(, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Error: ", "Can't find style. Error: ", e);
        }


        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.

            if (mMap != null) {



                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        if(mapFlag ==true) {
                            double zoomLevel = 16.0; //This goes up to 21
                            LatLng MyLocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, (float) zoomLevel));
                            mapFlag = false;

                            //Get first time on the map
                            c = Calendar.getInstance();
                            secondCounter = c.get(Calendar.SECOND);
                            Toast.makeText(MapsActivity.this, "Search Dogs..",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else {
                            c = Calendar.getInstance();
                            seconds = c.get(Calendar.SECOND);


                            //Refresh map every 8 second
                            if (((secondCounter + 8) % 60) < seconds) {
                                secondCounter = c.get(Calendar.SECOND);
                                //update my location
                              UpdateMap(arg0.getLatitude()+0.002897, arg0.getLongitude()-0.000297);
                           //c     UpdateMap(arg0.getLatitude(), arg0.getLongitude());
                            }
                        }

                    }




                });






            }



    }


//    Get Users Within Distance
//    * Method : POST
//    * payload : {
//        *       coordX     : "Lat",
//        *       coordY      : "Long",
//        *       range      : "in KM"
//                * }
//
    public void UpdateMap(final Double Lat, final Double Long){

          String temp="";
        String name="";
        try {
            temp = obj.getString("_id");
            name = obj.getString("ownerName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> paramsUser = new HashMap();
        paramsUser.put("userId", temp);
        paramsUser.put("coordY", Double.toString(Long));
        paramsUser.put("coordX", Double.toString(Lat));
        paramsUser.put("ownerName", name);


        JSONObject UserParameters = new JSONObject(paramsUser);

        JsonObjectRequest jsObjRequestV2 = new JsonObjectRequest
                (Request.Method.POST, UpdaeUser_url, UserParameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("\n\n\n\n\n update user details  Response: " + response.toString()+"\n\n\n\n\n");


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        System.out.println("FIX ME!!! error: " + error.toString());


                    }
                });
        MySingleton.getInstance(MapsActivity.this).addToRequestque(jsObjRequestV2);



        ///////////////////////////////////////////////////////////////
        Map<String, String> params = new HashMap();
        params.put("coordX", Double.toString(Lat));
        params.put("coordY", Double.toString(Long));
        params.put("range", "50");
        params.put("_id", temp);
        JSONObject parameters = new JSONObject(params);


        final String finalTemp = temp;
        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(Request.Method.POST, getMapData_url, parameters, new Response.Listener<JSONArray>()
        //JsonObjectRequest jsObjRequest = new JsonObjectRequest
          //      (Request.Method.POST, getMapData_url, parameters, new Response.Listener<JSONObject>() {
        {
                    @Override
                    public void onResponse(JSONArray response) {
                         System.out.println("\n\n\n\n\n   Response: " + response.toString()+"\n\n\n\n\n");


                        mMap.clear();
                        for(int i=0 ; i<response.length(); i++)
                        {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Double _lat = Double.parseDouble(object.getString("coordX"));
                                Double _long = Double.parseDouble(object.getString("coordY"));


                                // Show only other users
                                if(!object.getString("_id").equals(finalTemp) ) {
                            /*
                                    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                    Date yourDate = parser.parse(object.getString("lastActiveTime"));
                                    Calendar calendar = Calendar.getInstance();
                                    int currectDateTime = calendar.get(Calendar.DATE);
                                    int currectMinTime = calendar.get(Calendar.MINUTE);
                                    int currectHourTime = calendar.get(Calendar.HOUR);
                                    calendar.setTime(yourDate);
                                    int dayFomDB = calendar.get(Calendar.DAY_OF_MONTH); //Day of the month :)
                                    int timeFromDb = calendar.get(Calendar.MINUTE); //number of seconds
                                    int TimeHourFromDb = calendar.get(Calendar.HOUR);
*/
                                  //  if (dayFomDB == currectDateTime && (currectMinTime - timeFromDb) < 2 && TimeHourFromDb == currectHourTime) {
                                        mMap.addMarker(new MarkerOptions()
                                                .position(new LatLng(_lat, _long)).title(object.getString("ownerName") + " " + DistanceFromPoint(_lat, _long, Lat, Long)).icon(iconStyle));

                                  //  }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        System.out.println("FIX ME!!! error: " + error.toString());
                        AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                        alertDialog.setTitle("");
                        alertDialog.setMessage("   ---Debug2---=    "+error.toString());
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                        alertDialog.show();

                    }
                });
        MySingleton.getInstance(MapsActivity.this).addToRequestque(jsArrayRequest);



    }






    ///Return distance from the point in meter
    private String DistanceFromPoint(double latA, double lngA, double latB, double lngB) {
        Location locationA = new Location("");
        Location locationB = new Location("");
        locationA.setLatitude(latA);
        locationA.setLongitude(lngA); //
        locationB.setLatitude(latB);
        locationB.setLongitude(lngB); //
        float distance = locationA.distanceTo(locationB);

        return ((int) distance + "m From you");
    }





}
