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

import com.gandaedukasi.gandateacher.DetailHistoryActivity;
import com.gandaedukasi.gandateacher.R;
import com.gandaedukasi.gandateacher.models.History;
import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Karen on 8/21/2016.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView studentName;
        TextView studentLecture;
        TextView tgl_pertemuan;
        TextView waktu_pertemuan;
        TextView tempat_pertemuan;
        TextView pertemuan;
        ImageView imageStudent;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvHistory);
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
                    final History feedItem = listItems.get(getAdapterPosition());
                    final int position = getAdapterPosition();
                    Intent i = new Intent(mContext, DetailHistoryActivity.class);
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
                    i.putExtra("keterangan",feedItem.keterangan);
                    i.putExtra("kelebihan_jam",feedItem.kelebihan_jam);
                    mContext.startActivity(i);
                }
            });
        }

    }

    List<History> listItems;
    private Context mContext;

    public HistoryAdapter(Context context, List<History> listItems){
        this.listItems = listItems;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_history, viewGroup, false);
        HistoryViewHolder rjvh = new HistoryViewHolder(v);
        return rjvh;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder historyViewHolder, int i) {
        historyViewHolder.studentName.setText(listItems.get(i).nama_siswa);
        historyViewHolder.studentLecture.setText(listItems.get(i).label_mapel);
        historyViewHolder.tgl_pertemuan.setText(listItems.get(i).label_tanggal);
        historyViewHolder.waktu_pertemuan.setText(listItems.get(i).label_waktu);
        historyViewHolder.tempat_pertemuan.setText(listItems.get(i).label_tempat);
        historyViewHolder.pertemuan.setText("Pertemuan ke "+listItems.get(i).pertemuan);
        if(listItems.get(i).photo.equals("")){
            historyViewHolder.imageStudent.setImageResource(R.drawable.guest);
        }else{
            Ion.with(mContext)
                    .load(new RequestServer().getPhotoUrl()+"siswa/"+listItems.get(i).photo)
                    .withBitmap()
                    .placeholder(R.drawable.guest)
                    .error(R.drawable.guest)
                    .intoImageView(historyViewHolder.imageStudent);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}