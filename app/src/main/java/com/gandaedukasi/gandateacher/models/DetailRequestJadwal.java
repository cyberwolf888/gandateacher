package com.gandaedukasi.gandateacher.models;

/**
 * Created by Karen on 8/18/2016.
 */

public class DetailRequestJadwal {
    public String detail_id, label_mapel, label_tgl, label_waktu, label_tempat;

    public DetailRequestJadwal(String detail_id, String label_mapel, String label_tgl, String label_waktu, String label_tempat ){
        this.detail_id = detail_id;
        this.label_mapel = label_mapel;
        this.label_tgl = label_tgl;
        this.label_waktu = label_waktu;
        this.label_tempat = label_tempat;
    }
}
