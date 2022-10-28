package com.example.onlinehardwareorderingapp.models.ui;

import com.example.onlinehardwareorderingapp.interfaces.BaseModel;

public class Branch implements BaseModel {
    private int id;
    private int hardwareStoreId;
    private String name;
    private String address;

    public Branch(int id, String name, String address, int hardwareStoreId){
        this.id = id;
        this.name = name;
        this.address = address;
        this.hardwareStoreId = hardwareStoreId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHardwareStoreId() {
        return hardwareStoreId;
    }

    public void setHardwareStoreId(int hardwareStoreId) {
        this.hardwareStoreId = hardwareStoreId;
    }
}
