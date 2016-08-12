package com.gandaedukasi.gandateacher.models;

/**
 * Created by Karen on 8/11/2016.
 */

public class KetersedianJadwal {
    public String jadwal_id;
    public String mapel_id;
    public String zona_id;
    public String label_cabang;
    public String label_mapel;
    public String waktu_mulai;
    public String waktu_selesai;
    public String hari;
    public String status;

    public KetersedianJadwal(String jadwal_id, String mapel_id, String zona_id, String label_cabang, String label_mapel, String waktu_mulai, String waktu_selesai, String hari, String status){
        this.jadwal_id = jadwal_id;
        this.mapel_id = mapel_id;
        this.zona_id = zona_id;
        this.label_cabang = label_cabang;
        this.label_mapel = label_mapel;
        this.waktu_mulai = waktu_mulai;
        this.waktu_selesai = waktu_selesai;
        this.hari = hari;
        this.status = status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
