package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TambahJadwalActivity extends AppCompatActivity {
    Spinner inputMapel, inputHari;
    EditText inputWaktuMulai, inputWaktuSelesai;
    Button btnTambah;
    Session session;

    private TimePickerDialog timePicker1;
    private TimePickerDialog timePicker2;
    Calendar mcurrentTime = Calendar.getInstance();
    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);

    ProgressDialog pDialog;
    List<String> listMapel = new ArrayList<String>();
    List<String> listHari = new ArrayList<String>();
    ArrayAdapter<String> dataAdapterMapel;
    ArrayAdapter<String> dataAdapterHari;
    JsonArray dataMapel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(TambahJadwalActivity.this);

        setContentView(R.layout.activity_tambah_jadwal);

        inputMapel = (Spinner) findViewById(R.id.inputMapel);
        inputHari = (Spinner) findViewById(R.id.inputHari);
        inputWaktuMulai = (EditText) findViewById(R.id.inputWaktuMulai);
        inputWaktuSelesai = (EditText) findViewById(R.id.inputWaktuSelesai);
        btnTambah = (Button) findViewById(R.id.buttonAdd);

        inputWaktuMulai.setFocusable(false);
        inputWaktuSelesai.setFocusable(false);

        dataAdapterHari = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listHari);
        dataAdapterHari.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputHari.setAdapter(dataAdapterHari);

        dataAdapterMapel = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMapel);
        dataAdapterMapel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputMapel.setAdapter(dataAdapterMapel);

        inputWaktuMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker1 = new TimePickerDialog(TambahJadwalActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        inputWaktuMulai.setText( hourOfDay + ":" + minuteOfDay);
                        hour = hourOfDay+1;
                        minute = minuteOfDay;
                    }
                }, hour, minute, true);
                timePicker1.setTitle("Waktu Mulai");
                timePicker1.show();
            }
        });
        inputWaktuSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2 = new TimePickerDialog(TambahJadwalActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        try{
                            Date inTime = sdf.parse(hour+":"+minute);
                            Date outTime = sdf.parse(hourOfDay+":"+minuteOfDay);
                            if(inTime.compareTo(outTime) > 0){
                                Toast.makeText(getApplicationContext(), "Waktu selesai tidak boleh lebih dari waktu mulai!", Toast.LENGTH_SHORT).show();
                            }else{
                                inputWaktuSelesai.setText( hourOfDay + ":" + minuteOfDay);
                            }
                        }catch (Exception ex){

                        }
                    }
                }, hour, minute, true);
                timePicker2.setTitle("Waktu Selesai");
                timePicker2.show();
            }
        });
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahJadwal();
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        pDialog = new ProgressDialog(TambahJadwalActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        //Set value spinner inputHari
        listHari.add("Senin");
        listHari.add("Selasa");
        listHari.add("Rabu");
        listHari.add("Kamis");
        listHari.add("Jumat");
        listHari.add("Sabtu");
        listHari.add("Minggu");
        dataAdapterHari.notifyDataSetChanged();

        String url = new RequestServer().getServer_url()+"getMapelPengajar";
        Log.d("Login Url",">"+url);

        JsonObject jsonReq = new JsonObject();
        jsonReq.addProperty("user_id", session.getUserId());

        if(isNetworkAvailable()){
            Ion.with(TambahJadwalActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").toString();
                                if (status.equals("1")){
                                    dataMapel = result.getAsJsonArray("data");
                                    for (int i=0; i<dataMapel.size(); i++){
                                        JsonObject objData = dataMapel.get(i).getAsJsonObject();
                                        listMapel.add(objData.get("label").getAsString());
                                    }
                                    dataAdapterMapel.notifyDataSetChanged();
                                }
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
            Intent i = new Intent(TambahJadwalActivity.this, KetersediaanJadwalActivity.class);
            pDialog.dismiss();
            startActivity(i);
            finish();
        }
    }

    public void tambahJadwal(){
        inputWaktuMulai.setError(null);
        inputWaktuSelesai.setError(null);

        JsonObject objData = dataMapel.get(inputMapel.getSelectedItemPosition()).getAsJsonObject();
        String mapel_id = objData.get("id").getAsString();
        String hari = inputHari.getSelectedItem().toString();
        String waktu_mulai = inputWaktuMulai.getText().toString();
        String waktu_selesai = inputWaktuSelesai.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(waktu_mulai)) {
            inputWaktuMulai.setError(getString(R.string.id_error_waktu_mulai));
            focusView = inputWaktuMulai;
            cancel = true;
        }
        if (TextUtils.isEmpty(waktu_selesai)) {
            inputWaktuSelesai.setError(getString(R.string.id_error_waktu_selesai));
            focusView = inputWaktuSelesai;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else {
            if(isNetworkAvailable()){
            pDialog = new ProgressDialog(TambahJadwalActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            String url = new RequestServer().getServer_url()+"tambahJadwalPengajar";
            Log.d("Register Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());
            jsonReq.addProperty("mapel_id", mapel_id);
            jsonReq.addProperty("hari", hari);
            jsonReq.addProperty("waktu_mulai", waktu_mulai);
            jsonReq.addProperty("waktu_selesai", waktu_selesai);
            Log.d("Request",">"+jsonReq);

                Ion.with(TambahJadwalActivity.this)
                        .load(url)
                        //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                        .setJsonObjectBody(jsonReq)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try{
                                    String status = result.get("status").toString();
                                    if (status.equals("1")){
                                        Intent i = new Intent(TambahJadwalActivity.this, KetersediaanJadwalActivity.class);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Gagal Menyimpan data. Silahkan coba lagi.", Toast.LENGTH_LONG).show();
                                    }
                                    pDialog.dismiss();
                                }catch (Exception ex){
                                    Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                    pDialog.dismiss();
                                }
                            }
                        });
            }else{
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
