package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.adapter.DetailRequestJadwalAdapter;
import com.gandaedukasi.gandateacher.models.DetailRequestJadwal;
import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class DetailRequestActivity extends AppCompatActivity {
    private List<DetailRequestJadwal> detailRequestJadwal;
    protected RecyclerView mRecyclerView;
    protected DetailRequestJadwalAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    public JsonArray data;
    private Session session;
    private ProgressDialog pDialog;
    private String jadwal_id, siswa_id, mapel_id;
    private ImageView studentPhoto;
    private TextView studentName,studentSchool;
    private Button btnAccept, btnDecline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(DetailRequestActivity.this);
        jadwal_id = getIntent().getStringExtra("jadwal_id");
        siswa_id = getIntent().getStringExtra("siswa_id");
        mapel_id = getIntent().getStringExtra("mapel_id");
        setContentView(R.layout.activity_detail_request);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvDetailRequestJadwal);
        mLayoutManager = new LinearLayoutManager(DetailRequestActivity.this);
        studentPhoto = (ImageView) findViewById(R.id.studentPhoto);
        studentName = (TextView) findViewById(R.id.studentName);
        studentSchool = (TextView) findViewById(R.id.studentSchool);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnDecline = (Button) findViewById(R.id.btnDecline);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailRequestActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Accept")
                        .setMessage("Apakah anda yakin untuk menerima jadwal ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accept();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();

            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailRequestActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Decline")
                        .setMessage("Apakah anda yakin untuk menolak jadwal ini?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                decline();
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onResume(){
        super.onResume();
        getData();
    }

    private void getData()
    {
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(DetailRequestActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            detailRequestJadwal = new ArrayList<>();
            data = new JsonArray();
            String url = new RequestServer().getServer_url() + "getDetailRequestSiswa";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());
            jsonReq.addProperty("jadwal_id", jadwal_id);
            Log.d("req",">"+jsonReq);
            Ion.with(this)
                    .load(url)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").getAsString();
                                if (status.equals("1")) {
                                    JsonObject objData = result.getAsJsonObject("data");
                                    Log.d("objData",">"+result);
                                    data = objData.getAsJsonArray("detail");

                                    if(!objData.get("photo").isJsonNull()){
                                        Ion.with(DetailRequestActivity.this)
                                                .load(new RequestServer().getPhotoUrl()+"siswa/"+objData.get("photo").getAsString())
                                                .withBitmap()
                                                .placeholder(R.drawable.guest)
                                                .error(R.drawable.guest)
                                                .intoImageView(studentPhoto);
                                    }
                                    studentName.setText(objData.get("nama_siswa").getAsString());
                                    studentSchool.setText(objData.get("no_telp").getAsString());
                                    for (int i=0; i<data.size(); i++){
                                        JsonObject od = data.get(i).getAsJsonObject();
                                        detailRequestJadwal.add(new DetailRequestJadwal(
                                                od.get("id").getAsString(),
                                                od.get("label_mapel").getAsString(),
                                                od.get("label_tanggal").getAsString(),
                                                od.get("label_waktu").getAsString(),
                                                od.get("label_tempat").getAsString()
                                        ));
                                    }
                                    mAdapter = new DetailRequestJadwalAdapter(DetailRequestActivity.this, detailRequestJadwal);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setLayoutManager(mLayoutManager);
                                }else{
                                    //TODO jika status 0
                                }
                            }catch (Exception ex){
                                Log.e("erroooooooor",">"+ex);
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
        }
    }

    private void accept(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(DetailRequestActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            String url = new RequestServer().getServer_url() + "terimaJadwal";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());
            jsonReq.addProperty("jadwal_id", jadwal_id);
            Log.d("req",">"+jsonReq);
            Ion.with(this)
                    .load(url)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").getAsString();
                                if (status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "Jadwal berhasil diterima. Mohon tunggu pembayaran dari siswa.", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(DetailRequestActivity.this,RequestActivity.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    new AlertDialog.Builder(DetailRequestActivity.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle("Gagal!")
                                            .setMessage(result.get("error").getAsString())
                                            .setNegativeButton("Cancel",null)
                                            .show();
                                    //Toast.makeText(getApplicationContext(), result.get("error").getAsString(), Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Log.e("erroooooooor",">"+ex);
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
        }
    }

    private void decline(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(DetailRequestActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Toast.makeText(getApplicationContext(), "Proses dibatalkan!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            String url = new RequestServer().getServer_url() + "tolakJadwal";

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());
            jsonReq.addProperty("jadwal_id", jadwal_id);
            Log.d("req",">"+jsonReq);
            Ion.with(this)
                    .load(url)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").getAsString();
                                if (status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "Jadwal berhasil ditolak.", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(DetailRequestActivity.this,RequestActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }catch (Exception ex){
                                Log.e("erroooooooor",">"+ex);
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else{
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
