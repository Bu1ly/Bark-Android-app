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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
    EditText Name, Email, UserName,Password,ConfimPassword , DogType;
    TextView link_login_btn;
    String name,email,username,password,confimpassword , Sex ,dogtype;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    AlertDialog.Builder builder;


    String reg_url ="https://barkandroid.herokuapp.com/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        reg_bn = (Button)findViewById(R.id.btn_signup);
        //Name = (EditText)findViewById(R.id.input_name);
        Email=(EditText)findViewById(R.id.input_email);
        UserName = (EditText)findViewById(R.id.input_username);
        Password = (EditText)findViewById(R.id.input_password);
        ConfimPassword = (EditText)findViewById(R.id.confim_password);
        DogType =(EditText)findViewById(R.id.input_type);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        link_login_btn=(TextView)findViewById(R.id.link_login);
        builder = new AlertDialog.Builder(register.this);


        link_login_btn.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                finish(); ///close the Activity & come back to login activity
            }

        });


        reg_bn.setOnClickListener(new View.OnClickListener()
        {
           public void onClick(View v)
           {
              // name = Name.getText().toString();
               email = Email.getText().toString();
               username = UserName.getText().toString();
               password = Password.getText().toString();
               confimpassword = ConfimPassword.getText().toString();
               dogtype = DogType.getText().toString();

               int selectedId = radioSexGroup.getCheckedRadioButtonId();
               radioSexButton = (RadioButton) findViewById(selectedId);
                Sex = radioSexButton.getText().toString();

               if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.INTERNET") == PackageManager.PERMISSION_GRANTED) {



               if(email.equals("")||username.equals("")||password.equals("")||confimpassword.equals("")||dogtype.equals(""))
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



                   Map<String, String> params = new HashMap();

                   params.put("ownerName",username); //userName - ownerName (db)
                   params.put("email",email); // email - email (db)
                   params.put("sis",password); // password - sis (db)
                   params.put("dogName",dogtype); // dog type - connect on db with "dogname"
                   params.put("gender",Sex); // gender dog - gender (db)v


                                       /* User Register API
                    * Method : POST
                    * payload : {
                    *       dogName     : "",
                    *       gender      : "",
                    *       age         : "",
                    *       ownerName   : "",
                    *       email       : "",
                    *       sis         : "",
                    *       coordX      : "",
                    *       coordY      : ""
                    * }
                    * Passport Authentication : false
                    * */

                   JSONObject parameters = new JSONObject(params);
                    System.out.println("JSONObject: ----------   " + parameters.toString());

                   JsonObjectRequest jsObjRequest = new JsonObjectRequest
                           (Request.Method.POST, reg_url, parameters, new Response.Listener<JSONObject>() {

                               @Override
                               public void onResponse(JSONObject response) {
                                   System.out.println("Response: " + response.toString());

                                   AlertDialog alertDialog = new AlertDialog.Builder(register.this).create();
                                   alertDialog.setTitle("");
                                   alertDialog.setMessage("Account Created ");
                                   alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                           new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog, int which) {
                                                   dialog.dismiss();
                                                   finish();
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
                   // Name.setText("");
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
