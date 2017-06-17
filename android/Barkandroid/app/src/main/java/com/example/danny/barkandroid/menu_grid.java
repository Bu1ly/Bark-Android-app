package com.example.danny.barkandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class menu_grid extends AppCompatActivity {


    ImageView mapIcon , profileIcon ,chatButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_grid);


        mapIcon=(ImageView)findViewById(R.id.imageMap);
        profileIcon = (ImageView)findViewById(R.id.ImageProfile);
        chatButton= (ImageView)findViewById(R.id.imageView6);






        mapIcon.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                startActivity(new Intent(menu_grid.this,MapsActivity.class)); //register.class
            }

        });


        profileIcon.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                startActivity(new Intent(menu_grid.this,Profile.class)); //register.class
            }

        });

        chatButton.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                startActivity(new Intent(menu_grid.this,Chat.class)); //register.class
            }

        });
    }
}
