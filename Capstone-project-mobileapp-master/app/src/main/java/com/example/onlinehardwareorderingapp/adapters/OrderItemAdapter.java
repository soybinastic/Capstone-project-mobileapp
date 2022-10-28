package com.example.onlinehardwareorderingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.helper.MoneyFormat;
import com.example.onlinehardwareorderingapp.models.OrderItemModel;

import java.util.ArrayList;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemHolder>{

    private ArrayList<OrderItemModel> orderItems;
    public OrderItemAdapter(ArrayList<OrderItemModel> orderItems){
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_products_row, parent, false);
        return new OrderItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position) {
        holder.name.setText(orderItems.get(position).getProductName());
        holder.qty.setText("QTY: " + orderItems.get(position).getProductQuantity());
        holder.price.setText(MoneyFormat.getFormat(orderItems.get(position).getProductPrice()));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class OrderItemHolder extends RecyclerView.ViewHolder{
        TextView name, qty, price;
        ImageView img;
        public OrderItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.order_product_name);
            qty = itemView.findViewById(R.id.order_product_qty);
            price = itemView.findViewById(R.id.order_product_price);
            img = itemView.findViewById(R.id.order_product_img);
        }
    }
}
