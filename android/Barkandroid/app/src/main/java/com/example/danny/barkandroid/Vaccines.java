package com.example.danny.barkandroid;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by assaf on 29/04/2017.
 */

public class Vaccines extends AppCompatActivity {

     ImageView addIcon;
    private String Vaccines_url ="https://barkandroid.herokuapp.com/vaccines/";
    private String Add_Vaccines_url ="https://barkandroid.herokuapp.com/addVaccine";

    private ArrayList<String> items;
    private ArrayList<JSONObject> listItems;
    private ListView listV;
    private JSONObject obj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vaccines);

        addIcon = (ImageView)findViewById(R.id.addButton);

        addIcon.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                addRecordToList();
            }

        });



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



        Map<String, String> params = new HashMap();


        try {
            params.put("userId", obj.getString("_id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject parameters = new JSONObject(params);


        JsonArrayRequest jsObjRequest = null;
        try {
            jsObjRequest = new JsonArrayRequest
                    (Request.Method.GET, Vaccines_url+obj.getString("_id"), parameters, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            listItems = new ArrayList<JSONObject>();

                            listItems =getArrayListFromJSONArray(response);



                            listV=(ListView)findViewById(R.id.listv);
                            View header = getLayoutInflater().inflate(R.layout.header_list_vaccines, null);
                            listV.addHeaderView(header);
                            ListAdapterVaccines adapter=new ListAdapterVaccines(Vaccines.this, R.layout.list_vaccines ,R.id.txtid,listItems);
                            listV.setAdapter( adapter);


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub

                            System.out.println("FIX ME!!! error: " + error.toString());


                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MySingleton.getInstance(Vaccines.this).addToRequestque(jsObjRequest);







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
                Vaccines.this);
        // Get the layout inflater
        LayoutInflater inflater = Vaccines.this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.insert_alert, null);
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

                                Toast.makeText(Vaccines.this, "Record created" ,
                                        Toast.LENGTH_SHORT).show();

                                ///////////////////////////////



                                Map<String, String> params = new HashMap();


                                try {
                                    params.put("userId", obj.getString("_id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                params.put("name",mSearchText[0]);
                                params.put("date",mSearchText[1]);
                                JSONObject parameters = new JSONObject(params);



                                JsonArrayRequest jsObjRequest = new JsonArrayRequest
                                        (Request.Method.POST, Add_Vaccines_url, parameters, new Response.Listener<JSONArray>() {

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
                                MySingleton.getInstance(Vaccines.this).addToRequestque(jsObjRequest);








                                //////////////////////////////





                                finish();
                                startActivity(new Intent(Vaccines.this,Vaccines.class)); //register.class
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



}





