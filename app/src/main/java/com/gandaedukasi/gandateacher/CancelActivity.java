package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class CancelActivity extends AppCompatActivity {
    private String id,jadwal_id,pertemuan,label_mapel,label_tanggal,label_waktu,label_tempat;
    private Button btnKirim;
    private EditText keterangan;
    private TextView studentPertemuan,studentLecture,studentDate,studentTime,studentPlace;
    private Session session;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(CancelActivity.this);
        id = getIntent().getStringExtra("id");
        jadwal_id = getIntent().getStringExtra("jadwal_id");
        pertemuan = getIntent().getStringExtra("pertemuan");
        label_mapel = getIntent().getStringExtra("label_mapel");
        label_tanggal = getIntent().getStringExtra("label_tanggal");
        label_waktu = getIntent().getStringExtra("label_waktu");
        label_tempat = getIntent().getStringExtra("label_tempat");

        setContentView(R.layout.activity_cancel);

        btnKirim = (Button) findViewById(R.id.btnKirim);
        studentPertemuan = (TextView) findViewById(R.id.studentPertemuan);
        studentLecture = (TextView) findViewById(R.id.studentLecture);
        studentDate = (TextView) findViewById(R.id.studentDate);
        studentTime = (TextView) findViewById(R.id.studentTime);
        studentPlace = (TextView) findViewById(R.id.studentPlace);

        keterangan = (EditText) findViewById(R.id.keterangan);

        studentPertemuan.setText(pertemuan);
        studentLecture.setText(label_mapel);
        studentDate.setText(label_tanggal);
        studentTime.setText(label_waktu);
        studentPlace.setText(label_tempat);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel_jadwal();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void cancel_jadwal(){
        keterangan.setError(null);

        String ket = keterangan.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(ket)) {
            keterangan.setError("Keterangan tidak boleh kosong");
            focusView = keterangan;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else {
            if(isNetworkAvailable()){
                pDialog = new ProgressDialog(CancelActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                String url = new RequestServer().getServer_url()+"cancelJadwal";

                JsonObject jsonReq = new JsonObject();
                jsonReq.addProperty("user_id", session.getUserId());
                jsonReq.addProperty("dt_jadwal_id", id);
                jsonReq.addProperty("requested_by", "PENGAJAR");
                jsonReq.addProperty("type", "CC");
                jsonReq.addProperty("keterangan", ket);

                Log.d("Test Request",">"+jsonReq);

                Ion.with(CancelActivity.this)
                        .load(url)
                        //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                        .setJsonObjectBody(jsonReq)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                Log.d("Response",">"+result);
                                try{
                                    String status = result.get("status").toString();
                                    if (status.equals("1")){
                                        new AlertDialog.Builder(CancelActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Berhasil")
                                                .setMessage("Permintaan pembatalan jadwal berhasil dikirim. Mohon tunggu konfirmasi dari support kami.")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                })
                                                .show();
                                        //Toast.makeText(getApplicationContext(), "Permintaan pembatalan jadwal berhasil dikirim. Mohon tunggu konfirmasi dari support kami.", Toast.LENGTH_LONG).show();
                                    }else{
                                        String error = result.get("error").getAsString();
                                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception ex){
                                    Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                }
                                pDialog.dismiss();
                            }
                        });
            }
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
