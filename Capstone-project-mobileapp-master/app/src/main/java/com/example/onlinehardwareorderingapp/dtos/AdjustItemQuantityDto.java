package com.example.onlinehardwareorderingapp.dtos;

public class AdjustItemQuantityDto {
    private int quantity;

    public AdjustItemQuantityDto(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
