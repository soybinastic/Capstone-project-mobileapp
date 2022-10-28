package com.example.onlinehardwareorderingapp.models.ui;

import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.models.CartItemModel;
import com.example.onlinehardwareorderingapp.patterns.cart.ICartProcessor;

public class ExecutedCartProcessResult implements BaseModel {
    private boolean isSuccess;
    private int adjustedQuantity;
    private double productPrice;
    private String productName;
    private ICartProcessor cartProcessor;

    public ExecutedCartProcessResult(boolean isSuccess, int adjustedQuantity, double productPrice, String productName){
        this.isSuccess = isSuccess;
        this.adjustedQuantity = adjustedQuantity;
        this.productName = productName;
        this.productPrice = productPrice;
    }
    public ExecutedCartProcessResult(boolean isSuccess, int adjustedQuantity, double productPrice,
                                     String productName, ICartProcessor cartProcessor){
        this.isSuccess = isSuccess;
        this.adjustedQuantity = adjustedQuantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.cartProcessor = cartProcessor;
    }
    public ExecutedCartProcessResult(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getAdjustedQuantity() {
        return adjustedQuantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public ICartProcessor getCartProcessor() {
        return cartProcessor;
    }
}
