package com.gandaedukasi.gandateacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailSubmitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_submit);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
