package com.example.het.mex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import java.util.concurrent.ExecutionException;

import static com.example.het.mex.HomePage_Employee.decodeBase64;

public class Man_DecodeImage extends AppCompatActivity {
    private static final String URL = "https://mex123456-turbulent-cheetah.eu-gb.mybluemix.net/claim/";
    String resultObject;
    String image;
    String name,busniessType,description;
    String ClaimId;
    String MailSubject = "Approval Status for Claim [MeX]",MailMessage,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man__decode_image);

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

        name =data.getString("name");
        final TextView textView3 =(TextView) findViewById(R.id.textView3);
        textView3.setText(name);

        busniessType =data.getString("busniessType");
        final TextView textView9 =(TextView) findViewById(R.id.textView9);
        textView9.setText(busniessType);

        description =data.getString("description");
        final TextView textView10 =(TextView) findViewById(R.id.textView10);
        textView10.setText(description);

        ClaimId = data.getString("ClaimId");



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
                                email = dataobj.getString("email");
                            }

                            ProgressDialog nDialog;
                            nDialog = new ProgressDialog(Man_DecodeImage.this);
                            nDialog.setMessage("Loading..");
                            nDialog.setTitle("Get Data");
                            nDialog.setIndeterminate(false);
                            nDialog.setCancelable(false);
                            nDialog.show();
                            String finalImageString = URLDecoder.decode(image,"UTF-8");
                            Bitmap imageBitmap = decodeBase64(finalImageString);
                            nDialog.dismiss();


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
                        Toast.makeText(Man_DecodeImage.this,"Error",Toast.LENGTH_SHORT).show();

                    }
                });

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


//        byte[] byteArray = getIntent().getByteArrayExtra("image");
//        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//        ImageView imgvw = (ImageView)findViewById(R.id.imageView);
//        imgvw.setImageBitmap(bmp);
    }

    public void ApproveBtnClicked(View view) {

//        PUT data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, URL+name+"/"+ClaimId+"/"+"approved", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resp = response.getString("result");
                            if (resp.equals("true")) {

                                MailMessage  = "Hello "+name+","+"\nYour Claim for following details has been approved"+"\nName: "+name + "\nBusiness Type: "+busniessType + "\nDescription: "+description +"\n\nAdmin,"+"\nMeX";

                                Toast.makeText(Man_DecodeImage.this,"Successfully approved!",Toast.LENGTH_SHORT).show();
//                                sendEmail();
                                if(email.equals("null")){
                                    Toast.makeText(Man_DecodeImage.this,"Email not found",Toast.LENGTH_SHORT).show();
                                    sendHome();
                                }
                                else {
                                    sendEmail();
                                }

                            } else {
                                Toast.makeText(Man_DecodeImage.this, "Error approving bill", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Man_DecodeImage.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    public void DisApproveBtnClicked(View view) {

        //        PUT data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, URL+name+"/"+ClaimId+"/"+"disapproved", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resp = response.getString("result");
                            if (resp.equals("true")) {

                                MailMessage  = "Hello "+name+","+"\nYour Claim for following details has been disapproved"+"\nName: "+name + "\nBusiness Type: "+busniessType + "\nDescription: "+description +"\n\nAdmin,"+"\nMeX";

                                Toast.makeText(Man_DecodeImage.this,"Successfully disapproved!",Toast.LENGTH_SHORT).show();

                                if(email.equals("null")){
                                    Toast.makeText(Man_DecodeImage.this,"Email not found",Toast.LENGTH_SHORT).show();
                                    sendHome();
                                }
                                else {
                                    sendEmail();
                                }

                            } else {
                                Toast.makeText(Man_DecodeImage.this, "Error disapproving bill", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Man_DecodeImage.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void sendEmail(){
        SendMail1 sm = new SendMail1(this,email,MailSubject,MailMessage);
        sm.execute();
    }

    public void sendHome(){
        Intent i = new Intent(Man_DecodeImage.this,HomePage_Manager.class);
        startActivity(i);
        finish();
    }
}

