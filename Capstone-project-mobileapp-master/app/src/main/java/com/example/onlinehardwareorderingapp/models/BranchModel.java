package com.example.onlinehardwareorderingapp.models;

public class BranchModel {
    private int id;
    private int hardwareStoreId;
    private String name;
    private String address;
    private boolean isActive;
    private double latitude;
    private double longitude;
    private double distance;

    public BranchModel(int id, String name, String address){
        this.id = id;
        this.name = name;
        this.address = address;
    }
    public BranchModel(int id, String name, String address, int hardwareStoreId, boolean isActive){
        this.id = id;
        this.name = name;
        this.address = address;
        this.hardwareStoreId = hardwareStoreId;
        this.isActive = isActive;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
