package com.gandaedukasi.gandateacher;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RescheduleActivity extends AppCompatActivity {

    private String id,jadwal_id,pertemuan,label_mapel,label_tanggal,label_waktu,label_tempat;
    private Button btnKirim;
    private EditText tglPertemuan,waktuPertemuan,tempatPertemuan,kekterangan;
    private TextView studentPertemuan,studentLecture,studentDate,studentTime,studentPlace;
    private Session session;
    private DatePickerDialog tgl_pertemuan;
    private TimePickerDialog waktu_pertemuan;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(RescheduleActivity.this);
        id = getIntent().getStringExtra("id");
        jadwal_id = getIntent().getStringExtra("jadwal_id");
        pertemuan = getIntent().getStringExtra("pertemuan");
        label_mapel = getIntent().getStringExtra("label_mapel");
        label_tanggal = getIntent().getStringExtra("label_tanggal");
        label_waktu = getIntent().getStringExtra("label_waktu");
        label_tempat = getIntent().getStringExtra("label_tempat");

        setContentView(R.layout.activity_reschedule);

        btnKirim = (Button) findViewById(R.id.btnKirim);
        studentPertemuan = (TextView) findViewById(R.id.studentPertemuan);
        studentLecture = (TextView) findViewById(R.id.studentLecture);
        studentDate = (TextView) findViewById(R.id.studentDate);
        studentTime = (TextView) findViewById(R.id.studentTime);
        studentPlace = (TextView) findViewById(R.id.studentPlace);

        tglPertemuan = (EditText) findViewById(R.id.tglPertemuan);
        waktuPertemuan = (EditText) findViewById(R.id.waktuPertemuan);
        tempatPertemuan = (EditText) findViewById(R.id.tempatPertemuan);
        kekterangan = (EditText) findViewById(R.id.kekterangan);

        studentPertemuan.setText(pertemuan);
        studentLecture.setText(label_mapel);
        studentDate.setText(label_tanggal);
        studentTime.setText(label_waktu);
        studentPlace.setText(label_tempat);

        tglPertemuan.setFocusable(false);
        waktuPertemuan.setFocusable(false);

        setDateTimeField();
        setTimeField();

        tglPertemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tgl_pertemuan.show();
            }
        });

        waktuPertemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waktu_pertemuan.show();
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubah_jadwal();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void ubah_jadwal(){
        tglPertemuan.setError(null);
        tempatPertemuan.setError(null);
        kekterangan.setError(null);

        String tanggal = tglPertemuan.getText().toString();
        String waktu = waktuPertemuan.getText().toString();
        String tempat = tempatPertemuan.getText().toString();
        String ket = kekterangan.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(tanggal)) {
            tglPertemuan.setError("Tanggal pertemuan tidak boleh kosong");
            focusView = tglPertemuan;
            cancel = true;
        }

        if (TextUtils.isEmpty(waktu)) {
            waktuPertemuan.setError("Waktu pertemuan tidak boleh kosong");
            focusView = waktuPertemuan;
            cancel = true;
        }

        if (TextUtils.isEmpty(tempat)) {
            tempatPertemuan.setError("Tempat pertemuan tidak boleh kosong");
            focusView = tempatPertemuan;
            cancel = true;
        }

        if (TextUtils.isEmpty(ket)) {
            kekterangan.setError("Keterangan tidak boleh kosong");
            focusView = kekterangan;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else {
            if(isNetworkAvailable()){
                pDialog = new ProgressDialog(RescheduleActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                String url = new RequestServer().getServer_url()+"rescheduleJadwal";

                JsonObject jsonReq = new JsonObject();
                jsonReq.addProperty("user_id", session.getUserId());
                jsonReq.addProperty("dt_jadwal_id", id);
                jsonReq.addProperty("requested_by", "PENGAJAR");
                jsonReq.addProperty("type", "RS");
                jsonReq.addProperty("tglPertemuan", tanggal);
                jsonReq.addProperty("waktuPertemuan", waktu);
                jsonReq.addProperty("tempatPertemuan", tempat);
                jsonReq.addProperty("keterangan", ket);

                Log.d("Test Request",">"+jsonReq);

                Ion.with(RescheduleActivity.this)
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
                                        new AlertDialog.Builder(RescheduleActivity.this)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Berhasil")
                                                .setMessage("Permintaan perubahan jadwal berhasil dikirim. Mohon tunggu konfirmasi dari support kami.")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                })
                                                .show();
                                        //Toast.makeText(getApplicationContext(), "Permintaan perubahan jadwal berhasil dikirim. Mohon tunggu konfirmasi dari support kami.", Toast.LENGTH_LONG).show();
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

    private void setDateTimeField() {
        final Calendar newCalendar = Calendar.getInstance();
        newCalendar.set(newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH)+7);
        tgl_pertemuan = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                String formatted = formater.format(newDate.getTime());
                if(newDate.after(newCalendar)){
                    tglPertemuan.setText(formatted);
                }else{
                    Toast.makeText(getApplicationContext(), "Pertemuan minimal 7 hari kedepan", Toast.LENGTH_LONG).show();
                }

            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    private void setTimeField() {
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        waktu_pertemuan = new TimePickerDialog(RescheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                waktuPertemuan.setText( hourOfDay + ":" + minuteOfDay);
            }
        }, hour, minute, true);
        waktu_pertemuan.setTitle("Waktu Pertemuan");
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
