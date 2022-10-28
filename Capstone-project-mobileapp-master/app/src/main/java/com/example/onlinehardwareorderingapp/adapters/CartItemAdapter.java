package com.example.onlinehardwareorderingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.helper.MoneyFormat;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.models.CartItemModel;
import com.example.onlinehardwareorderingapp.patterns.cart.CartProcessor;
import com.example.onlinehardwareorderingapp.patterns.cart.OnDecrementQuantity;
import com.example.onlinehardwareorderingapp.patterns.cart.OnIncrementQuantity;
import com.example.onlinehardwareorderingapp.patterns.cart.OnRemoveItem;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemHolder>{

    private ArrayList<CartItemModel> cartItems;
    private RecyclerView recyclerView;
    private Invoker<BaseModel, String> listener;

    public CartItemAdapter(ArrayList<CartItemModel> cartItems, RecyclerView recyclerView, Invoker<BaseModel, String> listener){
        this.cartItems = cartItems;
        this.recyclerView = recyclerView;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemHolder holder, int position) {
        holder.productName.setText(cartItems.get(position).getProductName());
        holder.productQuantity.setText(String.valueOf(cartItems.get(position).getProductQuantity()));
        holder.productPrice.setText(MoneyFormat.getFormat(cartItems.get(position).getProductPrice()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartItemHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName, productPrice, removeTextView_as_Btn;
        EditText productQuantity;
        ConstraintLayout decrementQty, incrementQty;
        public CartItemHolder(@NonNull View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.cart_item_image);
            productName = (TextView) itemView.findViewById(R.id.cart_item_name);
            productPrice = (TextView) itemView.findViewById(R.id.cart_item_price);
            productQuantity = (EditText) itemView.findViewById(R.id.editText_cart_item_qty);
            decrementQty = (ConstraintLayout) itemView.findViewById(R.id.decrement_item_qty_btn);
            incrementQty = (ConstraintLayout) itemView.findViewById(R.id.increment_item_qty_btn);
            removeTextView_as_Btn = (TextView) itemView.findViewById(R.id.cart_item_remove);

            decrementQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentQuantity = Integer.parseInt(productQuantity.getText().toString());
                    if (currentQuantity > 1){
                        listener.onClick(new CartProcessor(new OnDecrementQuantity(cartItems.get(getAdapterPosition()))), "");
                    }
                }
            });

            incrementQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(new CartProcessor(new OnIncrementQuantity(cartItems.get(getAdapterPosition()))), "");
                }
            });

            removeTextView_as_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(new CartProcessor(new OnRemoveItem(cartItems.get(getAdapterPosition()))), "");
                }
            });
        }


    }
}
