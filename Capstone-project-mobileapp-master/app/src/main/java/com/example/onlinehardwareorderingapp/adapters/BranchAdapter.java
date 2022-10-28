package com.example.onlinehardwareorderingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.models.BranchModel;

import java.util.ArrayList;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder>{
    private ArrayList<BranchModel> branches;
    private RecyclerView recyclerView;
    private Invoker<BranchModel, String> clickListener;
    public BranchAdapter(){}
    public BranchAdapter(ArrayList<BranchModel> branches, RecyclerView recyclerView, Invoker<BranchModel, String> clickListener){
        this.branches = branches;
        this.recyclerView = recyclerView;
        this.clickListener = clickListener;
    }

    public BranchAdapter(ArrayList<BranchModel> branches){
        this.branches = branches;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_store, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildLayoutPosition(view);
                //Toast.makeText(parent.getContext(), branches.get(position).getName(), Toast.LENGTH_LONG).show();
                clickListener.onClick(branches.get(position), "");
            }
        });
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        holder.branchName.setText(branches.get(position).getName());
        holder.branchAddress.setText(branches.get(position).getAddress());
        holder.distance.setText("Distance: " + String.format("%.2f", branches.get(position).getDistance()) + "km");
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    public class BranchViewHolder extends RecyclerView.ViewHolder {
        ImageView storeImage;
        TextView branchName, branchAddress, distance;
        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            storeImage = itemView.findViewById(R.id.storeImage);
            branchName = itemView.findViewById(R.id.storeNameTxt);
            branchAddress = itemView.findViewById(R.id.storeAddressTxt);
            distance = itemView.findViewById(R.id.distanceTxt);
        }
    }
}
