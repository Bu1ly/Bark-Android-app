package com.example.danny.barkandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

public class menu_grid extends AppCompatActivity {


    ImageView mapIcon , profileIcon ,chatButton , Searchusers, LogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_grid);


        mapIcon=(ImageView)findViewById(R.id.imageMap);
        profileIcon = (ImageView)findViewById(R.id.ImageProfile);
        chatButton= (ImageView)findViewById(R.id.Chat_icon);
        Searchusers = (ImageView)findViewById(R.id.userList);
        LogOut = (ImageView)findViewById(R.id.logouticon);





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

        Searchusers.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                startActivity(new Intent(menu_grid.this,SearchUsers.class)); //register.class
            }

        });

        LogOut.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {

                File dir = getFilesDir();
                File file = new File(dir, "UserKey");
                file.delete();
                File file2 = new File(dir, "MyJsonObj");
                file2.delete();
                startActivity(new Intent(menu_grid.this,LogIn.class)); //register.class

            }

        });
    }
}
