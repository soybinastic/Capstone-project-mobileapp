package com.example.onlinehardwareorderingapp.models.ui;

import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.models.OrderModel;

public class OrderAttributes implements BaseModel {
    private int orderId;
    private int hardwareStoreId;
    private int branchId;
    private String branchName;
    private double total;
    private OrderModel orderModel;
    public OrderAttributes(OrderModel orderModel){
        this.orderModel = orderModel;
    }

    public OrderAttributes(int orderId, int hardwareStoreId, int branchId){
        this.orderId = orderId;
        this.hardwareStoreId = hardwareStoreId;
        this.branchId = branchId;
    }
    public OrderAttributes(int orderId, int hardwareStoreId, int branchId,
                           String branchName, double total){
        this.orderId = orderId;
        this.hardwareStoreId = hardwareStoreId;
        this.branchId = branchId;
        this.branchName = branchName;
        this.total = total;
    }
    public OrderAttributes(){}

    public int getOrderId() {
        return orderId;
    }

    public int getHardwareStoreId() {
        return hardwareStoreId;
    }

    public int getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public double getTotal() {
        return total;
    }

    public OrderModel getOrderModel() {
        return orderModel;
    }
}
