package com.example.het.mex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class HomePage_Manager extends AppCompatActivity {
    String selectedItem;
    String resultObject;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page__manager);

        mainFun();
    }
    public void mainFun(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //        Toolbar with a side nav button and functionality
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(false);

//                Item click handler
                switch (menuItem.getItemId()) {
//                    case R.id.claim_now:
//                        Intent intent = new Intent(HomePage_Employee.this, Claim_Now.class);
//                        intent.putExtra("UniversalName",Uniname);
//                        startActivity(intent);
//                    case R.id.logout:
//                        Intent i = new Intent(HomePage_Employee.this,MainActivity.class);
//                        startActivity(i);
//                        finish();
//                    case R.id.rpass:
//                        Intent in = new Intent(HomePage_Employee.this,Reset_Password.class);
//                        startActivity(in);
                }

                mDrawerLayout.closeDrawers();

//                Temp toast for item clicks
//                Toast.makeText(HomePage_Employee.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if(menuItem.getTitle().equals("Home")){
                    Intent i = new Intent(HomePage_Manager.this,HomePage_Manager.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                }
                else if(menuItem.getTitle().equals("Reset Password")){
                    Intent in = new Intent(HomePage_Manager.this,Reset_Password.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(in);

                }
                else if(menuItem.getTitle().equals("Logout")){
                    Intent i = new Intent(HomePage_Manager.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(menuItem.getTitle().equals("Approved Claims")){
                    Intent i = new Intent(HomePage_Manager.this,Man_ApprovedClaims.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);

                }
                else if(menuItem.getTitle().equals("Disapproved Claims")){
                    Intent i = new Intent(HomePage_Manager.this,Man_DisapprovedClaims.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(i);
                }
                return true;
            }
        });

        //        Business category dropdown
        Spinner spinner =(Spinner) findViewById(R.id.buscateg_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.bcat_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        Get item selected selected from dropdown
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_SHORT).show();

                GetSearchResults(new CallBack() {
                    @Override
                    public void onSuccess(ArrayList<getClaimData> ClaimDetails) {
                        ArrayList<getClaimData> searchResults = ClaimDetails;
                        final ListView lv1 = (ListView) findViewById(R.id.ListView01);
                        lv1.setAdapter(new MyCustomBaseAdapter(HomePage_Manager.this, searchResults));

                        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                Object o = lv1.getItemAtPosition(position);
                                getClaimData fullObject = (getClaimData)o;


                                Intent i = new Intent(HomePage_Manager.this,Man_DecodeImage.class);
                                i.putExtra("busniessType",fullObject.busniessType);
                                i.putExtra("name",fullObject.name);
                                i.putExtra("description",fullObject.description);
                                i.putExtra("ClaimId",fullObject.id);
                                startActivity(i);

                            }
                        });
                    }

                    @Override
                    public void onFail(String msg) {
                        Toast.makeText(HomePage_Manager.this,msg,Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void GetSearchResults(final CallBack onCallBack){
        if(selectedItem.equals("All Claims")){
            String URL = "https://mex123456-turbulent-cheetah.eu-gb.mybluemix.net/claim";
            //        Get data
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                resultObject = response.getString("result");
                                JSONArray dataarr = new JSONArray(resultObject);
                                ArrayList<String> arr = new ArrayList<String>();
                                ArrayList<String> arr1 = new ArrayList<String>();
                                ArrayList<String> arr2 = new ArrayList<String>();
                                ArrayList<String> name_arr = new ArrayList<String>();
                                ArrayList<String> id_arr = new ArrayList<String>();
                                ArrayList<getClaimData> results = new ArrayList<getClaimData>();


                                for( int i = 0 ;i<dataarr.length();i++){
                                    JSONObject dataobj = dataarr.getJSONObject(i);
                                    String busniesstype = dataobj.getString("busniesstype");
                                    String date = dataobj.getString("date");
                                    String description = dataobj.getString("description");
                                    String name = dataobj.getString("name");
                                    String id = dataobj.getString("claim_id");
                                    String status = dataobj.getString("status");

                                    if(status.equals("progress")){
                                        arr.add(busniesstype);
                                        arr1.add(date);
                                        arr2.add(description);
                                        name_arr.add(name);
                                        id_arr.add(id);
                                    }
                                }

                                for (int i = 0;i<arr.size();i++){
                                    getClaimData srl = new getClaimData();
                                    srl.setbusniessType(arr.get(i));
                                    srl.setdate(arr1.get(i));
                                    srl.setdescription(arr2.get(i));
                                    srl.setName(name_arr.get(i));
                                    srl.setClaimId(id_arr.get(i));
                                    results.add(srl);
                                }

                                onCallBack.onSuccess(results);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Toast.makeText(HomePage_Manager.this,"Error",Toast.LENGTH_SHORT).show();

                        }
                    });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

// Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
        else {
            String URL = "https://mex123456-turbulent-cheetah.eu-gb.mybluemix.net/busniesstype/";
            //        Get data
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, URL+selectedItem, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                resultObject = response.getString("result");
                                JSONArray dataarr = new JSONArray(resultObject);
                                ArrayList<String> arr = new ArrayList<String>();
                                ArrayList<String> arr1 = new ArrayList<String>();
                                ArrayList<String> arr2 = new ArrayList<String>();
                                ArrayList<String> name_arr = new ArrayList<String>();
                                ArrayList<String> id_arr = new ArrayList<String>();
                                ArrayList<getClaimData> results = new ArrayList<getClaimData>();


                                for( int i = 0 ;i<dataarr.length();i++){
                                    JSONObject dataobj = dataarr.getJSONObject(i);
                                    String busniesstype = dataobj.getString("busniesstype");
                                    String date = dataobj.getString("date");
                                    String description = dataobj.getString("description");
                                    String name = dataobj.getString("name");
                                    String id = dataobj.getString("claim_id");
                                    String status = dataobj.getString("status");

                                    if(status.equals("progress")){
                                        arr.add(busniesstype);
                                        arr1.add(date);
                                        arr2.add(description);
                                        name_arr.add(name);
                                        id_arr.add(id);
                                    }
                                }

                                for (int i = 0;i<arr.size();i++){
                                    getClaimData srl = new getClaimData();
                                    srl.setbusniessType(arr.get(i));
                                    srl.setdate(arr1.get(i));
                                    srl.setdescription(arr2.get(i));
                                    srl.setName(name_arr.get(i));
                                    srl.setClaimId(id_arr.get(i));
                                    results.add(srl);
                                }

                                onCallBack.onSuccess(results);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Toast.makeText(HomePage_Manager.this,"Error",Toast.LENGTH_SHORT).show();

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

    public class getClaimData {
        private String busniessType = "";
        private String date = "";
        private String description = "";
        private String name = "";
        //        private String image = "";
        private String id = "";

        public void setbusniessType(String busniessType) {
            this.busniessType = busniessType;
        }

        public String getbusniessType() {
            return busniessType;
        }

        public void setdate(String date) {
            this.date = date;
        }

        public String getdate() {
            return date;
        }

        public void setdescription(String description) {
            this.description = description;
        }

        public String getdescription() {
            return description;
        }

//        public void setImage(String image){this.image = image;}
//
//        public String getImage(){return image;}

        public void setClaimId(String id){this.id = id;}

        public String getClaimId(){return id;}

        public void setName(String name){this.name = name;}

        public String getName(){return name;}
    }

    public class MyCustomBaseAdapter extends BaseAdapter {
        private ArrayList<getClaimData> searchArrayList;

        private LayoutInflater mInflater;

        public MyCustomBaseAdapter(Context context, ArrayList<getClaimData> results) {
            searchArrayList = results;
            mInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return searchArrayList.size();
        }

        public Object getItem(int position) {
            return searchArrayList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_row_view, null);
                holder = new ViewHolder();
                holder.txtbType = (TextView) convertView.findViewById(R.id.textView2);
                holder.txtdate = (TextView) convertView.findViewById(R.id.textView4);
                holder.txtdesc = (TextView) convertView.findViewById(R.id.textView5);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtbType.setText(searchArrayList.get(position).getName());
            holder.txtdate.setText(searchArrayList.get(position).getdate());
            holder.txtdesc.setText(searchArrayList.get(position).getdescription());

            return convertView;
        }


    }
    static class ViewHolder {
        TextView txtbType;
        TextView txtdate;
        TextView txtdesc;
    }

    public interface CallBack {
        void onSuccess(ArrayList<getClaimData> ClaimDetails);

        void onFail(String msg);
    }

    //    Action bar click handler
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);


    }
}
