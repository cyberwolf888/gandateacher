package com.gandaedukasi.gandateacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {
    Button btnSave;
    TextView teacherTingkatPendidikan, teacherMapel;
    String name, email, phone, address, pendidikan, tingkat_pendidikan, mapel, zona, photo, id_tingkat_pendidikan, id_mapel;
    EditText teacherName, editEmail, teacherPhone, teacherEdu, teacherAddress;
    Spinner Zone;
    ImageView teacherPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        teacherName = (EditText) findViewById(R.id.teacherName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        teacherPhone = (EditText) findViewById(R.id.teacherPhone);
        teacherEdu = (EditText) findViewById(R.id.teacherEdu);
        teacherAddress = (EditText) findViewById(R.id.teacherAddress);

        teacherTingkatPendidikan = (TextView) findViewById(R.id.teacherTingkatPendidikan);
        teacherMapel = (TextView) findViewById(R.id.teacherMapel);

        btnSave = (Button) findViewById(R.id.btnSave);

        teacherName.setText(name);
        editEmail.setText(email);
        teacherPhone.setText(phone);
        teacherEdu.setText(pendidikan);
        teacherAddress.setText(address);
        teacherTingkatPendidikan.setText(tingkat_pendidikan);
        teacherMapel.setText(mapel);

        teacherTingkatPendidikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "teacher tingkat pendidikan", Toast.LENGTH_LONG).show();
            }
        });
        teacherMapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "teacher mapel", Toast.LENGTH_LONG).show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
