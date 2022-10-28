package com.example.onlinehardwareorderingapp.patterns.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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

public class OnRemoveItem implements ICartProcessor{
    private CartItemModel cartItemModel;
    private AlertDialog alertDialog;
    public OnRemoveItem(){}
    public OnRemoveItem(CartItemModel cartItemModel){
        this.cartItemModel = cartItemModel;
    }
    @Override
    public void onProcess(CartService cartService, Context context, Invoker<BaseModel, String> listener,
                          LoadingAlertDialog loadingAlertDialog, Map<String, String> headers) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setMessage("You are removing an item (" + cartItemModel.getProductName()
            + ") with quantity of " + cartItemModel.getProductQuantity());
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                loadingAlertDialog.showDialog();
                Call<ServerResponse> result = cartService.remove(cartItemModel.getHardwareStoreId(), cartItemModel.getCartId(),
                        cartItemModel.getProductId(), cartItemModel.getBranchId(), headers);
                result.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.isSuccessful()){
                            listener.onClick(new ExecutedCartProcessResult(true, 0, cartItemModel.getProductPrice(),
                                    cartItemModel.getProductName(), OnRemoveItem.this), "");
                        }else {
                            Toast.makeText(context, "Failed to remove item", Toast.LENGTH_LONG).show();
                            listener.onClick(new ExecutedCartProcessResult(false), "");
                        }
                        loadingAlertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        loadingAlertDialog.dismiss();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        listener.onClick(new ExecutedCartProcessResult(false), "");
                    }
                });
                //Toast.makeText(context, "YES", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //Toast.makeText(context, "CANCEL", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog = builder.create();
        alertDialog.setTitle("Confirmation");
        alertDialog.show();

    }

    @Override
    public void onRefreshDetails(ArrayList<CartItemModel> cartItems, double total, int adjustedQty) {
        cartItems.remove(cartItemModel);
    }
}
