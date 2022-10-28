package com.example.onlinehardwareorderingapp.models;

public class OrderModel {
    private int orderId;
    private int customerId;
    private int hardwareStoreId;
    private String orderDate;
    private double total;
    private boolean deliver;
    private boolean isRecieved;
    private String branchName;
    private int brancId;
    private String status;

    public OrderModel(){}

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getHardwareStoreId() {
        return hardwareStoreId;
    }

    public void setHardwareStoreId(int hardwareStoreId) {
        this.hardwareStoreId = hardwareStoreId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isDeliver() {
        return deliver;
    }

    public void setDeliver(boolean deliver) {
        this.deliver = deliver;
    }

    public boolean isRecieved() {
        return isRecieved;
    }

    public void setRecieved(boolean recieved) {
        isRecieved = recieved;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getBrancId() {
        return brancId;
    }

    public void setBrancId(int branchId) {
        this.brancId = branchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
