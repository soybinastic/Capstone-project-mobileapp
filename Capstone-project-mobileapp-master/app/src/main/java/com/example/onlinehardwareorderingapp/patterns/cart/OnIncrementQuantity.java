package com.example.onlinehardwareorderingapp.patterns.cart;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CartService;
import com.example.onlinehardwareorderingapp.models.CartItemModel;
import com.example.onlinehardwareorderingapp.models.ui.ExecutedCartProcessResult;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OnIncrementQuantity implements ICartProcessor{
    private CartItemModel cartItemModel;
    public OnIncrementQuantity(){

    }
    public OnIncrementQuantity(CartItemModel cartItemModel){
        this.cartItemModel = cartItemModel;
    }

    @Override
    public void onProcess(CartService cartService, Context context, Invoker<BaseModel, String> listener,
                          LoadingAlertDialog loadingAlertDialog, Map<String, String> headers) {
        Toast.makeText(context, cartItemModel.getProductName() + " +", Toast.LENGTH_SHORT).show();
        loadingAlertDialog.showDialog();
        Call<ServerResponse> result = cartService.incrementQuantity(cartItemModel.getHardwareStoreId(), cartItemModel.getCartId(),
                cartItemModel.getProductId(), cartItemModel.getBranchId(), headers);

        result.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()){
                    listener.onClick(new ExecutedCartProcessResult(true, 1, cartItemModel.getProductPrice(),
                            cartItemModel.getProductName(), OnIncrementQuantity.this), "");
                }else {
                    listener.onClick(new ExecutedCartProcessResult(false), "");
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                loadingAlertDialog.dismiss();
                listener.onClick(new ExecutedCartProcessResult(false), "");
            }
        });
    }

    @Override
    public void onRefreshDetails(ArrayList<CartItemModel> cartItems, double total, int adjustedQty) {
        int index = cartItems.indexOf(this.cartItemModel);
        //total = 0;
        cartItems.get(index).setProductQuantity(cartItems.get(index).getProductQuantity() + adjustedQty);
//        for (CartItemModel ci : cartItems){
//            total += (ci.getProductPrice() * ci.getProductQuantity());
//        }
    }
}
