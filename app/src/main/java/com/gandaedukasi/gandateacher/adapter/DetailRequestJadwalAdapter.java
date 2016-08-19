package com.gandaedukasi.gandateacher.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gandaedukasi.gandateacher.R;
import com.gandaedukasi.gandateacher.models.DetailRequestJadwal;

import java.util.List;

/**
 * Created by Karen on 8/18/2016.
 */

public class DetailRequestJadwalAdapter extends RecyclerView.Adapter<DetailRequestJadwalAdapter.DetailRequestJadwalViewHolder> {

    public class DetailRequestJadwalViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView studentLecture;
        TextView studentDate;
        TextView studentTime;
        TextView studentPlace;

        public DetailRequestJadwalViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvDetailRequestJadwal);
            studentLecture = (TextView)itemView.findViewById(R.id.studentLecture);
            studentDate = (TextView)itemView.findViewById(R.id.studentDate);
            studentTime = (TextView)itemView.findViewById(R.id.studentTime);
            studentPlace = (TextView)itemView.findViewById(R.id.studentPlace);
        }
    }

    List<DetailRequestJadwal> listItems;
    private Context mContext;

    public DetailRequestJadwalAdapter(Context context, List<DetailRequestJadwal> listItems){
        this.listItems = listItems;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public DetailRequestJadwalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_detail_request, viewGroup, false);
        DetailRequestJadwalViewHolder rjvh = new DetailRequestJadwalViewHolder(v);
        return rjvh;
    }

    @Override
    public void onBindViewHolder(DetailRequestJadwalViewHolder detailRequestJadwalViewHolder, int i) {
        detailRequestJadwalViewHolder.studentLecture.setText(listItems.get(i).label_mapel);
        detailRequestJadwalViewHolder.studentDate.setText(listItems.get(i).label_tgl);
        detailRequestJadwalViewHolder.studentTime.setText(listItems.get(i).label_waktu);
        detailRequestJadwalViewHolder.studentPlace.setText(listItems.get(i).label_tempat);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
