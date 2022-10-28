package com.example.onlinehardwareorderingapp.fragments.bottomsheets;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.adapters.OrderItemAdapter;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.MoneyFormat;
import com.example.onlinehardwareorderingapp.models.OrderItemModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderItemsBottomSheet extends BottomSheetDialogFragment {
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior<View> behavior;
    View view;
    private RecyclerView recyclerView;
    private TextView branchName, totalTv;
    //private Call<ArrayList<OrderItemModel>> result;
    private ArrayList<OrderItemModel> orderItems;
    //private LoadingAlertDialog loadingAlertDialog;
    private String branch_name;
    private double total;

    public OrderItemsBottomSheet(ArrayList<OrderItemModel> orderItems,
                                 String branchName, double total){
        this.orderItems = orderItems;
        //this.loadingAlertDialog = loadingAlertDialog;
        this.branch_name = branchName;
        this.total = total;
    }
    public OrderItemsBottomSheet(){}
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return bottomSheetDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_products_bottom_sheet, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.order_products_recycler_view);
        branchName = (TextView) view.findViewById(R.id.order_products_branch_name);
        totalTv = (TextView) view.findViewById(R.id.order_product_total);
        //androidx.core.view.ViewCompat.setNestedScrollingEnabled(recyclerView, false);

//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                view.getParent().requestDisallowInterceptTouchEvent(true);
//                view.onTouchEvent(motionEvent);
//                return false;
//            }
//        });
        initRecyclerView();
        //loadData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        behavior = BottomSheetBehavior.from((View)view.getParent());
        //behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) bottomSheetDialog.findViewById(R.id.order_product_bottom_sheet);
        assert coordinatorLayout != null;
        coordinatorLayout.setMinimumHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
    }
//    private void loadData(){
//        loadingAlertDialog.showDialog();
//        result.enqueue(new Callback<ArrayList<OrderItemModel>>() {
//            @Override
//            public void onResponse(Call<ArrayList<OrderItemModel>> call, Response<ArrayList<OrderItemModel>> response) {
//                if (response.isSuccessful()){
//                    orderItems = response.body();
//                    initRecyclerView();
//                }else {
//                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
//                }
//                loadingAlertDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<OrderItemModel>> call, Throwable t) {
//                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
//                loadingAlertDialog.dismiss();
//            }
//        });
//    }
    private void initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new OrderItemAdapter(orderItems));
        branchName.setText(branch_name);
        totalTv.setText("TOTAL: " + MoneyFormat.getFormat(total));
    }
}
