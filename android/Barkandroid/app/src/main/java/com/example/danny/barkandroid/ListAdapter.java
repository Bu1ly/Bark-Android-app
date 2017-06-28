package com.example.danny.barkandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.danny.barkandroid.R.*;


/**
 * Created by assaf
 */

public class ListAdapter extends ArrayAdapter<JSONObject> {


        int vg;

        ArrayList<JSONObject> list;

        Context context;

        public ListAdapter(Context context, int vg, int id, ArrayList<JSONObject> list){

            super(context,vg, id,list);

            this.context=context;

            this.vg=vg;

            this.list=list;

        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = inflater.inflate(vg, parent, false);



            TextView txtName=(TextView)itemView.findViewById(id.txtname);

            TextView txtSex=(TextView)itemView.findViewById(id.txtsex);
            ImageView temp = (ImageView)itemView.findViewById(id.imageView3);

            try {
                if(position==0){

                    Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),mipmap.doglost1);
                    temp.setImageBitmap(largeIcon);
                }
                else if(position==1){

                    Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),mipmap.doglost2);
                    temp.setImageBitmap(largeIcon);
                }
                else if(position==2){

                    Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),mipmap.dog_lost3_german);
                    temp.setImageBitmap(largeIcon);
                }
                else{
                    Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),mipmap.dog_icon_lost);
                    temp.setImageBitmap(largeIcon);

                }


                txtName.setText(list.get(position).getString("dogName"));

                txtSex.setText(list.get(position).getString("phone"));



            } catch (JSONException e) {

                e.printStackTrace();

            }



            return itemView;

        }

    }
