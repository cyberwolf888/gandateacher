package com.gandaedukasi.gandateacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gandaedukasi.gandateacher.DetailJadwalActivity;
import com.gandaedukasi.gandateacher.DetailSubmitActivity;
import com.gandaedukasi.gandateacher.R;
import com.gandaedukasi.gandateacher.models.Jadwal;
import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Karen on 8/20/2016.
 */

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder> {

    public class JadwalViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView studentName;
        TextView studentLecture;
        TextView tgl_pertemuan;
        TextView waktu_pertemuan;
        TextView tempat_pertemuan;
        TextView pertemuan;
        ImageView imageStudent;

        public JadwalViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvJadwal);
            studentName = (TextView)itemView.findViewById(R.id.studentName);
            studentLecture = (TextView)itemView.findViewById(R.id.studentLecture);
            tgl_pertemuan = (TextView)itemView.findViewById(R.id.tgl_pertemuan);
            waktu_pertemuan = (TextView)itemView.findViewById(R.id.waktu_pertemuan);
            tempat_pertemuan = (TextView)itemView.findViewById(R.id.tempat_pertemuan);
            pertemuan = (TextView)itemView.findViewById(R.id.pertemuan);
            imageStudent = (ImageView)itemView.findViewById(R.id.imageStudent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Jadwal feedItem = listItems.get(getAdapterPosition());
                    final int position = getAdapterPosition();
                    Intent i = new Intent(mContext, DetailJadwalActivity.class);
                    i.putExtra("jadwal_id",feedItem.jadwal_id);
                    i.putExtra("detail_jadwal_id",feedItem.detail_jadwal_id);
                    i.putExtra("nama_siswa",feedItem.nama_siswa);
                    i.putExtra("no_telp",feedItem.no_telp);
                    i.putExtra("photo",feedItem.photo);
                    i.putExtra("label_mapel",feedItem.label_mapel);
                    i.putExtra("label_tanggal",feedItem.label_tanggal);
                    i.putExtra("label_waktu",feedItem.label_waktu);
                    i.putExtra("label_tempat",feedItem.label_tempat);
                    i.putExtra("pertemuan",feedItem.pertemuan);
                    mContext.startActivity(i);
                }
            });
        }

    }

    List<Jadwal> listItems;
    private Context mContext;

    public JadwalAdapter(Context context, List<Jadwal> listItems){
        this.listItems = listItems;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public JadwalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_jadwal, viewGroup, false);
        JadwalViewHolder rjvh = new JadwalViewHolder(v);
        return rjvh;
    }

    @Override
    public void onBindViewHolder(JadwalViewHolder jadwalViewHolder, int i) {
        jadwalViewHolder.studentName.setText(listItems.get(i).nama_siswa);
        jadwalViewHolder.studentLecture.setText(listItems.get(i).label_mapel);
        jadwalViewHolder.tgl_pertemuan.setText(listItems.get(i).label_tanggal);
        jadwalViewHolder.waktu_pertemuan.setText(listItems.get(i).label_waktu);
        jadwalViewHolder.tempat_pertemuan.setText(listItems.get(i).label_tempat);
        jadwalViewHolder.pertemuan.setText("Pertemuan ke "+listItems.get(i).pertemuan);

        if(listItems.get(i).photo.equals("")){
            jadwalViewHolder.imageStudent.setImageResource(R.drawable.guest);
        }else{
            Ion.with(mContext)
                    .load(new RequestServer().getPhotoUrl()+"siswa/"+listItems.get(i).photo)
                    .withBitmap()
                    .placeholder(R.drawable.guest)
                    .error(R.drawable.guest)
                    .intoImageView(jadwalViewHolder.imageStudent);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
