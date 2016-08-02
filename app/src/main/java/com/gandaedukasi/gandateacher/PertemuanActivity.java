package com.gandaedukasi.gandateacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PertemuanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertemuan);

        Button btnTry;

        btnTry = (Button)findViewById(R.id.btnTryPertemuan);

        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PertemuanActivity.this, DetailJadwalActivity.class);
                startActivity(i);
            }
        });
    }
}
