package com.example.danny.barkandroid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {


    Button login_bn;
    EditText Email, Password;
    String email,password;
    TextView link_to_singup ,_temp_link_to_map;
    AlertDialog.Builder builder;
    String reg_url ="https://barkandroid.herokuapp.com/login";
   //String reg_url ="http://192.168.1.29:8000/login";



    int SIGN_IN_REQUEST_CODE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);



        FileInputStream fin;
        int c;
        String tempkey = "";

        String FILENAME = "UserKey";
        try {
            fin = openFileInput(FILENAME);



            while( (c = fin.read()) != -1){
                tempkey = tempkey + Character.toString((char)c);
            }
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(tempkey.equals("MyKey")){
            try {
                if(openFileInput("MyJsonObj")!=null) // move to the menu only if he have json data user
                     startActivity(new Intent(LogIn.this, menu_grid.class));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        File dir = getFilesDir();
        File file = new File(dir, "UserKey");
        file.delete();
        File file2 = new File(dir, "MyJsonObj");
        file2.delete();





        login_bn = (Button)findViewById(R.id.btn_login);
        Email=(EditText)findViewById(R.id.input_email);
        Password = (EditText)findViewById(R.id.input_password);
        link_to_singup=(TextView)findViewById(R.id.link_signup);
       // _temp_link_to_map=(TextView)findViewById(R.id.temp_link_to_map);


        builder = new AlertDialog.Builder(this);



        link_to_singup.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                startActivity(new Intent(LogIn.this,register.class)); //register.class
            }

        });




        login_bn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                email = Email.getText().toString();
                password = Password.getText().toString();

                if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED)
                    if (email.equals("") || password.equals("")) {
                        builder.setTitle("Something went wrong....");
                        builder.setMessage("please fill all the fields...");
                        displayAlert("input_error");
                    } else {
                        Map<String, String> params = new HashMap();
                        params.put("email", email);
                        params.put("sis", password);

                        JSONObject parameters = new JSONObject(params);

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.POST, reg_url, parameters, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                       // System.out.println("\n\n\n\n\n   Response: " + response.toString()+"\n\n\n\n\n");

                                        ////Save the password on Internal Storage


                                        String FILENAME = "UserKey";
                                        String string = "MyKey";
                                        FileOutputStream fos;
                                        try {
                                            fos = openFileOutput(FILENAME, MODE_PRIVATE);
                                            fos.write(string.getBytes());
                                            fos.close();

                                            fos = openFileOutput("MyJsonObj", MODE_PRIVATE);
                                            fos.write(response.toString().getBytes());
                                            fos.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        ////END


                                        AlertDialog alertDialog = new AlertDialog.Builder(LogIn.this).create();
                                        alertDialog.setTitle("");
                                        alertDialog.setMessage("login successful");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                        startActivity(new Intent(LogIn.this, menu_grid.class));
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
                        MySingleton.getInstance(LogIn.this).addToRequestque(jsObjRequest);



                    }
            }
        });


    }




    public void displayAlert(final String code)
    {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(code.equals("input_error"))
                {
                    Password.setText("");

                }
                else if(code.equals("reg_success"))
                {
                    finish();
                }
                else if(code.equals("reg_failed"))
                {

                    Email.setText("");

                    Password.setText("");

                }

            }
        });

        AlertDialog alertDialog =builder.create();
        alertDialog.show();


    }




}
