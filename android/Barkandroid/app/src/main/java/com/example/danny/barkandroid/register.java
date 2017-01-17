package com.example.danny.barkandroid;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    Button reg_bn;
    EditText Name, Email, UserName,Password,ConfimPassword;
    String name,email,username,password,confimpassword;
    AlertDialog.Builder builder;
    String reg_url ="http://192.168.43.192:8080/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        reg_bn = (Button)findViewById(R.id.btn_signup);
        Name = (EditText)findViewById(R.id.input_name);
        Email=(EditText)findViewById(R.id.input_email);
        UserName = (EditText)findViewById(R.id.input_username);
        Password = (EditText)findViewById(R.id.input_password);
        ConfimPassword = (EditText)findViewById(R.id.confim_password);
        builder = new AlertDialog.Builder(register.this);

//        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
//        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);



        reg_bn.setOnClickListener(new View.OnClickListener()
        {
           public void onClick(View v)
           {
               name = Name.getText().toString();
               email = Email.getText().toString();
               username = UserName.getText().toString();
               password = Password.getText().toString();
               confimpassword = ConfimPassword.getText().toString();

               if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED) {



               if(name.equals("") ||email.equals("")||username.equals("")||password.equals("")||confimpassword.equals(""))
               {
                   builder.setTitle("Something went wrong....");
                   builder.setMessage("please fill all the fields...");
                   displayAlert("input_error");

               }

               else if(!(password.equals(confimpassword)))
                   {
                       builder.setTitle("Something went wrong....");
                       builder.setMessage("Your passwords are not matching...");
                       displayAlert("input_error");
                   }

               else
               {


//                   //////////////////
//                  // JsonObjectRequest jsonObjectRequest =new JsonObjectRequest(Request.Method.POST, reg_url,(String)new Response.Listener<JSONObject>()
//                   StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url,
//                           new Response.Listener<String>() {
//                               @Override
//                               public void onResponse(String response) {
//                                        System.out.println("good!!");
//                                   try {
//                                       JSONArray jsonArray  = new JSONArray(response);
//                                       JSONObject jsonObject = jsonArray.getJSONObject(0);
//                                       String code = jsonObject.getString("code");
//                                       String message = jsonObject.getString("message");
//                                       builder.setTitle("Server Response...");
//                                       builder.setMessage(message);
//                                       displayAlert(code);
//                                   } catch (JSONException e) {
//                                       e.printStackTrace();
//                                   }
//
//
//
//                               }
//
//                           }, new Response.ErrorListener() {
//
//                       @Override
//                       public void onErrorResponse(VolleyError error) {
//
//                       }
//                   }
//
//                   ){
//                       @Override
//                       protected Map<String, String> getParams() throws AuthFailureError {
//                           Map<String,String> params =new HashMap<String, String>();
//                           params.put("name", name);
//                           params.put("email",email);
//                           params.put("username",username);
//                           params.put("password",password);
//                           return params;
//                       }
//                   };
//                   MySingleton.getInstance(register.this).addToRequestque(stringRequest);
//                   System.out.println("stringRequest: " + stringRequest.toString());
//                   System.out.println("Send Register Data to DB");
//
//                   ////////////////

                   Map<String, String> params = new HashMap();
                   params.put("dogName", name);
                   params.put("gender",email);
                   params.put("age",username);
                   params.put("ownerName",password);
                   params.put("sis",password);

                   JSONObject parameters = new JSONObject(params);
                    System.out.println("JSONObject: ----------   " + parameters.toString());

                   JsonObjectRequest jsObjRequest = new JsonObjectRequest
                           (Request.Method.POST, reg_url, parameters, new Response.Listener<JSONObject>() {

                               @Override
                               public void onResponse(JSONObject response) {
                                   System.out.println("Response: " + response.toString());

                                   AlertDialog alertDialog = new AlertDialog.Builder(register.this).create();
                                   alertDialog.setTitle("Alert");
                                   alertDialog.setMessage("Account Created ");
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
                   System.out.println("jsObjRequest: "+ jsObjRequest.toString());

// Access the RequestQueue through your singleton class.
                   MySingleton.getInstance(register.this).addToRequestque(jsObjRequest);
               //    MySingleton.getInstance(this.Re).addToRequestQueue(jsObjRequest);

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
                    ConfimPassword.setText("");
                }
                else if(code.equals("reg_success"))
                {
                    finish();
                }
                else if(code.equals("reg_failed"))
                {
                    Name.setText("");
                    Email.setText("");
                    UserName.setText("");
                    Password.setText("");
                    ConfimPassword.setText("");

                }

            }
        });

        AlertDialog alertDialog =builder.create();
        alertDialog.show();


    }
}
