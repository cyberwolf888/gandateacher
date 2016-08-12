package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.adapter.KetersedianJadwalAdapter;
import com.gandaedukasi.gandateacher.models.KetersedianJadwal;
import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class KetersediaanJadwalActivity extends AppCompatActivity {

    private List<KetersedianJadwal> ketersediaanJadwals;
    protected RecyclerView mRecyclerView;
    protected KetersedianJadwalAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProgressDialog pDialog;
    public JsonArray data;

    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(KetersediaanJadwalActivity.this);
        setContentView(R.layout.activity_ketersediaan_jadwal);

        mRecyclerView = (RecyclerView) findViewById(R.id.rvKetersedianJadwal);
        mLayoutManager = new LinearLayoutManager(KetersediaanJadwalActivity.this);
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(KetersediaanJadwalActivity.this, MainActivity.class);
        ComponentName cn = i.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(mainIntent);
    }

    private void getData(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(KetersediaanJadwalActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            ketersediaanJadwals = new ArrayList<>();
            data = new JsonArray();
            String url = new RequestServer().getServer_url() + "getJadwalPengajar";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());

            Ion.with(KetersediaanJadwalActivity.this)
                    .load(url)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").getAsString();
                                if (status.equals("1")) {
                                    data = result.getAsJsonArray("data");
                                    for (int i=0; i<data.size(); i++){
                                        JsonObject objData = data.get(i).getAsJsonObject();
                                        ketersediaanJadwals.add(new KetersedianJadwal(
                                                objData.get("id").getAsString(),
                                                objData.get("mapel_id").getAsString(),
                                                objData.get("zona_id").getAsString(),
                                                objData.get("label_cabang").getAsString(),
                                                objData.get("label_mapel").getAsString(),
                                                objData.get("jam_mulai").getAsString(),
                                                objData.get("jam_selesai").getAsString(),
                                                objData.get("label_hari").getAsString(),
                                                objData.get("status").getAsString()
                                                ));
                                    }
                                    mAdapter = new KetersedianJadwalAdapter(KetersediaanJadwalActivity.this, ketersediaanJadwals);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Tidak jadwal yang tersedia!", Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jadwal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent i = new Intent(KetersediaanJadwalActivity.this,TambahJadwalActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
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
}
