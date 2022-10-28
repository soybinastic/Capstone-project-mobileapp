package com.example.onlinehardwareorderingapp.models.ui;

import com.example.onlinehardwareorderingapp.interfaces.BaseModel;

public class Search implements BaseModel {
    private double adjustedKm;

    public Search(double adjustedKm){
        this.adjustedKm = adjustedKm;
    }
    public Search(){}
    public double getAdjustedKm() {
        return adjustedKm;
    }

    public void setAdjustedKm(double adjustedKm) {
        this.adjustedKm = adjustedKm;
    }
}
