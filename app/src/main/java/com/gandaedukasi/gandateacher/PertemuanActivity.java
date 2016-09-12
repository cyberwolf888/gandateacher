package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.adapter.HistoryAdapter;
import com.gandaedukasi.gandateacher.models.History;
import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class PertemuanActivity extends AppCompatActivity {
    private List<History> histories;
    protected RecyclerView mRecyclerView;
    protected HistoryAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ProgressDialog pDialog;
    public JsonArray data;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(PertemuanActivity.this);
        setContentView(R.layout.activity_pertemuan);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvHistory);
        mLayoutManager = new LinearLayoutManager(PertemuanActivity.this);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PertemuanActivity.this, MainActivity.class);
        ComponentName cn = i.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(mainIntent);
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }

    private void getData(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(PertemuanActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            pDialog.show();

            histories = new ArrayList<>();
            data = new JsonArray();
            String url = new RequestServer().getServer_url() + "getHistoryPengajar";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());
            Log.d("jsonReq",">"+jsonReq);
            Ion.with(PertemuanActivity.this)
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
                                    Log.d("Data",">"+data);
                                    for (int i=0; i<data.size(); i++){
                                        JsonObject objData = data.get(i).getAsJsonObject();
                                        String photo = "";
                                        if(!objData.get("photo").isJsonNull()){
                                            photo = objData.get("photo").getAsString();
                                        }
                                        histories.add(new History(
                                                objData.get("jadwal_id").getAsString(),
                                                objData.get("detail_jadwal_id").getAsString(),
                                                objData.get("nama_siswa").getAsString(),
                                                objData.get("no_telp").getAsString(),
                                                photo,
                                                objData.get("label_mapel").getAsString(),
                                                objData.get("label_tanggal").getAsString(),
                                                objData.get("label_waktu").getAsString(),
                                                objData.get("label_tempat").getAsString(),
                                                objData.get("pertemuan").getAsString(),
                                                objData.get("keterangan").getAsString(),
                                                objData.get("tambahan_jam").getAsString()
                                        ));
                                    }
                                    Log.d("histories",">"+histories.size());
                                    mAdapter = new HistoryAdapter(PertemuanActivity.this, histories);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                }else{
                                    new AlertDialog.Builder(PertemuanActivity.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Kosong")
                                            .setMessage("Maaf, pertemuan sedang tidak tersedia")
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent i = new Intent(PertemuanActivity.this, MainActivity.class);
                                                    ComponentName cn = i.getComponent();
                                                    Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                                                    startActivity(mainIntent);
                                                }
                                            })
                                            .show();
                                    //Toast.makeText(getApplicationContext(), "Jadwal sedang kosong", Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Log.e("Erooooor",">"+ex.toString());
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

}
