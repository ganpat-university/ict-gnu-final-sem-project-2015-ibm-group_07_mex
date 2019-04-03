package com.example.het.mex;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import static com.example.het.mex.HomePage_Employee.decodeBase64;

public class Emp_DecodeImage extends AppCompatActivity {
    String resultObject;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp__decode_image);

        mainFun();
    }

    public void mainFun(){
        //        Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Claim");
        setSupportActionBar(toolbar);

        Bundle data = getIntent().getExtras();
        if(data == null){
            return;
        }

        String name =data.getString("name");
        final TextView textView3 =(TextView) findViewById(R.id.textView3);
        textView3.setText(name);

        String busniessType =data.getString("busniessType");
        final TextView textView9 =(TextView) findViewById(R.id.textView9);
        textView9.setText(busniessType);

        String description =data.getString("description");
        final TextView textView10 =(TextView) findViewById(R.id.textView10);
        textView10.setText(description);

        String ClaimId = data.getString("ClaimId");




        String URL = "https://mex123456-turbulent-cheetah.eu-gb.mybluemix.net/claim/"+name+"/"+ClaimId;




//        THE LOGIC

        //        Get data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            resultObject = response.getString("result");
                            JSONArray dataarr = new JSONArray(resultObject);

                            for( int i = 0 ;i<dataarr.length();i++){
                                JSONObject dataobj = dataarr.getJSONObject(i);
                                image = dataobj.getString("image");
                            }

//                            ProgressDialog nDialog;
//                            nDialog = new ProgressDialog(Emp_DecodeImage.this);
//                            nDialog.setMessage("Loading..");
//                            nDialog.setTitle("Get Data");
//                            nDialog.setIndeterminate(false);
//                            nDialog.setCancelable(false);
//                            nDialog.show();
                            String finalImageString = URLDecoder.decode(image,"UTF-8");
                            Bitmap imageBitmap = decodeBase64(finalImageString);
//                            nDialog.dismiss();


                            ImageView imgvw = (ImageView)findViewById(R.id.imageView);
                            imgvw.setImageBitmap(imageBitmap);



                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(Emp_DecodeImage.this,"Error",Toast.LENGTH_SHORT).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }

}
