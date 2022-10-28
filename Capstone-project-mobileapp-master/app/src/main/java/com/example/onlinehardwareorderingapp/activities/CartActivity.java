package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.adapters.CartItemAdapter;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.MoneyFormat;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CartService;
import com.example.onlinehardwareorderingapp.models.CartDetailsModel;
import com.example.onlinehardwareorderingapp.models.CartItemModel;
import com.example.onlinehardwareorderingapp.models.ui.ExecutedCartProcessResult;
import com.example.onlinehardwareorderingapp.patterns.cart.CartProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity implements Invoker<BaseModel, String> {
    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    RetrofitService retrofitService = RetrofitService.getInstance();
    private String authToken = "";
    RecyclerView recyclerView;
    ArrayList<CartItemModel> cartItems = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    Retrofit retrofit;
    CartService cartService;
    int storeId = 0, branchId = 0;
    LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    TextView totalTextView;
    ConstraintLayout checkOutBtn;
    double total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = findViewById(R.id.cart_toolbar);
        toolbar.setTitle("Items in Cart");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        storeId = intent.getIntExtra("hardwareStoreId", 0);
        branchId = intent.getIntExtra("branchId", 0);

        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
        authToken = sharedPreferences.getString(Keys.AUTH_TOKEN, "");
        recyclerView = (RecyclerView) findViewById(R.id.cart_items_recyclerview);
        totalTextView = (TextView) findViewById(R.id.cart_item_total);
        ConstraintLayout keepShopping = (ConstraintLayout) findViewById(R.id.keep_shopping_btn);
        checkOutBtn = (ConstraintLayout) findViewById(R.id.checkout_btn);

        keepShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent checkOutActivity = new Intent(CartActivity.this, CheckOutActivity.class);
                checkOutActivity.putExtra("hardwareStoreId", storeId);
                checkOutActivity.putExtra("branchId", branchId);
                startActivity(checkOutActivity);
            }
        });
        layoutManager = new LinearLayoutManager(this);
        initRetrofit();
        loadCartItems();
//        ActionBar actionBar = getSupportActionBar();
//        ColorDrawable color = new ColorDrawable(Color.parseColor("#212121"));
//        //actionBar.hide();
//        actionBar.setBackgroundDrawable(color);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    void initCartRecyclerView(){
        total = 0;
        for (CartItemModel cartItemModel : cartItems){
            total += (cartItemModel.getProductPrice() * cartItemModel.getProductQuantity());
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CartItemAdapter(cartItems, recyclerView, this));
        totalTextView.setText(MoneyFormat.getFormat(total));
    }
    void initRetrofit(){
        retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        cartService = retrofit.create(CartService.class);
    }

    void loadCartItems(){
        loadingAlertDialog.showDialog();
        Call<CartDetailsModel> result = cartService.getItems(storeId, branchId, headers());
        result.enqueue(new Callback<CartDetailsModel>() {
            @Override
            public void onResponse(Call<CartDetailsModel> call, Response<CartDetailsModel> response) {
                if (response.isSuccessful()){
                    cartItems = response.body().getCartItems();
                    checkOutBtn.setEnabled(cartItems.size() > 0);
                    initCartRecyclerView();
                }else {
                    Toast.makeText(CartActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                }

                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CartDetailsModel> call, Throwable t) {
                loadingAlertDialog.dismiss();
                Toast.makeText(CartActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Map<String, String> headers(){
        Map<String, String> headers = new HashMap<>();
        headers.put(Keys.CONTENT_TYPE, "application/json");
        headers.put(Keys.AUTHORIZATION, "Bearer " + authToken);
        return headers;
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

    @Override
    public void onClick(BaseModel data, String s) {
        if (data instanceof CartProcessor){
            CartProcessor cartProcessor = (CartProcessor) data;
            cartProcessor.onProcess(this, cartService, this, loadingAlertDialog, headers());
        }

        if (data instanceof ExecutedCartProcessResult){
            ExecutedCartProcessResult result = (ExecutedCartProcessResult) data;
            if (result.isSuccess()){
                Toast.makeText(this, result.getProductName(), Toast.LENGTH_SHORT).show();
                result.getCartProcessor().onRefreshDetails(cartItems, total, result.getAdjustedQuantity());
                initCartRecyclerView();
            }else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}