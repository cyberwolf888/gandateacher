package com.gandaedukasi.gandateacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.DetailRequestActivity;
import com.gandaedukasi.gandateacher.R;
import com.gandaedukasi.gandateacher.models.RequestJadwal;
import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Karen on 8/18/2016.
 */

public class RequestJadwalAdapter extends RecyclerView.Adapter<RequestJadwalAdapter.RequestJadwalViewHolder> {

    public class RequestJadwalViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView studentName;
        TextView studentLecture;
        ImageView imageStudent;
        //Button btnAccept;
        //Button btnIgnore;
        public RequestJadwalViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvRequestJadwal);
            studentName = (TextView)itemView.findViewById(R.id.studentName);
            studentLecture = (TextView)itemView.findViewById(R.id.studentLecture);
            imageStudent = (ImageView)itemView.findViewById(R.id.imageStudent);
            //btnAccept = (Button)itemView.findViewById(R.id.btnAccept);
            //btnIgnore = (Button)itemView.findViewById(R.id.btnIgnore);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final RequestJadwal feedItem = listItems.get(getAdapterPosition());
                    final int position = getAdapterPosition();
                    Intent i = new Intent(mContext, DetailRequestActivity.class);
                    i.putExtra("jadwal_id",feedItem.jadwal_id);
                    i.putExtra("siswa_id",feedItem.siswa_id);
                    i.putExtra("mapel_id",feedItem.mapel_id);
                    mContext.startActivity(i);
                }
            });
        }
    }

    List<RequestJadwal> listItems;
    private Context mContext;

    public RequestJadwalAdapter(Context context, List<RequestJadwal> listItems){
        this.listItems = listItems;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RequestJadwalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_request_jadwal, viewGroup, false);
        RequestJadwalViewHolder rjvh = new RequestJadwalViewHolder(v);
        return rjvh;
    }

    @Override
    public void onBindViewHolder(RequestJadwalViewHolder requestJadwalViewHolder, int i) {
        requestJadwalViewHolder.studentName.setText(listItems.get(i).nama_siswa);
        requestJadwalViewHolder.studentLecture.setText(listItems.get(i).label_mapel);

        if(listItems.get(i).photo.equals("")){
            requestJadwalViewHolder.imageStudent.setImageResource(R.drawable.guest);
        }else{
            Ion.with(mContext)
                    .load(new RequestServer().getPhotoUrl()+"siswa/"+listItems.get(i).photo)
                    .withBitmap()
                    .placeholder(R.drawable.guest)
                    .error(R.drawable.guest)
                    .intoImageView(requestJadwalViewHolder.imageStudent);
        }
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
