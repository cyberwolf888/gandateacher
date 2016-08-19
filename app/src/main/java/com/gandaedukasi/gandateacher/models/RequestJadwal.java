package com.gandaedukasi.gandateacher.models;

/**
 * Created by Karen on 8/18/2016.
 */

public class RequestJadwal {
    public String jadwal_id, siswa_id, mapel_id, nama_siswa, label_mapel, photo;

    public RequestJadwal(String jadwal_id, String siswa_id, String mapel_id, String nama_siswa, String label_mapel, String photo){
        this.jadwal_id = jadwal_id;
        this.siswa_id = siswa_id;
        this.mapel_id = mapel_id;
        this.nama_siswa = nama_siswa;
        this.label_mapel = label_mapel;
        this.photo = photo;
    }
}
