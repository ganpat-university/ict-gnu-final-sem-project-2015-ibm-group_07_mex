package com.example.het.mex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Reset_Password extends AppCompatActivity {
    private static final String URL = "https://mex123456-turbulent-cheetah.eu-gb.mybluemix.net/forgetpassword/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);

        mainFun();
    }

    public void mainFun(){
        //        Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Reset Password");
        setSupportActionBar(toolbar);
    }

    public void SubmitBtnClicked(View view) {
        EditText pwd = (EditText)findViewById(R.id.editText);
        EditText pwd1 = (EditText)findViewById(R.id.editText1);
        String pass = pwd.getText().toString();
        String pass1 = pwd1.getText().toString();
        if(pass.length()!=0 && pass1.length()!=0 && pass.equals(pass1)){

            SharedPreferences sharedPref = getSharedPreferences("OTPVerification", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
//        editor.clear();
//        editor.commit();
            String Email;
            Email = sharedPref.getString("Email","");

            if(Email.length() == 0){
                SharedPreferences sharedpref = getSharedPreferences("UniversalDetails", Context.MODE_PRIVATE);
                Email = sharedpref.getString("Email","");
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, URL+Email+"/"+pass, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getString("result").equals("true")){
                                    Toast.makeText(Reset_Password.this,"Password reset successfully!!",Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Reset_Password.this,MainActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else if(response.getString("result").equals("Not Found")){
                                    Toast.makeText(Reset_Password.this,"An error occurred",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(Reset_Password.this,"An error occurred",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Toast.makeText(Reset_Password.this,"Email does not exist, Please register first",Toast.LENGTH_SHORT).show();
                        }
                    });
            editor.clear();
            editor.apply();

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
        else {
            Toast.makeText(Reset_Password.this,"Passwords empty or do not match",Toast.LENGTH_SHORT).show();
        }
    }
}
