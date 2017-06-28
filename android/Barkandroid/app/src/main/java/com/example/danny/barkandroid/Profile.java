package com.example.danny.barkandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.danny.barkandroid.R.mipmap.profile_dog_icon;

public class Profile extends Activity {


    ///// after save need to save to the server
    ///// on load the page need to get from the server the user profile

    private String UpdaeUser_url = "https://barkandroid.herokuapp.com/change_info";
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
    private EditText userName ,DogType;
    private TextView email;
    private JSONObject obj;
    private Button save;
    private String imageString="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imageView = (ImageView) findViewById(R.id.imgView);
        userName = (EditText)findViewById(R.id.userNameProfile);
        email = (TextView) findViewById(R.id.emailProfile);
        DogType = (EditText)findViewById(R.id.Dog_type);
        save = (Button)findViewById(R.id.btn_signup);


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

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.dog);

        imageView.setImageBitmap(getRoundedShape(icon));
        Button buttonLoadImage = (Button) findViewById(R.id.buttonLoadPicture);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                SaveMyProfileDeatiles();
            }

        });


        loadProfile();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imageView.setImageBitmap(getRoundedShape(BitmapFactory.decodeFile(picturePath)));

             imageString = getStringImage(getRoundedShape(BitmapFactory.decodeFile(picturePath)));
        }


    }


    private void loadProfile(){
        try {
            userName.setText(obj.getString("ownerName"));
            email.setText(obj.getString("email"));
            DogType.setText(obj.getString("dogName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void  SaveMyProfileDeatiles(){

        String temp="";

        String _gender="";
        try {
            temp = obj.getString("_id");

            _gender = obj.getString("gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> paramsUser = new HashMap();
        paramsUser.put("userId", temp);
        paramsUser.put("gender", _gender);

        paramsUser.put("ownerName", userName.getText().toString());
        paramsUser.put("dogName", DogType.getText().toString());

        if(!imageString.equals(""))
            paramsUser.put("photoBase64", imageString);





        JSONObject UserParameters = new JSONObject(paramsUser);

        JsonObjectRequest jsObjRequestV2 = new JsonObjectRequest
                (Request.Method.POST, UpdaeUser_url, UserParameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("\n\n\n\nFIX ME!!! error: " + error.toString()+"\n\n\n\n");


                        Toast.makeText(Profile.this, "Profile was update",
                                Toast.LENGTH_SHORT).show();
                        finish();


                    }
                });
        MySingleton.getInstance(Profile.this).addToRequestque(jsObjRequestV2);


    }

    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 300;
        int targetHeight = 330;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
