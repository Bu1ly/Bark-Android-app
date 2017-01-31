package com.example.danny.barkandroid;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.danny.barkandroid.R.layout.login;

public class MainActivity extends AppCompatActivity {


    Button login_bn;
    EditText Email, Password;
    String email,password;
    TextView link_to_singup ,_temp_link_to_map;
    AlertDialog.Builder builder;
    String reg_url ="http://192.168.43.192:8080/register";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        login_bn = (Button)findViewById(R.id.btn_login);
        Email=(EditText)findViewById(R.id.input_email);
        Password = (EditText)findViewById(R.id.input_password);
        link_to_singup=(TextView)findViewById(R.id.link_signup);
        _temp_link_to_map=(TextView)findViewById(R.id.temp_link_to_map);


        builder = new AlertDialog.Builder(this);


//        ContentResolver contentResolver = getContentResolver();
//
//        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
//        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_NETWORK_STATE"}, REQUEST_CODE_ASK_PERMISSIONS);
//        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.ACCESS_NETWORK_STATE") == PackageManager.PERMISSION_GRANTED) {
//      //      cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
//        }

        link_to_singup.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,register.class)); //register.class
            }

        });

        ///////////////
        _temp_link_to_map.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
            }

        });

        ///////////////


        login_bn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                email = Email.getText().toString();
                password = Password.getText().toString();

                if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED) {
                    if(email.equals("")||password.equals(""))
                    {
                        builder.setTitle("Something went wrong....");
                        builder.setMessage("please fill all the fields...");
                        displayAlert("input_error");
                    }

                    else
                    {
                        Map<String, String> params = new HashMap();
                        params.put("email",email);
                        params.put("sis",password);

                        JSONObject parameters = new JSONObject(params);

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                (Request.Method.GET, reg_url, parameters, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("Response: " + response.toString());

                                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                                        alertDialog.setTitle("Alert");
                                        alertDialog.setMessage(" log in!! ");
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

                                        System.out.println("FIX ME!!! error: "+ error.toString());


                                    }
                                });
                        MySingleton.getInstance(MainActivity.this).addToRequestque(jsObjRequest);

                    }



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
