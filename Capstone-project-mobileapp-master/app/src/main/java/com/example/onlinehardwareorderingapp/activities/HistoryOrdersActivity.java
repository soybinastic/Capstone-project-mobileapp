package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.adapters.OrderAdapter;
import com.example.onlinehardwareorderingapp.fragments.bottomsheets.OrderItemsBottomSheet;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.OrderService;
import com.example.onlinehardwareorderingapp.models.OrderItemModel;
import com.example.onlinehardwareorderingapp.models.OrderModel;
import com.example.onlinehardwareorderingapp.models.ui.OrderAttributes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryOrdersActivity extends AppCompatActivity implements Invoker<OrderAttributes, String> {
    private LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    private RetrofitService retrofitService = RetrofitService.getInstance();
    private Retrofit retrofit;
    private UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    private RecyclerView orderRecyclerView;
    private String authToken = "";
    private ArrayList<OrderModel> orders;
    OrderService orderService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.order_toolbar);
        toolbar.setTitle("My Orders");
        setSupportActionBar(toolbar);
        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
        authToken = sharedPreferences.getString(Keys.AUTH_TOKEN, "");
        orderRecyclerView = (RecyclerView) findViewById(R.id.ordersRecyclerView);
        initRetrofit();
        loadingAlertDialog.showDialog();

        loadOrders();
    }

    void initRetrofit(){
        retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderService = retrofit.create(OrderService.class);
    }
    void initOrderRecyclerView(){
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderRecyclerView.setAdapter(new OrderAdapter(orders, orderRecyclerView, this));
    }
    void loadOrders(){
        Call<ArrayList<OrderModel>> result = orderService.getAll(headers());
        result.enqueue(new Callback<ArrayList<OrderModel>>() {
            @Override
            public void onResponse(Call<ArrayList<OrderModel>> call, Response<ArrayList<OrderModel>> response) {
                if (response.isSuccessful()){
                    orders = response.body();
                    initOrderRecyclerView();
                }else {
                    Toast.makeText(HistoryOrdersActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArrayList<OrderModel>> call, Throwable t) {
                Toast.makeText(HistoryOrdersActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                loadingAlertDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.essential_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private Map<String, String> headers(){
        Map<String, String> headers = new HashMap<>();
        headers.put(Keys.CONTENT_TYPE, "application/json");
        headers.put(Keys.AUTHORIZATION, "Bearer " + authToken);
        return headers;
    }

    @Override
    public void onClick(OrderAttributes data, String s) {
        OrderAttributes order = (OrderAttributes) data;
        Intent intent = new Intent(HistoryOrdersActivity.this, OrderHistoryDetailsActivity.class);
        intent.putExtra("orderId", data.getOrderModel().getOrderId());
        intent.putExtra("branchId", data.getOrderModel().getBrancId());
        intent.putExtra("hardwareStoreId", data.getOrderModel().getHardwareStoreId());
        intent.putExtra("total", data.getOrderModel().getTotal());
        intent.putExtra("orderDate", data.getOrderModel().getOrderDate());
        intent.putExtra("status", data.getOrderModel().getStatus());
        intent.putExtra("branchName", data.getOrderModel().getBranchName());
        startActivity(intent);
        //Toast.makeText(this, "StoreId : " + order.getHardwareStoreId() + " BranchID: " + order.getBranchId(), Toast.LENGTH_SHORT).show();
//        loadingAlertDialog.showDialog();
//        orderService.getOrderProducts(order.getHardwareStoreId(),
//                order.getBranchId(), order.getOrderId(), headers())
//                .enqueue(new Callback<ArrayList<OrderItemModel>>() {
//                    @Override
//                    public void onResponse(Call<ArrayList<OrderItemModel>> call, Response<ArrayList<OrderItemModel>> response) {
//                        if (response.isSuccessful()){
//                            OrderItemsBottomSheet orderItemsBottomSheet = new OrderItemsBottomSheet(response.body(), order.getBranchName(),
//                                    order.getTotal());
//                            orderItemsBottomSheet.show(getSupportFragmentManager(), orderItemsBottomSheet.getTag());
//                            Toast.makeText(HistoryOrdersActivity.this, String.valueOf(response.body().size()), Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(HistoryOrdersActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                        }
//                        loadingAlertDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Call<ArrayList<OrderItemModel>> call, Throwable t) {
//                        loadingAlertDialog.dismiss();
//                    }
//                });
    }
}