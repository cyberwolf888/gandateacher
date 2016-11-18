package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class VerifyAlamatActivity extends AppCompatActivity {
    private Button buttonReg;
    private EditText teacherAddress,teacherKodePos;
    private ProgressDialog pDialog;

    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(VerifyAlamatActivity.this);
        setContentView(R.layout.activity_verify_alamat);

        teacherAddress = (EditText) findViewById(R.id.teacherAddress);
        teacherKodePos = (EditText) findViewById(R.id.teacherKodePos);
        buttonReg = (Button) findViewById(R.id.buttonReg);
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptVerify();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void attemptVerify() {
        teacherAddress.setError(null);
        teacherKodePos.setError(null);

        String alamat = teacherAddress.getText().toString();
        String kodepos = teacherKodePos.getText().toString();

        boolean cancel = false;
        View focusView = null;

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

        if (cancel) {
            focusView.requestFocus();
        }else {
            if(isNetworkAvailable()){
                pDialog = new ProgressDialog(VerifyAlamatActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                String url = new RequestServer().getServer_url()+"verifyalamat";
                Log.d("Register Url",">"+url);

                JsonObject jsonReq = new JsonObject();
                jsonReq.addProperty("alamat", alamat);
                jsonReq.addProperty("kodepos", kodepos);
                jsonReq.addProperty("user_id", session.getUserId());
                Log.d("Request",">"+jsonReq);

                Ion.with(VerifyAlamatActivity.this)
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
                                        Toast.makeText(getApplicationContext(), "Verifikasi alamat berhasil.", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(VerifyAlamatActivity.this, MainActivity.class);
                                        startActivity(i);
                                    }else{
                                        Toast.makeText(getApplicationContext(), result.get("error").getAsString(), Toast.LENGTH_LONG).show();
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
