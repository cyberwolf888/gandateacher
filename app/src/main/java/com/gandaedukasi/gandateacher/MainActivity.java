package com.gandaedukasi.gandateacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView btnRequestMengajar, btnSubmitPertemuan, btnJadwalLes, btnLihatPertemuan, btnProfile;

        btnRequestMengajar = (ImageView)findViewById(R.id.btnRequestMengajar);
        btnSubmitPertemuan = (ImageView)findViewById(R.id.btnSubmitPertemuan);
        btnJadwalLes = (ImageView)findViewById(R.id.btnJadwalLes);
        btnLihatPertemuan = (ImageView)findViewById(R.id.btnLihatPertemuan);
        btnProfile = (ImageView)findViewById(R.id.btnProfile);

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
}
