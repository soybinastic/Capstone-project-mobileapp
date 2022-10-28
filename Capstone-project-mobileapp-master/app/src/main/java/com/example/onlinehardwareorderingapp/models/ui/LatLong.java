package com.example.onlinehardwareorderingapp.models.ui;

import com.example.onlinehardwareorderingapp.interfaces.BaseModel;

public class LatLong implements BaseModel {
    private double lat;
    private double lng;
    public LatLong(){}
    public LatLong(double lat, double lng){
        this.lng = lng;
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
