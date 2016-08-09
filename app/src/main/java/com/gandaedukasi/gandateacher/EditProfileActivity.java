package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;

public class EditProfileActivity extends AppCompatActivity {
    Button btnSave;
    TextView teacherTingkatPendidikan, teacherMapel, teacherZone;
    String name, email, phone, address, pendidikan, tingkat_pendidikan, mapel, zona, photo, id_tingkat_pendidikan, id_mapel;
    EditText teacherName, editEmail, teacherPhone, teacherEdu, teacherAddress;
    ImageView teacherPhoto;
    String imagePath;
    ProgressDialog pDialog;
    Session session;
    private final static int SELECT_PHOTO = 12345;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(EditProfileActivity.this);
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");
        pendidikan = getIntent().getStringExtra("pendidikan");
        address = getIntent().getStringExtra("alamat");
        tingkat_pendidikan = getIntent().getStringExtra("label_tingkat_pendidikan");
        zona = getIntent().getStringExtra("zona");
        photo = getIntent().getStringExtra("photo");
        id_tingkat_pendidikan = getIntent().getStringExtra("id_tingkat_pendidikan");
        id_mapel = getIntent().getStringExtra("id_mapel");
        mapel = getIntent().getStringExtra("label_mapel");

        setContentView(R.layout.activity_edit_profile);

        teacherPhoto = (ImageView) findViewById(R.id.teacherPhoto);

        teacherName = (EditText) findViewById(R.id.teacherName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        teacherPhone = (EditText) findViewById(R.id.teacherPhone);
        teacherEdu = (EditText) findViewById(R.id.teacherEdu);
        teacherAddress = (EditText) findViewById(R.id.teacherAddress);

        teacherTingkatPendidikan = (TextView) findViewById(R.id.teacherTingkatPendidikan);
        teacherMapel = (TextView) findViewById(R.id.teacherMapel);
        teacherZone = (TextView) findViewById(R.id.teacherZone);

        btnSave = (Button) findViewById(R.id.btnSave);

        teacherName.setText(name);
        editEmail.setText(email);
        teacherPhone.setText(phone);
        teacherEdu.setText(pendidikan);
        teacherAddress.setText(address);
        teacherTingkatPendidikan.setText(tingkat_pendidikan);
        teacherMapel.setText(mapel);
        teacherZone.setText(zona);

        if(!TextUtils.isEmpty(photo)){
            String url_photo = new RequestServer().getPhotoUrl()+"/pengajar/"+photo;
            Ion.with(teacherPhoto).load(url_photo);
            Log.d("Url Photo",url_photo);
        }

        teacherPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                /*Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();*/
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(EditProfileActivity.this, MainActivity.class);
        ComponentName cn = i.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(mainIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            //Cek file size
            File file = new File(imagePath);
            int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
            Log.d("File Size",">"+file_size);
            if(file_size>(1.5*1024)){
                //TODO jika gambar terlalu besar
                imagePath = "";
                Toast.makeText(getApplicationContext(), "Ukuran gambar terlalu besar. Ukuran file maksimal 1.5 MB", Toast.LENGTH_LONG).show();
            }else{
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                teacherPhoto.setImageBitmap(bitmap);
            }

            cursor.close();
        }
    }

    private void updateProfile(){
        teacherName.setError(null);
        editEmail.setError(null);
        teacherPhone.setError(null);
        teacherEdu.setError(null);
        teacherAddress.setError(null);

        String nama_lengkap = teacherName.getText().toString();
        String email = editEmail.getText().toString();
        String telp = teacherPhone.getText().toString();
        String pendidikan = teacherEdu.getText().toString();
        String alamat = teacherAddress.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //validasi alamat
        if (TextUtils.isEmpty(alamat)) {
            teacherAddress.setError(getString(R.string.id_error_alamat_empty));
            focusView = teacherAddress;
            cancel = true;
        }

        //validasi edukasi
        if (TextUtils.isEmpty(pendidikan)) {
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
            String url = new RequestServer().getServer_url()+"editProfilePengajar";
            Log.d("Test Url",">"+url);

            if(isNetworkAvailable()){
                pDialog = new ProgressDialog(EditProfileActivity.this);
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();

                if(TextUtils.isEmpty(imagePath)){
                    Ion.with(EditProfileActivity.this)
                            .load(url)
                            .setMultipartParameter("user_id", session.getUserId())
                            .setMultipartParameter("name", nama_lengkap)
                            .setMultipartParameter("email", email)
                            .setMultipartParameter("phone", telp)
                            .setMultipartParameter("pendidikan", pendidikan)
                            .setMultipartParameter("alamat", alamat)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    Log.d("Response",">"+result);
                                    try{
                                        String status = result.get("status").toString();
                                        if (status.equals("1")){
                                            Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else{
                                            //TODO jika status 0
                                            Toast.makeText(getApplicationContext(), "Gagal menyipan data", Toast.LENGTH_LONG).show();
                                        }
                                    }catch (Exception ex){
                                        Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                    }
                                    pDialog.dismiss();
                                }
                            });
                }else{
                    Ion.with(EditProfileActivity.this)
                            .load(url)
                            .setMultipartParameter("user_id", session.getUserId())
                            .setMultipartParameter("name", nama_lengkap)
                            .setMultipartParameter("email", email)
                            .setMultipartParameter("phone", telp)
                            .setMultipartParameter("pendidikan", pendidikan)
                            .setMultipartParameter("alamat", alamat)
                            .setMultipartFile("photo", "application/images", new File(imagePath))
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    Log.d("Response",">"+result);
                                    try{
                                        String status = result.get("status").toString();
                                        if (status.equals("1")){
                                            Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else{
                                            //TODO jika status 0
                                            Toast.makeText(getApplicationContext(), "Gagal menyipan data", Toast.LENGTH_LONG).show();
                                        }
                                    }catch (Exception ex){
                                        Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                    }
                                    pDialog.dismiss();
                                }
                            });
                }

            }else{
                Toast.makeText(getApplicationContext(), getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
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

    private boolean isNamaValid(String nama){
        return nama.length() > 4;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
}
