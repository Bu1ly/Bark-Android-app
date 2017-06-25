package com.example.danny.barkandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchUsers extends AppCompatActivity {


    private String reg_url ="http://192.168.1.29:8000/getAllUsers";
    private ArrayList<String> items;
    private ArrayList<JSONObject> listItems;
    private ListView listV;
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);


         items = new ArrayList<String>();


        Map<String, String> params = new HashMap();
        //params.put("email", email);
        //params.put("sis", password);

       // JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, reg_url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // System.out.println("\n\n\n\n\n   Response: " + response.toString()+"\n\n\n\n\n");

                        ////Save the password on Internal Storage

                        for(int i=0; i<5 ; i++) {

                            String name=" dasf +\n +dfasfs ";
                            items.add(name);
                            // Log.d(name,"Output");
                        }



                        AlertDialog alertDialog = new AlertDialog.Builder(SearchUsers.this).create();
                        alertDialog.setTitle("");
                        alertDialog.setMessage("login successful");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                        alertDialog.show();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        System.out.println("FIX ME!!! error: " + error.toString());


                    }
                });
        MySingleton.getInstance(SearchUsers.this).addToRequestque(jsObjRequest);










        ListView listOfMessages = (ListView)findViewById(R.id.myListView);





        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1, items);
        listOfMessages.setAdapter(mArrayAdapter);
    }


}




*/



        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_users);



            JsonArrayRequest jsObjRequest = new JsonArrayRequest
                    (Request.Method.GET, reg_url, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            // System.out.println("\n\n\n\n\n   Response: " + response.toString()+"\n\n\n\n\n");



                            listItems = new ArrayList<JSONObject>();
                            ////Save the password on Internal Storage
                             listItems =getArrayListFromJSONArray(response);

                            listV=(ListView)findViewById(R.id.listv);
                            View header = getLayoutInflater().inflate(R.layout.header_listv, null);
                            listV.addHeaderView(header);
                            ListAdapter adapter=new ListAdapter(SearchUsers.this, R.layout.list_layout ,R.id.txtid,listItems);
                            listV.setAdapter(adapter);


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                            System.out.println("FIX ME!!! error: " + error.toString());


                        }
                    });
            MySingleton.getInstance(SearchUsers.this).addToRequestque(jsObjRequest);







        }



        private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray){

            ArrayList<JSONObject> aList=new ArrayList<JSONObject>();

            try {

                if (jsonArray != null) {

                    for (int i = 0; i < jsonArray.length(); i++) {

                        aList.add(jsonArray.getJSONObject(i));

                    }

                }

            }catch (JSONException je){je.printStackTrace();}

            return  aList;

        }

    }