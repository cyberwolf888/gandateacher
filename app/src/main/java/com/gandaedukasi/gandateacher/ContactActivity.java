package com.gandaedukasi.gandateacher;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ContactActivity.this, MainActivity.class);
        ComponentName cn = i.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(mainIntent);
    }
}
