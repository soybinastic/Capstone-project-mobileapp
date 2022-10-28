package com.example.onlinehardwareorderingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.models.CategoryModel;
import com.example.onlinehardwareorderingapp.models.ui.Category;

import java.util.ArrayList;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.CategoryHolder>{
    private ArrayList<CategoryModel> categories;
    private Invoker<BaseModel, String> selectListener;
    private RecyclerView recyclerView;
    public ProductCategoryAdapter(ArrayList<CategoryModel> categories, Invoker<BaseModel, String> selectListener, RecyclerView recyclerView){
        this.categories = categories;
        this.selectListener = selectListener;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_category, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildLayoutPosition(view);
                selectListener.onClick(new Category(categories.get(position)), "");
            }
        });
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.categoryNameTxt.setText(categories.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {

        TextView categoryNameTxt;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTxt = (TextView) itemView.findViewById(R.id.productCategoryNameTxt);
        }
    }
}
