package com.example.onlinehardwareorderingapp.models;

import java.util.ArrayList;

public class CartDetailsModel {
    private double shippingFee;
    private ArrayList<CartItemModel> cartItems;

    public double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public ArrayList<CartItemModel> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItemModel> cartItems) {
        this.cartItems = cartItems;
    }
}
