package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.dtos.order.PostOrderDto;
import com.example.onlinehardwareorderingapp.fragments.MapFragment;
import com.example.onlinehardwareorderingapp.fragments.bottomsheets.BottomSheetMap;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.MoneyFormat;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CartService;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CustomerService;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.OrderService;
import com.example.onlinehardwareorderingapp.models.CartDetailsModel;
import com.example.onlinehardwareorderingapp.models.CartItemModel;
import com.example.onlinehardwareorderingapp.models.CustomerModel;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class CheckOutActivity extends AppCompatActivity {
    CartDetailsModel cartDetailsModel;
    LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    RetrofitService retrofitService = RetrofitService.getInstance();
    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    private CustomerModel customerModel = null;
    private ArrayList<CartItemModel> cartItems;
    private double total;
    int counter = 0, branchId = 0, hardwareStoreId = 0;
    boolean isDeliver = false;
    String lat = "", lon = "";
    private String authToken = "";
    private Retrofit retrofit;
    TextView totalTxtView, fullName,address, contactNo, shippingFee, totalCost;
    Button placeOrderBtn;
    CheckBox isDeliverCheckBox;
    private BottomSheetMap map = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.check_out_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        branchId = intent.getIntExtra("branchId", 0);
        hardwareStoreId = intent.getIntExtra("hardwareStoreId", 0);

        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
        authToken = sharedPreferences.getString(Keys.AUTH_TOKEN, "");
        loadingAlertDialog.showDialog();
        initViews();
        initRetrofit();
        loadCustomerInfo();
        loadItemsInCart();
        
        //placeOrderBtn.setEnabled((customerModel != null && total > 0));
        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDeliver){
                    //map.show(getSupportFragmentManager(), map.getTag());
                    placeOrderBtn.setEnabled(false);
                    if(cartItems.size() > 0){
                        onPlaceOrder();
                    }
                }
            }
        });

        isDeliverCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(CheckOutActivity.this, b ? "YES" : "NO", Toast.LENGTH_SHORT).show();
                isDeliver = b;
                if (b){
                    map = new BottomSheetMap(false, customerModel.getLatitude(), customerModel.getLongitude());
                    map.show(getSupportFragmentManager(), map.getTag());
                }
            }
        });
//        Fragment fragment = new MapFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.map, fragment)
//                .commit();
    }
    void initViews(){
        placeOrderBtn = (Button) findViewById(R.id.place_order_btn);
        isDeliverCheckBox = (CheckBox) findViewById(R.id.check_out_checkbox);
        totalTxtView = (TextView) findViewById(R.id.check_out_total);
        fullName = (TextView) findViewById(R.id.checkout_customer_fullname);
        address = (TextView) findViewById(R.id.checkout_customer_address);
        contactNo = (TextView) findViewById(R.id.checkout_customer_contact_no);
        shippingFee = (TextView) findViewById(R.id.shipping_fee);
        totalCost = (TextView) findViewById(R.id.total_cost);
    }
    void initRetrofit(){
         retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    void showAlertDialog(String title, String message){
        AlertDialog alert = new AlertDialog.Builder(this)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alert.setTitle(title);
        alert.show();
    }

    void onPlaceOrder(){
        loadingAlertDialog.showDialog();

        OrderService orderService = retrofit.create(OrderService.class);
        Call<ServerResponse> result = orderService.placeOrder(new PostOrderDto(customerModel, cartItems,
                String.valueOf(customerModel.getLatitude()), String.valueOf(customerModel.getLongitude()),
                isDeliver, branchId, hardwareStoreId, cartDetailsModel.getShippingFee()), headers());
        result.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.isSuccessful()){
                    cartItems.clear();
                    Intent intent = new Intent(CheckOutActivity.this, SuccessfullyPlaceOrderActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    showAlertDialog("Warning", "Please verify your account\nGo to account\nFind Verify Now");
                    //Toast.makeText(CheckOutActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                loadingAlertDialog.dismiss();
                Toast.makeText(CheckOutActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void loadItemsInCart(){
        CartService cartService = retrofit.create(CartService.class);
        Call<CartDetailsModel> result = cartService.getItems(hardwareStoreId, branchId, headers());
        result.enqueue(new Callback<CartDetailsModel>() {
            @Override
            public void onResponse(Call<CartDetailsModel> call, Response<CartDetailsModel> response) {
                counter += 1;
                if (response.isSuccessful()){
                    cartDetailsModel = response.body();
                    cartItems = response.body().getCartItems();
                    generateTotalAndSet();
                }
                dismissAlert();
            }

            @Override
            public void onFailure(Call<CartDetailsModel> call, Throwable t) {
                counter += 1;
                dismissAlert();
            }
        });
    }
    void generateTotalAndSet(){
        for (CartItemModel ci : cartItems){
            total += (ci.getProductPrice() * ci.getProductQuantity());
        }
        totalTxtView.setText(MoneyFormat.getFormat(total));
        shippingFee.setText(MoneyFormat.getFormat(cartDetailsModel.getShippingFee()));
        totalCost.setText(MoneyFormat.getFormat((total + cartDetailsModel.getShippingFee())));
    }
    
    void setCustomerInfo(){
        fullName.setText(customerModel.getFirstName() + ", " + customerModel.getLastName());
        address.setText(customerModel.getAddress());
        contactNo.setText(customerModel.getContactNo());
    }
    void loadCustomerInfo(){
        CustomerService customerService = retrofit.create(CustomerService.class);
        Call<CustomerModel> result = customerService.getInformation(headers());
        result.enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                counter += 1;
                if (response.isSuccessful()){
                    customerModel = response.body();
                    setCustomerInfo();
                }
                dismissAlert();
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                counter += 1;
                dismissAlert();
            }
        });
    }

    private Map<String, String> headers(){
        Map<String, String> headers = new HashMap<>();
        headers.put(Keys.CONTENT_TYPE, "application/json");
        headers.put(Keys.AUTHORIZATION, "Bearer " + authToken);
        return headers;
    }

    void dismissAlert(){
        if (counter >= 2){
            loadingAlertDialog.dismiss();
        }
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
}