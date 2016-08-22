package com.gandaedukasi.gandateacher;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.gandaedukasi.gandateacher.utility.Session;
import com.koushikdutta.ion.Ion;

public class DetailJadwalActivity extends AppCompatActivity {
    private String jadwal_id, detail_jadwal_id, nama_siswa, no_telp, photo, label_mapel, label_tanggal, label_waktu, label_tempat, pertemuan;
    private TextView studentName, studentSchool, studentPertemuan, studentLecture, studentDate, studentTime, studentPlace;
    private ImageView studentPhoto;
    private Button btnSubmit;
    protected ProgressDialog pDialog;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(DetailJadwalActivity.this);
        jadwal_id = getIntent().getStringExtra("jadwal_id");
        detail_jadwal_id = getIntent().getStringExtra("detail_jadwal_id");
        nama_siswa = getIntent().getStringExtra("nama_siswa");
        no_telp = getIntent().getStringExtra("no_telp");
        photo = getIntent().getStringExtra("photo");
        label_mapel = getIntent().getStringExtra("label_mapel");
        label_tanggal = getIntent().getStringExtra("label_tanggal");
        label_waktu = getIntent().getStringExtra("label_waktu");
        label_tempat = getIntent().getStringExtra("label_tempat");
        pertemuan = getIntent().getStringExtra("pertemuan");

        setContentView(R.layout.activity_detail_jadwal);

        studentName = (TextView) findViewById(R.id.studentName);
        studentSchool = (TextView) findViewById(R.id.studentSchool);
        studentPertemuan = (TextView) findViewById(R.id.studentPertemuan);
        studentLecture = (TextView) findViewById(R.id.studentLecture);
        studentDate = (TextView) findViewById(R.id.studentDate);
        studentTime = (TextView) findViewById(R.id.studentTime);
        studentPlace = (TextView) findViewById(R.id.studentPlace);
        studentPhoto = (ImageView) findViewById(R.id.studentPhoto);

        studentName.setText(nama_siswa);
        studentSchool.setText(no_telp);
        studentPertemuan.setText(pertemuan);
        studentLecture.setText(label_mapel);
        studentDate.setText(label_tanggal);
        studentTime.setText(label_waktu);
        studentPlace.setText(label_tempat);
        if(photo.equals("")){
            studentPhoto.setImageResource(R.drawable.guest);
        }else{
            Ion.with(DetailJadwalActivity.this)
                    .load(new RequestServer().getPhotoUrl()+"siswa/"+photo)
                    .withBitmap()
                    .placeholder(R.drawable.guest)
                    .error(R.drawable.guest)
                    .intoImageView(studentPhoto);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
