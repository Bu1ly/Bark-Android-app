package com.example.danny.barkandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by assaf on 17/01/2017.
 */

public class LogIn_no_use extends AppCompatActivity {

    Button login_bn;
    EditText  Email, Password;
    String email,password;
    TextView link_to_singup;
    AlertDialog.Builder builder;
    String reg_url ="http://192.168.43.192:8080/register";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_bn = (Button)findViewById(R.id.btn_login);
        Email=(EditText)findViewById(R.id.input_email);
        Password = (EditText)findViewById(R.id.input_password);
        link_to_singup=(TextView)findViewById(R.id.link_signup);

        builder = new AlertDialog.Builder(LogIn_no_use.this);


        link_to_singup.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                startActivity(new Intent(LogIn_no_use.this,register.class));
            }

        });

        login_bn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });


    }

}
