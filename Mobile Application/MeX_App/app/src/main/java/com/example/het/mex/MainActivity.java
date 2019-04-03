package com.example.het.mex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String checkRestrue="true";

    //    private static final String URL = "https://mex1242523-busy-ratel.eu-gb.mybluemix.net/login/het@email.com/pwd123";
    private static final String URL = "https://mex123456-turbulent-cheetah.eu-gb.mybluemix.net/";
//    private static final String POST_URL = "http://ptsv2.com/t/dz7dh-1551415957/post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFun();
    }

    public void mainFun(){
        //        Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //    Submit button click handler
    public void SubmitButtonClicked(View view){
        final EditText usrnam = (EditText)findViewById(R.id.username);
        final EditText pwd = (EditText)findViewById(R.id.password);

        String username = usrnam.getText().toString().trim().toLowerCase();
        String password = pwd.getText().toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL+"login/"+username+"/"+password, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String resp = response.getString("result");
                            if(resp.equals("Not Found")){
                                Toast.makeText(MainActivity.this,"Invalid credentials, please try again",Toast.LENGTH_SHORT).show();
                            }
                            else if(resp.equals("Password Does not match")){
                                Toast.makeText(MainActivity.this,"Invalid password",Toast.LENGTH_SHORT).show();
                            }

                            else {
                                JSONObject jObject = new JSONObject(response.toString());
                                JSONObject resultObject = jObject.getJSONObject("result");
                                String res = resultObject.getString("current_status");
                                String name = resultObject.getString("name");
                                String role = resultObject.getString("role");
                                String email = resultObject.getString("email");

                                if(role.equals("User")){
                                    if(res.equals(checkRestrue)){
                                        Intent i = new Intent(MainActivity.this,HomePage_Employee.class);
                                        SharedPreferences sharedpref = getSharedPreferences("UniversalDetails", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor  = sharedpref.edit();
                                        editor.putString("Name",name);
                                        editor.putString("Email",email);
                                        editor.commit();
//                                        i.putExtra("UniversalName",name);
                                        startActivity(i);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this,"Response not received from server",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                else if(role.equals("Manager")){
                                    if(res.equals(checkRestrue)){
                                        Intent i = new Intent(MainActivity.this,HomePage_Manager.class);
                                        SharedPreferences sharedpref = getSharedPreferences("UniversalDetails", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor  = sharedpref.edit();
                                        editor.putString("Name",name);
                                        editor.putString("Email",email);
                                        editor.commit();
//                                        i.putExtra("UniversalName",name);
                                        startActivity(i);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this,"Response not received from server",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this,"No role defined",Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(MainActivity.this,"Invalid or Empty credentials",Toast.LENGTH_SHORT).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    //    Forgot Password button click handler
    public void forgot_pwd(View view){
        Intent f_p = new Intent(MainActivity.this,Forgot_Password.class);
        startActivity(f_p);
    }

    //    Signup button click handler
    public void Signup(View view){
        Intent signup = new Intent(MainActivity.this,Signup.class);
        startActivity(signup);
    }


}
