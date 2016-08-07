package com.gandaedukasi.gandateacher.utility;

/**
 * Created by Karen on 7/30/2016.
 */

public class RequestServer {
    private String server_url = "http://gandaedukasi.esy.es/android/";
    private String images_url = "http://gandaedukasi.esy.es/images/";
    public String getServer_url(){
        return this.server_url;
    }
    public String getPhotoUrl(){
        return this.images_url+"photo/";
    }

}
