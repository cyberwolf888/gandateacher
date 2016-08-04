package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainActivity extends AppCompatActivity {
    Session session;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(MainActivity.this);
        if(!session.isLoggedIn()){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        cekProfile();

        setContentView(R.layout.activity_main);

        ImageView btnRequestMengajar, btnSubmitPertemuan, btnJadwalLes, btnLihatPertemuan, btnProfile;

        btnRequestMengajar = (ImageView)findViewById(R.id.btnRequestMengajar);
        btnSubmitPertemuan = (ImageView)findViewById(R.id.btnSubmitPertemuan);
        btnJadwalLes = (ImageView)findViewById(R.id.btnJadwalLes);
        btnLihatPertemuan = (ImageView)findViewById(R.id.btnLihatPertemuan);
        btnProfile = (ImageView)findViewById(R.id.btnProfile);

        btnRequestMengajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RequestActivity.class);
                startActivity(i);
            }
        });

        btnSubmitPertemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SubmitActivity.class);
                startActivity(i);
            }
        });

        btnJadwalLes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JadwalActivity.class);
                startActivity(i);
            }
        });

        btnLihatPertemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PertemuanActivity.class);
                startActivity(i);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });
    }

    private void cekProfile(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            String url = new RequestServer().getServer_url()+"cekProfilePengajar";
            Log.d("Cek Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());
            Log.d("Cek Req",">"+jsonReq);

            Ion.with(MainActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                Log.d("Cek Req",">"+result);
                                String status = result.get("status").getAsString();
                                if (status.equals("1")){
                                    String isNewAccount = result.get("isNewAccount").getAsString();
                                    Log.d("Cek isNewAccount",">"+isNewAccount);
                                    if (isNewAccount.equals("1")){
                                        Intent i = new Intent(MainActivity.this, TingkatPendidikanActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }else{
                                    //TODO jika status 0
                                }
                            } catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });

        }else {
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
        }

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            session.logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
