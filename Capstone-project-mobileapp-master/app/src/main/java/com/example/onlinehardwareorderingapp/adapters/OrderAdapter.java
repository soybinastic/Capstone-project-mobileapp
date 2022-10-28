package com.example.onlinehardwareorderingapp.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.helper.MoneyFormat;
import com.example.onlinehardwareorderingapp.helper.UserRule;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.models.OrderModel;
import com.example.onlinehardwareorderingapp.models.ui.OrderAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private RecyclerView recyclerView;
    private ArrayList<OrderModel> orders;
    private Invoker<OrderAttributes, String> listener;
    public OrderAdapter(ArrayList<OrderModel> orders, RecyclerView recyclerView,
                        Invoker<OrderAttributes, String> listener){
        this.orders = orders;
        this.recyclerView = recyclerView;
        this.listener = listener;
    }
    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = recyclerView.getChildLayoutPosition(view);
                //listener.onClick(new OrderAttributes(orders.get(position).getOrderId(),
                //        orders.get(position).getHardwareStoreId(), orders.get(position).getBrancId(),
                 //       orders.get(position).getBranchName(), orders.get(position).getTotal()), "");
                listener.onClick(new OrderAttributes(orders.get(position)), "");
            }
        });
        return new OrderHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        holder.orderDate.setText(orders.get(position).getOrderDate());
        holder.status.setText(orders.get(position).getStatus());
        holder.branch.setText(orders.get(position).getBranchName());
        holder.deliverOptions.setText((orders.get(position).isDeliver() ? "Deliver" : "Pick-up"));
        holder.total.setText(MoneyFormat.getFormat(orders.get(position).getTotal()));

        String dateInput = orders.get(position).getOrderDate().replace(" ", "T");
        String[]stringArr = dateInput.split("-");
        String y = stringArr[0];
        String m = stringArr[1];
        String strD = new String(stringArr[2]);
        String d = strD.substring(0,2);
        String time = strD.substring(3, 8);
        String orderDate = y + "/" + m + "/" + d + " " + time;
        holder.orderDate.setText(orderDate);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        TextView orderDate, status, branch, deliverOptions, total;
        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.order_date_tv);
            status = itemView.findViewById(R.id.order_status_tv);
            branch = itemView.findViewById(R.id.order_branch_tv);
            deliverOptions = itemView.findViewById(R.id.order_deliver_option_tv);
            total = itemView.findViewById(R.id.order_total);
        }
    }
}
