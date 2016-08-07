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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    Session session;
    TextView teacherName,teacherEmail,teacherPhone,teacherEdukasi,teacherAddress,teacherTingkatPendidikan,teacherMapel,teacherZona;
    ImageView teacherPhoto;
    String id_tingkat_pendidikan, id_mapel, photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(ProfileActivity.this);
        setContentView(R.layout.activity_profile);

        teacherName = (TextView) findViewById(R.id.teacherName);
        teacherEmail = (TextView) findViewById(R.id.teacherEmail);
        teacherPhone = (TextView) findViewById(R.id.teacherPhone);
        teacherEdukasi = (TextView) findViewById(R.id.teacherEdukasi);
        teacherAddress = (TextView) findViewById(R.id.teacherAddress);
        teacherTingkatPendidikan = (TextView) findViewById(R.id.teacherTingkatPendidikan);
        teacherMapel = (TextView) findViewById(R.id.teacherMapel);
        teacherZona = (TextView) findViewById(R.id.teacherZona);

        teacherPhoto = (ImageView) findViewById(R.id.teacherPhoto);

        ImageView btnTry;

        btnTry = (ImageView) findViewById(R.id.btnEdit);

        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = teacherName.getText().toString();
                String email = teacherEmail.getText().toString();
                String phone = teacherPhone.getText().toString();
                String pendidikan = teacherEdukasi.getText().toString();
                String alamat = teacherAddress.getText().toString();
                String label_tingkat_pendidikan = teacherTingkatPendidikan.getText().toString();
                String label_mapel = teacherMapel.getText().toString();
                String zona = teacherZona.getText().toString();

                Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
                i.putExtra("name", name);
                i.putExtra("email", email);
                i.putExtra("phone", phone);
                i.putExtra("pendidikan", pendidikan);
                i.putExtra("alamat", alamat);
                i.putExtra("label_tingkat_pendidikan", label_tingkat_pendidikan);
                i.putExtra("label_mapel", label_mapel);
                i.putExtra("id_tingkat_pendidikan", id_tingkat_pendidikan);
                i.putExtra("id_mapel", id_mapel);
                i.putExtra("photo", photo);
                i.putExtra("zona", zona);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileActivity.this, MainActivity.class);
        ComponentName cn = i.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(mainIntent);
    }

    public void onResume(){
        super.onResume();
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

            String url = new RequestServer().getServer_url()+"getProfile/pengajar";
            Log.d("Test Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());

            Ion.with(ProfileActivity.this)
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
                                    JsonObject data = result.getAsJsonObject("data");
                                    teacherName.setText(data.get("fullname").getAsString());
                                    teacherEmail.setText(data.get("email").getAsString());
                                    teacherPhone.setText(data.get("pengajar_cp").getAsString());
                                    teacherEdukasi.setText(data.get("pengajar_pendidikan").getAsString());
                                    teacherAddress.setText(data.get("pengajar_alamat").getAsString());
                                    teacherTingkatPendidikan.setText(data.get("label_tingkat_pendidikan").getAsString());
                                    teacherMapel.setText(data.get("label_mapel").getAsString());
                                    teacherZona.setText(data.get("zona").getAsString());
                                    id_tingkat_pendidikan = data.get("id_tingkat_pendidikan").getAsString();
                                    id_mapel = data.get("id_mapel").getAsString();
                                    if(data.get("photo").isJsonNull()){
                                        photo = "";
                                    }else{
                                        photo = data.get("photo").getAsString();
                                    }
                                    String url_photo = new RequestServer().getPhotoUrl()+"/pengajar/"+photo;
                                    Ion.with(teacherPhoto).load(url_photo);
                                }else{
                                    //TODO jika status 0
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
