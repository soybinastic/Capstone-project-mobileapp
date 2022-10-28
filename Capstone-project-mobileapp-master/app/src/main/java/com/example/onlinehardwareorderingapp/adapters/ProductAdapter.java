package com.example.onlinehardwareorderingapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.helper.MoneyFormat;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.models.ProductModel;
import com.example.onlinehardwareorderingapp.models.ui.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private ArrayList<ProductModel> products;
    private Invoker<BaseModel, String> onViewListener;
    private RecyclerView recyclerView;
    private Context context;
    public ProductAdapter(ArrayList<ProductModel> products, Invoker<BaseModel, String> onViewListener, RecyclerView recyclerView){
        this.products = products;
        this.onViewListener = onViewListener;
        this.recyclerView = recyclerView;
    }
    public ProductAdapter(ArrayList<ProductModel> products, Invoker<BaseModel,
            String> onViewListener, RecyclerView recyclerView, Context context){
        this.products = products;
        this.onViewListener = onViewListener;
        this.recyclerView = recyclerView;
        this.context = context;
    }
    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildLayoutPosition(view);
                onViewListener.onClick(new Product(products.get(position)), "");
            }
        });
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.productName.setText(products.get(position).getName());
        holder.productPrice.setText(MoneyFormat.getFormat(products.get(position).getPrice()));
        Glide.with(context).load(Uri.parse(RetrofitService.API_URL + products.get(position).getImageFile()))
                .fitCenter().into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.product_name_txt);
            productPrice = (TextView) itemView.findViewById(R.id.product_price_txt);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
        }
    }
}
