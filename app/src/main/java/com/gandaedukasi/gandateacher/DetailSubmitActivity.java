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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class DetailSubmitActivity extends AppCompatActivity {
    private String jadwal_id, detail_jadwal_id, nama_siswa, no_telp, photo, label_mapel, label_tanggal, label_waktu, label_tempat, pertemuan;
    private TextView studentName, studentSchool, studentPertemuan, studentLecture, studentDate, studentTime, studentPlace;
    private ImageView studentPhoto;
    private EditText etKelebihanWaktu,etKeterangan;
    private Button btnSubmit;
    protected ProgressDialog pDialog;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(DetailSubmitActivity.this);
        jadwal_id = getIntent().getStringExtra("jadwal_id");
        detail_jadwal_id = getIntent().getStringExtra("detail_jadwal_id");
        nama_siswa = getIntent().getStringExtra("nama_siswa");
        no_telp = getIntent().getStringExtra("no_telp");
        photo = getIntent().getStringExtra("photo");
        label_mapel = getIntent().getStringExtra("label_mapel");
        label_tanggal = getIntent().getStringExtra("label_tanggal");
        label_waktu = getIntent().getStringExtra("label_waktu");
        label_tempat = getIntent().getStringExtra("label_tempat");
        pertemuan = getIntent().getStringExtra("pertemuan");

        setContentView(R.layout.activity_detail_submit);

        studentName = (TextView) findViewById(R.id.studentName);
        studentSchool = (TextView) findViewById(R.id.studentSchool);
        studentPertemuan = (TextView) findViewById(R.id.studentPertemuan);
        studentLecture = (TextView) findViewById(R.id.studentLecture);
        studentDate = (TextView) findViewById(R.id.studentDate);
        studentTime = (TextView) findViewById(R.id.studentTime);
        studentPlace = (TextView) findViewById(R.id.studentPlace);
        studentPhoto = (ImageView) findViewById(R.id.studentPhoto);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        etKelebihanWaktu = (EditText) findViewById(R.id.etKelebihanWaktu);
        etKeterangan = (EditText) findViewById(R.id.etKeterangan);

        studentName.setText(nama_siswa);
        studentSchool.setText(no_telp);
        studentPertemuan.setText(pertemuan);
        studentLecture.setText(label_mapel);
        studentDate.setText(label_tanggal);
        studentTime.setText(label_waktu);
        studentPlace.setText(label_tempat);
        if(photo.equals("")){
            studentPhoto.setImageResource(R.drawable.guest);
        }else{
            Ion.with(DetailSubmitActivity.this)
                    .load(new RequestServer().getPhotoUrl()+"siswa/"+photo)
                    .withBitmap()
                    .placeholder(R.drawable.guest)
                    .error(R.drawable.guest)
                    .intoImageView(studentPhoto);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempSubmit();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void attempSubmit(){
        etKelebihanWaktu.setError(null);
        etKeterangan.setError(null);

        String kelebihanWaktu = etKelebihanWaktu.getText().toString();
        String keterangan = etKeterangan.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //validasi alamat
        if (TextUtils.isEmpty(kelebihanWaktu)) {
            etKelebihanWaktu.setError("Kelebihan waktu tidak boleh kosong");
            focusView = etKelebihanWaktu;
            cancel = true;
        }

        //validasi edukasi
        if (TextUtils.isEmpty(keterangan)) {
            etKeterangan.setError("Keterangan tidak boleh kosong");
            focusView = etKeterangan;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else {
            if(isNetworkAvailable()){
                pDialog = new ProgressDialog(DetailSubmitActivity.this);
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

                String url = new RequestServer().getServer_url() + "createHistory";

                JsonObject jsonReq = new JsonObject();
                jsonReq.addProperty("user_id", session.getUserId());
                jsonReq.addProperty("jadwal_id", jadwal_id);
                jsonReq.addProperty("detail_jadwal_id", detail_jadwal_id);
                jsonReq.addProperty("kelebihanWaktu", kelebihanWaktu);
                jsonReq.addProperty("keterangan", keterangan);
                Log.e("jsonReq",">"+jsonReq);

                Ion.with(DetailSubmitActivity.this)
                        .load(url)
                        .setJsonObjectBody(jsonReq)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try{
                                    String status = result.get("status").getAsString();
                                    if (status.equals("1")) {
                                        new AlertDialog.Builder(DetailSubmitActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Berhasil!")
                                                .setMessage("Data berhasil ditambahkan")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), result.get("error").getAsString(), Toast.LENGTH_LONG).show();
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
