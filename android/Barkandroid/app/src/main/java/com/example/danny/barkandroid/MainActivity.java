package com.example.danny.barkandroid;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        textView=(TextView)findViewById(R.id.link_signup);

        ContentResolver contentResolver = getContentResolver();

        final int REQUEST_CODE_ASK_PERMISSIONS = 123;
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_NETWORK_STATE"}, REQUEST_CODE_ASK_PERMISSIONS);
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.ACCESS_NETWORK_STATE") == PackageManager.PERMISSION_GRANTED) {
      //      cursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        }

        textView.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this,register.class));
            }

        });
    }
}
