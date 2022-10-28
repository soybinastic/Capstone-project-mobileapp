package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.dtos.order.UpdateOrderDto;
import com.example.onlinehardwareorderingapp.fragments.bottomsheets.OrderItemsBottomSheet;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.MoneyFormat;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CustomerService;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.OrderService;
import com.example.onlinehardwareorderingapp.models.CustomerModel;
import com.example.onlinehardwareorderingapp.models.OrderItemModel;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class OrderHistoryDetailsActivity extends AppCompatActivity {
    private int orderId, branchId, hardwareStoreId;
    private String status, orderDate, branchName;
    private double total;

    private LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    private RetrofitService retrofitService = RetrofitService.getInstance();
    private Retrofit retrofit;
    private UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    OrderService orderService;
    private String authToken = "";

    Button cancelOrder, seeOrderedProducts;
    TextView _total, _status, _branchName, _orderDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_details);
        Toolbar toolbar = findViewById(R.id.order_details_history_toolbar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", 0);
        branchId = intent.getIntExtra("branchId", 0);
        hardwareStoreId = intent.getIntExtra("hardwareStoreId", 0);
        status = intent.getStringExtra("status");
        orderDate = intent.getStringExtra("orderDate");
        total = intent.getDoubleExtra("total", 0);
        branchName = intent.getStringExtra("branchName");

        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
        authToken = sharedPreferences.getString(Keys.AUTH_TOKEN, "");
        initControls();
        setValueToControls();
        initRetrofit();
        //loadOrderProducts();
        loadCustomerInfo();
        seeOrderedProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadOrderProducts();
            }
        });
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderCancelConfirmation();
            }
        });
    }
    private void onCancelOrder(){
        loadingAlertDialog.showDialog();
        orderService.cancelOrder(orderId, new UpdateOrderDto(branchId, hardwareStoreId, customerModel.getCustomerId(),orderId, true ), headers())
                .enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(OrderHistoryDetailsActivity.this, "The order successfully cancelled", Toast.LENGTH_SHORT).show();
                            cancelOrder.setVisibility(View.GONE);
                        }else {
                            try {
                                Toast.makeText(OrderHistoryDetailsActivity.this, response.errorBody().string().toString(), Toast.LENGTH_SHORT).show();
                            }catch (Exception ex){

                            }
                        }
                        loadingAlertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        loadingAlertDialog.dismiss();
                    }
                });
    }
    private void orderCancelConfirmation(){

        AlertDialog alert = new AlertDialog.Builder(this)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        onCancelOrder();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setMessage("Are you sure you want to cancel this order?")
                .create();
        alert.setTitle("Confirmation");
        alert.show();
    }

    private void setValueToControls(){
        _branchName.setText(branchName);
        _status.setText(status);
        _orderDate.setText(date());
        _total.setText(MoneyFormat.getFormat(total));
    }
    private String total(){
        return String.format("%,.2f", total);
    }

    private String date(){
        String dateInput = orderDate.replace(" ", "T");
        String[]stringArr = dateInput.split("-");
        String y = stringArr[0];
        String m = stringArr[1];
        String strD = new String(stringArr[2]);
        String d = strD.substring(0,2);
        String time = strD.substring(3, 8);
        String orderDate = y + "/" + m + "/" + d + " " + time;
        return orderDate;
    }

    private void  initControls(){
        cancelOrder = findViewById(R.id.cancel_order_btn);
        seeOrderedProducts = findViewById(R.id.see_order_products_btn);
        _total = findViewById(R.id.order_details_total_amount);
        _status = findViewById(R.id.order_details_status);
        _orderDate = findViewById(R.id.order_details_order_date);
        _branchName = findViewById(R.id.order_details_branch_name);

        cancelOrder.setVisibility(status.equals("Pending") ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.essential_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    void initRetrofit(){
        retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderService = retrofit.create(OrderService.class);
    }

    private Map<String, String> headers(){
        Map<String, String> headers = new HashMap<>();
        headers.put(Keys.CONTENT_TYPE, "application/json");
        headers.put(Keys.AUTHORIZATION, "Bearer " + authToken);
        return headers;
    }

    private void loadOrderProducts(){
        loadingAlertDialog.showDialog();
        orderService.getOrderProducts(hardwareStoreId,
                branchId, orderId, headers())
                .enqueue(new Callback<ArrayList<OrderItemModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<OrderItemModel>> call, Response<ArrayList<OrderItemModel>> response) {
                        if (response.isSuccessful()){
                            OrderItemsBottomSheet orderItemsBottomSheet = new OrderItemsBottomSheet(response.body(), branchName,
                                    total);
                            orderItemsBottomSheet.show(getSupportFragmentManager(), orderItemsBottomSheet.getTag());
                            //Toast.makeText(HistoryOrdersActivity.this, String.valueOf(response.body().size()), Toast.LENGTH_SHORT).show();
                        }else {
                            //Toast.makeText(HistoryOrdersActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        }
                        loadingAlertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<OrderItemModel>> call, Throwable t) {
                        loadingAlertDialog.dismiss();
                    }
                });
    }
    private CustomerModel customerModel;
    private void loadCustomerInfo() {
        loadingAlertDialog.showDialog();
        CustomerService customerService = retrofit.create(CustomerService.class);
        customerService.getInformation(headers())
                .enqueue(new Callback<CustomerModel>() {
                    @Override
                    public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                        if (response.isSuccessful()){
                            customerModel = response.body();
                            //Toast.makeText(OrderHistoryDetailsActivity.this, "" + customerModel.getCustomerId(), Toast.LENGTH_SHORT).show();
                        }
                        loadingAlertDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<CustomerModel> call, Throwable t) {
                        loadingAlertDialog.dismiss();
                    }
                });
    }
}