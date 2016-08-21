package com.gandaedukasi.gandateacher.models;

/**
 * Created by Karen on 8/20/2016.
 */

public class Jadwal {
    public String jadwal_id, detail_jadwal_id, nama_siswa, no_telp, photo, label_mapel, label_tanggal, label_waktu, label_tempat, pertemuan;

    public Jadwal(String jadwal_id, String detail_jadwal_id, String nama_siswa, String no_telp, String photo, String label_mapel, String label_tanggal, String label_waktu, String label_tempat, String pertemuan){
        this.jadwal_id = jadwal_id;
        this.detail_jadwal_id = detail_jadwal_id;
        this.nama_siswa = nama_siswa;
        this.no_telp = no_telp;
        this.photo = photo;
        this.label_mapel = label_mapel;
        this.label_tanggal = label_tanggal;
        this.label_waktu = label_waktu;
        this.label_tempat = label_tempat;
        this.pertemuan = pertemuan;
    }
}
