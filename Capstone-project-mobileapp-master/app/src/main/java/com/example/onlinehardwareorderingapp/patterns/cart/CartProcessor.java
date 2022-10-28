package com.example.onlinehardwareorderingapp.patterns.cart;

import android.content.Context;

import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CartService;

import java.util.Map;

public class CartProcessor implements BaseModel {
    private ICartProcessor cartProcessor;

    public CartProcessor(ICartProcessor cartProcessor){
        this.cartProcessor = cartProcessor;
    }
    public void onProcess(Context ctx, CartService cartService, Invoker<BaseModel, String> listener,
                          LoadingAlertDialog loadingAlertDialog, Map<String, String> headers){
        cartProcessor.onProcess(cartService, ctx, listener, loadingAlertDialog, headers);
    }
}
