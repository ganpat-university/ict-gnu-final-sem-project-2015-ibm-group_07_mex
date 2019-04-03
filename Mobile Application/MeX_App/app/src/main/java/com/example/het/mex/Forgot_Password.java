package com.example.het.mex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Forgot_Password extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        mainFun();

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        buttonSend = (Button) findViewById(R.id.buttonSend);

        //Adding click listener
        buttonSend.setOnClickListener(this);
    }

    private void sendEmail() {
        String otpString  =generateOTP();
        //Getting content for email
        String email = editTextEmail.getText().toString().trim().toLowerCase();
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if(email.length() == 0 || !matcher.matches()){
            Toast.makeText(Forgot_Password.this,"Email id is invalid or empty",Toast.LENGTH_SHORT).show();
        }
        else {
            String subject = "Password reset link [MeX]";
            String message = "Hello,"+"\nYour OTP for password reset is "+otpString + "\n\nAdmin,"+"\nMeX";

            SharedPreferences sharedPref = getSharedPreferences("OTPVerification", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("OTP",otpString);
            editor.putString("Email",email);
            editor.apply();



            //Creating SendMail object
            SendMail sm = new SendMail(this, email, subject, message);

            //Executing sendmail to send email
            sm.execute();

//        Set visibility to true
            EditText editTextOTP = (EditText) findViewById(R.id.editTextOTP);
            editTextOTP.setVisibility(View.VISIBLE);
            Button sbmtbtn = (Button)findViewById(R.id.buttonSendOTP);
            sbmtbtn.setVisibility(View.VISIBLE);
            TextView textViewOTP = (TextView)findViewById(R.id.textViewOTP);
            textViewOTP.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        sendEmail();
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }


    public void mainFun(){
        EditText editTextOTP = (EditText) findViewById(R.id.editTextOTP);
        editTextOTP.setVisibility(View.GONE);
        Button sbmtbtn = (Button)findViewById(R.id.buttonSendOTP);
        sbmtbtn.setVisibility(View.GONE);
        TextView textViewOTP = (TextView)findViewById(R.id.textViewOTP);
        textViewOTP.setVisibility(View.GONE);


        //        Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Forgot Password");
        setSupportActionBar(toolbar);
    }

    public void SubmitBtnClicked(View view) {
        SharedPreferences sharedPref = getSharedPreferences("OTPVerification", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.clear();
//        editor.commit();

        String OTP = sharedPref.getString("OTP","");
//        String Email = sharedPref.getString("Email","");

        EditText editTextOTP = (EditText) findViewById(R.id.editTextOTP);
        if(OTP.equals(editTextOTP.getText().toString())){
            Toast.makeText(Forgot_Password.this,"OTP matched",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Forgot_Password.this,Reset_Password.class);
            startActivity(i);
            finish();
        }
        else {
            Toast.makeText(Forgot_Password.this,"Wrong OTP",Toast.LENGTH_SHORT).show();
        }

    }

    public static String generateOTP() {
        int randomPin   =(int)(Math.random()*9000)+1000;
        String otp  =String.valueOf(randomPin);
        return otp;
    }


}
