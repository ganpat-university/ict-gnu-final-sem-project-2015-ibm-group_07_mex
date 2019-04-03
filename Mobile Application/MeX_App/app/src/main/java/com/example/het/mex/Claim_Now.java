package com.example.het.mex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Claim_Now extends AppCompatActivity {
    String selectedItem;
    String Uniname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim__now);
        SharedPreferences sharedpref = getSharedPreferences("UniversalDetails", Context.MODE_PRIVATE);
        Uniname = sharedpref.getString("Name","");

        mainFun();
    }

    public void mainFun(){
//        Bundle data = getIntent().getExtras();
//        if(data == null){
//            Toast.makeText(Claim_Now.this,"Cannot fetch name",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        Uniname =data.getString("UniversalName");
        TextView nameTxt = (TextView)findViewById(R.id.UniName);
        nameTxt.setText(Uniname);

//        Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Claim Expense");
        setSupportActionBar(toolbar);

        //        Business category dropdown
        Spinner spinner =(Spinner) findViewById(R.id.buscateg_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.bcat_array_CN,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        Get item selected selected from dropdown
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void SubmitBtnClicked(View view){
        Intent i = new Intent(Claim_Now.this,Take_Photo.class);

        final EditText desc_inp = (EditText) findViewById(R.id.desc);
//        final EditText con_inp = (EditText) findViewById(R.id.contact);

        String desc_id = desc_inp.getText().toString();
//        String con = con_inp.getText().toString();

//        i.putExtra("UniversalName",Uniname);
        i.putExtra("desc_id",desc_id);
        i.putExtra("bType",selectedItem);
//        i.putExtra("con",con);

        startActivity(i);
        finish();
    }
}
