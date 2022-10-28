package com.example.onlinehardwareorderingapp.dtos.order;

public class UpdateOrderDto {
    private int branchId;
    private int hardwareStoreId;
    private boolean isCancelled;
    private int customerId;
    private int orderId;

    public UpdateOrderDto(){}
    public UpdateOrderDto(int branchId, int hardwareStoreId,
                          int customerId, int orderId,
                          boolean isCancelled){
        this.branchId = branchId;
        this.hardwareStoreId = hardwareStoreId;
        this.customerId = customerId;
        this.orderId = orderId;
        this.isCancelled = isCancelled;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getHardwareStoreId() {
        return hardwareStoreId;
    }

    public void setHardwareStoreId(int hardwareStoreId) {
        this.hardwareStoreId = hardwareStoreId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
