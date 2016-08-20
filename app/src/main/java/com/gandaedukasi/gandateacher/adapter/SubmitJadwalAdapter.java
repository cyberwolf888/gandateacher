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

import com.gandaedukasi.gandateacher.DetailSubmitActivity;
import com.gandaedukasi.gandateacher.R;
import com.gandaedukasi.gandateacher.models.RequestJadwal;
import com.gandaedukasi.gandateacher.models.SubmitJadwal;
import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Karen on 8/20/2016.
 */

public class SubmitJadwalAdapter extends RecyclerView.Adapter<SubmitJadwalAdapter.SubmitJadwalViewHolder> {

    public class SubmitJadwalViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView studentName;
        TextView studentLecture;
        TextView tgl_pertemuan;
        TextView waktu_pertemuan;
        TextView tempat_pertemuan;
        ImageView imageStudent;

        public SubmitJadwalViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvSubmitJadwal);
            studentName = (TextView)itemView.findViewById(R.id.studentName);
            studentLecture = (TextView)itemView.findViewById(R.id.studentLecture);
            tgl_pertemuan = (TextView)itemView.findViewById(R.id.tgl_pertemuan);
            waktu_pertemuan = (TextView)itemView.findViewById(R.id.waktu_pertemuan);
            tempat_pertemuan = (TextView)itemView.findViewById(R.id.tempat_pertemuan);
            imageStudent = (ImageView)itemView.findViewById(R.id.imageStudent);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SubmitJadwal feedItem = listItems.get(getAdapterPosition());
                    final int position = getAdapterPosition();
                    Intent i = new Intent(mContext, DetailSubmitActivity.class);
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

    List<SubmitJadwal> listItems;
    private Context mContext;

    public SubmitJadwalAdapter(Context context, List<SubmitJadwal> listItems){
        this.listItems = listItems;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public SubmitJadwalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_submit_jadwal, viewGroup, false);
        SubmitJadwalViewHolder rjvh = new SubmitJadwalViewHolder(v);
        return rjvh;
    }

    @Override
    public void onBindViewHolder(SubmitJadwalViewHolder submitJadwalViewHolder, int i) {
        submitJadwalViewHolder.studentName.setText(listItems.get(i).nama_siswa);
        submitJadwalViewHolder.studentLecture.setText(listItems.get(i).label_mapel);
        submitJadwalViewHolder.tgl_pertemuan.setText(listItems.get(i).label_tanggal);
        submitJadwalViewHolder.waktu_pertemuan.setText(listItems.get(i).label_waktu);
        submitJadwalViewHolder.tempat_pertemuan.setText(listItems.get(i).label_tempat);

        if(listItems.get(i).photo.equals("")){
            submitJadwalViewHolder.imageStudent.setImageResource(R.drawable.guest);
        }else{
            Ion.with(mContext)
                    .load(new RequestServer().getPhotoUrl()+"siswa/"+listItems.get(i).photo)
                    .withBitmap()
                    .placeholder(R.drawable.guest)
                    .error(R.drawable.guest)
                    .intoImageView(submitJadwalViewHolder.imageStudent);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
