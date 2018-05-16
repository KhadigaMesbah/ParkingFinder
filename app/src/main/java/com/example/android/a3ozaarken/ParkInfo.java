package com.example.android.a3ozaarken;

/**
 * Created by Lenovo on 4/19/2018.
 */

public class ParkInfo {

    private String places;
    private int status,id;

    public ParkInfo (String places, int status,int id){

        this.setPlaces(places);
        this.setId(id);
        this.setStatus(status);
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
