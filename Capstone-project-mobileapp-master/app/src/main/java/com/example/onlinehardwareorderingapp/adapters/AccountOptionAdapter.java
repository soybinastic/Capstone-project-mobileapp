package com.example.onlinehardwareorderingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.helper.AccountOption;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;

import java.util.ArrayList;

public class AccountOptionAdapter extends RecyclerView.Adapter<AccountOptionAdapter.AccountOptionHolder> {
    private ArrayList<AccountOption> options;
    private RecyclerView recyclerView;
    private Invoker<AccountOption, String> callback;
    public AccountOptionAdapter(ArrayList<AccountOption> options, RecyclerView recyclerView, Invoker<AccountOption, String> callback){
        this.options = options;
        this.recyclerView = recyclerView;
        this.callback = callback;
    }
    @NonNull
    @Override
    public AccountOptionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_option, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildLayoutPosition(view);
                callback.onClick(options.get(position), "");
            }
        });
        return new AccountOptionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountOptionHolder holder, int position) {
        holder.optionName.setText(options.get(position).getTitle());
        if (options.get(position).getDrawableIcon() > 0){
            holder.icon.setImageResource(options.get(position).getDrawableIcon());
        }
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class AccountOptionHolder extends RecyclerView.ViewHolder{
        TextView optionName;
        ImageView icon;
        public AccountOptionHolder(@NonNull View itemView) {
            super(itemView);
            optionName = (TextView) itemView.findViewById(R.id.optionNameTextview);
            icon = (ImageView) itemView.findViewById(R.id.imageView3);

        }
    }
}
