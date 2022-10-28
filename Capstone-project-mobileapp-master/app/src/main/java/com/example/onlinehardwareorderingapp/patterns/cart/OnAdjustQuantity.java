package com.example.onlinehardwareorderingapp.patterns.cart;

import android.content.Context;

import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CartService;
import com.example.onlinehardwareorderingapp.models.CartItemModel;

import java.util.ArrayList;
import java.util.Map;

public class OnAdjustQuantity implements ICartProcessor{
    private CartItemModel cartItemModel;
    public OnAdjustQuantity(){

    }
    public OnAdjustQuantity(CartItemModel cartItemModel){
        this.cartItemModel = cartItemModel;
    }
    @Override
    public void onProcess(CartService cartService, Context context, Invoker<BaseModel, String> listener
            , LoadingAlertDialog loadingAlertDialog, Map<String, String> headers) {

    }

    @Override
    public void onRefreshDetails(ArrayList<CartItemModel> cartItems, double total, int adjustedQty) {

    }
}
