package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {
    Session session;
    ProgressDialog pDialog;
    ImageView btnRequestMengajar, btnSubmitPertemuan, btnJadwalLes, btnLihatPertemuan, btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
        OneSignal.startInit(this).init();
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("Onesignal debug", "User:" + userId);
                if (registrationId != null)
                    Log.d("Onesignal debug", "registrationId:" + registrationId);
            }
        });
        session = new Session(MainActivity.this);
        if(!session.isLoggedIn()){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        cekProfile();

        setContentView(R.layout.activity_main);

        btnRequestMengajar = (ImageView)findViewById(R.id.btnRequestMengajar);
        btnSubmitPertemuan = (ImageView)findViewById(R.id.btnSubmitPertemuan);
        btnJadwalLes = (ImageView)findViewById(R.id.btnJadwalLes);
        btnLihatPertemuan = (ImageView)findViewById(R.id.btnLihatPertemuan);
        btnProfile = (ImageView)findViewById(R.id.btnProfile);

        btnRequestMengajar.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tile_request_mengajar, 150, 150));
        btnSubmitPertemuan.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tile_submit_pertemuan, 150, 150));
        btnJadwalLes.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tile_jadwal_les, 150, 150));
        btnLihatPertemuan.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tile_lihat_pertemuan, 150, 150));
        btnProfile.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.tile_profil, 150, 150));

        btnRequestMengajar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RequestActivity.class);
                startActivity(i);
            }
        });

        btnSubmitPertemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SubmitActivity.class);
                startActivity(i);
            }
        });

        btnJadwalLes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, JadwalActivity.class);
                startActivity(i);
            }
        });

        btnLihatPertemuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PertemuanActivity.class);
                startActivity(i);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

    }

    private void cekProfile(){
        if(isNetworkAvailable()){
            pDialog = new ProgressDialog(MainActivity.this);
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

            String url = new RequestServer().getServer_url()+"cekProfilePengajar";
            Log.d("Cek Url",">"+url);

            JsonObject jsonReq = new JsonObject();
            jsonReq.addProperty("user_id", session.getUserId());
            Log.d("Cek Req",">"+jsonReq);

            Ion.with(MainActivity.this)
                    .load(url)
                    //.setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
                    .setJsonObjectBody(jsonReq)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try{
                                Log.d("Cek Req",">"+result);
                                String status = result.get("status").getAsString();
                                if (status.equals("1")){
                                    String isNewAccount = result.get("isNewAccount").getAsString();
                                    Log.d("Cek isNewAccount",">"+isNewAccount);
                                    if (isNewAccount.equals("1")){
                                        //Intent i = new Intent(MainActivity.this, TingkatPendidikanActivity.class);
                                        Intent i = new Intent(MainActivity.this, PilihCabangActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }else if(status.equals("2")){
                                    Intent i = new Intent(MainActivity.this, VerifyAlamatActivity.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    //TODO jika status 0
                                }
                            } catch (Exception ex){
                                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                            }
                            pDialog.dismiss();
                        }
                    });

        }else {
            Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
            finish();
        }

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_contact) {
            Intent i = new Intent(MainActivity.this,ContactActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_logout) {
            String url = new RequestServer().getServer_url()+"deleteNotif";
            Ion.with(MainActivity.this)
                    .load(url)
                    .setMultipartParameter("user_id", session.getUserId())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                        }
                    });
            session.logoutUser();
        }
        return super.onOptionsItemSelected(item);
    }
}
