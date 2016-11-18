package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner teacherZone;
    Button buttonReg, btnTambahPrestasi, btnDelPrestasi;
    EditText teacherName,editEmail,editPassword,teacherPhone,teacherEdu,teacherAddress,teacherKodePos;

    ProgressDialog pDialog;
    List<String> listZone = new ArrayList<String>();
    ArrayAdapter<String> dataAdapter;
    JsonArray mData = new JsonArray();

    int jumlah_prestasi = 0;
    private LinearLayout layoutPrestasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //teacherZone = (Spinner) findViewById(R.id.teacherZone);
        //teacherZone.setOnItemSelectedListener(this);

        //dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listZone);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //teacherZone.setAdapter(dataAdapter);

        teacherName = (EditText) findViewById(R.id.teacherName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        teacherPhone = (EditText) findViewById(R.id.teacherPhone);
        teacherEdu = (EditText) findViewById(R.id.teacherEdu);
        teacherAddress = (EditText) findViewById(R.id.teacherAddress);
        teacherKodePos = (EditText) findViewById(R.id.teacherKodePos);

        layoutPrestasi = (LinearLayout) findViewById(R.id.layoutPrestasi);

        buttonReg = (Button) findViewById(R.id.buttonReg);
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        btnTambahPrestasi = (Button) findViewById(R.id.btnTambahPrestasi);
        btnDelPrestasi = (Button) findViewById(R.id.btnDelPrestasi);

        btnTambahPrestasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jumlah_prestasi<10){
                    jumlah_prestasi++;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    params.setMargins(0, 0, 0, 3);
                    TextInputLayout til = new TextInputLayout(RegisterActivity.this);
                    til.setId(10+jumlah_prestasi);
                    til.setLayoutParams(params);
                    til.setHint("Prestasi "+String.valueOf(jumlah_prestasi));
                    layoutPrestasi.addView(til);

                    EditText etPrestasi = new EditText(RegisterActivity.this);
                    etPrestasi.setLayoutParams(params);
                    etPrestasi.setId(40+jumlah_prestasi);
                    etPrestasi.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    etPrestasi.setFocusable(true);
                    til.addView(etPrestasi);

                    if(jumlah_prestasi>0){
                        btnDelPrestasi.setVisibility(View.VISIBLE);
                    }else{
                        btnDelPrestasi.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Jumlah prestasi maksimal 10", Toast.LENGTH_LONG).show();
                }


            }
        });

        btnDelPrestasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutPrestasi.removeViewAt(jumlah_prestasi-1);
                Log.d("test",">"+jumlah_prestasi);

                jumlah_prestasi--;

                if(jumlah_prestasi>0){
                    btnDelPrestasi.setVisibility(View.VISIBLE);
                }else{
                    btnDelPrestasi.setVisibility(View.GONE);
                }

            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        /*
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        String url = new RequestServer().getServer_url()+"cabang";
        Log.d("Login Url",">"+url);

        JsonObject jsonReq = new JsonObject();
        jsonReq.addProperty("register", true);

        if(isNetworkAvailable()){
            Ion.with(RegisterActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                String status = result.get("status").toString();
                                //Log.d("Status",">"+status);
                                if (status.equals("1")){
                                    JsonArray data = result.getAsJsonArray("data");
                                    //Log.d("Response",">"+data);
                                    for (int i=0; i<data.size(); i++){
                                        JsonObject objData = data.get(i).getAsJsonObject();
                                        listZone.add(objData.get("nama").getAsString());
                                    }
                                    dataAdapter.notifyDataSetChanged();
                                }else{
                                    Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                }
                            }catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
            pDialog.dismiss();
        }
        */
    }

    private void attemptRegister() {
        teacherName.setError(null);
        editEmail.setError(null);
        editPassword.setError(null);
        teacherPhone.setError(null);
        teacherEdu.setError(null);
        teacherAddress.setError(null);
        teacherKodePos.setError(null);

        String nama_lengkap = teacherName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String telp = teacherPhone.getText().toString();
        String edukasi = teacherEdu.getText().toString();
        String alamat = teacherAddress.getText().toString();
        String kodepos = teacherKodePos.getText().toString();
        //String zona = teacherZone.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        JsonObject jsonReq = new JsonObject();
        Log.d("jumlah_prestasi ",">"+jumlah_prestasi);
        if(jumlah_prestasi>0){
            for (int i=1; i<=jumlah_prestasi; i++){
                EditText et = (EditText) findViewById(40+i);
                Log.d("Prestasi "+i,">"+et.getText().toString());
                et.setError(null);
                if (TextUtils.isEmpty(et.getText().toString())) {
                    et.setError("Prestasi tidak boleh kosong!");
                    focusView = et;
                    cancel = true;
                }
                jsonReq.addProperty("prestasi"+i, et.getText().toString());
                Log.d("Request",">"+jsonReq);
            }
        }

        //validasi kodepos
        if (TextUtils.isEmpty(kodepos)) {
            teacherKodePos.setError(getString(R.string.id_error_kodepos));
            focusView = teacherKodePos;
            cancel = true;
        }

        //validasi alamat
        if (TextUtils.isEmpty(alamat)) {
            teacherAddress.setError(getString(R.string.id_error_alamat_empty));
            focusView = teacherAddress;
            cancel = true;
        }

        //validasi edukasi
        if (TextUtils.isEmpty(edukasi)) {
            teacherEdu.setError(getString(R.string.id_error_edu_empty));
            focusView = teacherEdu;
            cancel = true;
        }

        //validasi telp
        if (TextUtils.isEmpty(telp)) {
            teacherPhone.setError(getString(R.string.id_error_telp_empty));
            focusView = teacherPhone;
            cancel = true;
        }

        //validasi password
        if (TextUtils.isEmpty(password)) {
            editPassword.setError(getString(R.string.id_error_password_empty));
            focusView = editPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            editPassword.setError(getString(R.string.id_error_passowrd_length));
            focusView = editPassword;
            cancel = true;
        }

        //validasi email
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.id_error_email_empty));
            focusView = editEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editEmail.setError(getString(R.string.id_error_email_invalid));
            focusView = editEmail;
            cancel = true;
        }

        //validasi nama lengkap
        if (TextUtils.isEmpty(nama_lengkap)) {
            teacherName.setError(getString(R.string.id_error_nama_empty));
            focusView = teacherName;
            cancel = true;
        } else if (!isNamaValid(nama_lengkap)) {
            teacherName.setError(getString(R.string.id_error_nama_invalid));
            focusView = teacherName;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }else {
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            String url = new RequestServer().getServer_url()+"register/pengajar";
            Log.d("Register Url",">"+url);

            jsonReq.addProperty("jumlah_prestasi", jumlah_prestasi);
            jsonReq.addProperty("nama", nama_lengkap);
            jsonReq.addProperty("email", email);
            jsonReq.addProperty("password", password);
            jsonReq.addProperty("telp", telp);
            jsonReq.addProperty("edukasi", edukasi);
            jsonReq.addProperty("alamat", alamat);
            jsonReq.addProperty("kodepos", kodepos);
            //jsonReq.addProperty("zona", zona);
            Log.d("Request",">"+jsonReq);

            if(isNetworkAvailable()){
                Ion.with(RegisterActivity.this)
                        .load(url)
                        //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                        .setJsonObjectBody(jsonReq)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try{
                                    Log.d("Response",">"+result);
                                    String status = result.get("status").toString();
                                    if (status.equals("1")){
                                        Toast.makeText(getApplicationContext(), getString(R.string.id_success_register), Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(getApplicationContext(), result.get("error").getAsString(), Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception ex){
                                    Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                }
                                pDialog.dismiss();
                            }
                        });
            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }

    private boolean isNamaValid(String nama){
        return nama.length() > 4;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

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
