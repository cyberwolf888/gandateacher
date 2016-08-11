package com.gandaedukasi.gandateacher.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gandaedukasi.gandateacher.R;
import com.gandaedukasi.gandateacher.models.KetersedianJadwal;
import com.gandaedukasi.gandateacher.utility.RequestServer;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Karen on 8/11/2016.
 */

public class KetersedianJadwalAdapter extends RecyclerView.Adapter<KetersedianJadwalAdapter.KetersedianJadwalViewHolder> {

    public class KetersedianJadwalViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView tvMapel;
        TextView tvHari;
        TextView tvWaktuMulai;
        TextView tvWaktuSelesai;
        TextView tvTempat;
        ImageView btnDelete;

        public KetersedianJadwalViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvKetersedianJadwal);
            tvMapel = (TextView)itemView.findViewById(R.id.tvMapel);
            tvHari = (TextView)itemView.findViewById(R.id.tvHari);
            tvWaktuMulai = (TextView)itemView.findViewById(R.id.tvWaktuMulai);
            tvWaktuSelesai = (TextView)itemView.findViewById(R.id.tvWaktuSelesai);
            tvTempat = (TextView)itemView.findViewById(R.id.tvTempat);
            btnDelete = (ImageView)itemView.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final KetersedianJadwal feedItem = ketersedianJadwals.get(getAdapterPosition());
                    final int position = getAdapterPosition();
                    new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete")
                            .setMessage("Apakah anda yakin untuk menghapus jadwal ini?")
                            .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(mContext, "Item id " + feedItem.jadwal_id + " clicked.", Toast.LENGTH_LONG).show();
                                    final ProgressDialog pDialog = new ProgressDialog(mContext);
                                    pDialog.setMessage("Loading...");
                                    pDialog.setIndeterminate(false);
                                    pDialog.setCancelable(false);
                                    pDialog.show();

                                    String url = new RequestServer().getServer_url() + "deleteJadwalPengajar";

                                    JsonObject jsonReq = new JsonObject();
                                    jsonReq.addProperty("jadwal_id", feedItem.jadwal_id);

                                    Ion.with(mContext)
                                            .load(url)
                                            .setJsonObjectBody(jsonReq)
                                            .asJsonObject()
                                            .setCallback(new FutureCallback<JsonObject>() {
                                                @Override
                                                public void onCompleted(Exception e, JsonObject result) {
                                                    try {
                                                        String status = result.get("status").getAsString();
                                                        if (status.equals("1")) {
                                                            removeAt(position);
                                                            Toast.makeText(mContext, "Berhasil menghapus data!", Toast.LENGTH_LONG).show();
                                                            //Intent i = new Intent(mContext, KetersedianJadwal.class);
                                                            //mContext.startActivity(i);
                                                        }else{
                                                            Toast.makeText(mContext, "Gagal menghapus data!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }catch (Exception ex){
                                                        Toast.makeText(mContext, mContext.getString(R.string.id_error_network), Toast.LENGTH_LONG).show();
                                                    }
                                                    pDialog.dismiss();
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("Tidak", null)
                            .show();
                }
                public void removeAt(int position) {
                    ketersedianJadwals.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, ketersedianJadwals.size());
                }
            });
        }
    }

    List<KetersedianJadwal> ketersedianJadwals;
    private Context mContext;

    public KetersedianJadwalAdapter(Context context, List<KetersedianJadwal> ketersedianJadwals){
        this.ketersedianJadwals = ketersedianJadwals;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public KetersedianJadwalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_ketersedian_jadwal, viewGroup, false);
        KetersedianJadwalViewHolder kjvh = new KetersedianJadwalViewHolder(v);
        return kjvh;
    }

    @Override
    public void onBindViewHolder(KetersedianJadwalViewHolder ketersedianJadwalViewHolder, int i) {
        ketersedianJadwalViewHolder.tvMapel.setText(ketersedianJadwals.get(i).label_mapel);
        ketersedianJadwalViewHolder.tvHari.setText(ketersedianJadwals.get(i).hari);
        ketersedianJadwalViewHolder.tvWaktuMulai.setText(ketersedianJadwals.get(i).waktu_mulai);
        ketersedianJadwalViewHolder.tvWaktuSelesai.setText(ketersedianJadwals.get(i).waktu_selesai);
        ketersedianJadwalViewHolder.tvTempat.setText(ketersedianJadwals.get(i).label_cabang);
    }

    @Override
    public int getItemCount() {
        return ketersedianJadwals.size();
    }
}
