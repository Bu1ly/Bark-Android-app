package com.example.danny.barkandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchUsers extends AppCompatActivity {


    private String reg_url ="https://barkandroid.herokuapp.com/getLostDogsList";
    private String Add_lostDog_url ="https://barkandroid.herokuapp.com/addLostDog";
    private String Delete_lostDog_url ="https://barkandroid.herokuapp.com/removeLostDog";


//    private String reg_url ="http://192.168.1.29:8000/getLostDogsList";
//    private String Add_lostDog_url ="http://192.168.1.29:8000/addLostDog";
//    private String Delete_lostDog_url ="http://192.168.1.29:8000/removeLostDog";



    private ArrayList<String> items;
    private ArrayList<JSONObject> listItems;
    private ListView listV;
    private ImageView deleteIcon , addIcon;
    private JSONObject obj;



        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_users);
            addIcon = (ImageView)findViewById(R.id.addButton);
            deleteIcon = (ImageView)findViewById(R.id.deleteButton);



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

            addIcon.setOnClickListener(new View.OnClickListener()
            {

                public void onClick(View v)
                {
                    addRecordToList();
                }

            });

            deleteIcon.setOnClickListener(new View.OnClickListener()
            {

                public void onClick(View v)
                {
                    DeleteRecordToList();
                }

            });




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




    public void addRecordToList() {


        final String[] mSearchText = new String[2];

        AlertDialog.Builder builder = new AlertDialog.Builder(
                SearchUsers.this);
        // Get the layout inflater
        LayoutInflater inflater = SearchUsers.this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.insert_alert_lost_dog, null);
        final EditText Name = (EditText)mView.findViewById(R.id.text__View1);
        final EditText date = (EditText)mView.findViewById(R.id.text__View2);
        builder.setView(mView)

                // Add action buttons
                .setPositiveButton("ADD",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {

                                mSearchText[0] = Name.getText().toString();
                                mSearchText[1] = date.getText().toString();

                                Toast.makeText(SearchUsers.this, "Record created" ,
                                        Toast.LENGTH_SHORT).show();

                                ///////////////////////////////



                                Map<String, String> params = new HashMap();


                                try {
                                    params.put("_id", obj.getString("_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                params.put("dogName",mSearchText[0]);
                                params.put("phone",mSearchText[1]);
                                JSONObject parameters = new JSONObject(params);



                                JsonArrayRequest jsObjRequest = new JsonArrayRequest
                                        (Request.Method.POST, Add_lostDog_url, parameters, new Response.Listener<JSONArray>() {

                                            @Override
                                            public void onResponse(JSONArray response) {






                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // TODO Auto-generated method stub

                                                System.out.println("FIX ME!!! error: " + error.toString());


                                            }
                                        });
                                MySingleton.getInstance(SearchUsers.this).addToRequestque(jsObjRequest);








                                //////////////////////////////





                                finish();
                                startActivity(new Intent(SearchUsers.this,SearchUsers.class)); //register.class
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        // return builder.create();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();




    }


    public void DeleteRecordToList() {

                                Map<String, String> params = new HashMap();


                                try {
                                    params.put("id", obj.getString("_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                JSONObject parameters = new JSONObject(params);



                                JsonArrayRequest jsObjRequest = new JsonArrayRequest
                                        (Request.Method.POST, Delete_lostDog_url, parameters, new Response.Listener<JSONArray>() {

                                            @Override
                                            public void onResponse(JSONArray response) {






                                            }
                                        }, new Response.ErrorListener() {

                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // TODO Auto-generated method stub

                                                System.out.println("FIX ME!!! error: " + error.toString());


                                            }
                                        });
                                MySingleton.getInstance(SearchUsers.this).addToRequestque(jsObjRequest);

                                  Toast.makeText(SearchUsers.this, "Record deleted" ,
                                    Toast.LENGTH_SHORT).show();
                                    finish();

    }



    }