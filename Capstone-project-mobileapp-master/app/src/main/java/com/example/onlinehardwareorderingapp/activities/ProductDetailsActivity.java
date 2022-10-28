package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.dtos.InitializeItemToCartDto;
import com.example.onlinehardwareorderingapp.helper.ButtonWithProgressBar;
import com.example.onlinehardwareorderingapp.helper.JwtHelper;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.MoneyFormat;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.helper.UserRule;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CartService;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.ProductService;
import com.example.onlinehardwareorderingapp.models.ProductModel;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailsActivity extends AppCompatActivity {
    RetrofitService retrofitService = RetrofitService.getInstance();
    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();

    LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    AlertDialog alertDialog;
    private int branchId = 0, hardwareProductId = 0, hardwareStoreId = 0;
    private ConstraintLayout cart;
    private ImageView backBtn, productImage;
    private View addToCartView;
    private ButtonWithProgressBar buttonWithProgressBar;
    private TextView productName, description, price, increase, decrease;
    private EditText quantityInput;
    private ProductModel productModel;
    private boolean isUserLoggedIn = false;
    private String authToken = "";
    private UserRule userRule = UserRule.getInstance();
    private boolean verified;
    private int quantityStateCounter = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        //getSupportActionBar().hide();
        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
        authToken = sharedPreferences.getString(Keys.AUTH_TOKEN, "");
        verified = sharedPreferences.getBoolean(Keys.VERIFIED, false);
        Date tokenExp = userRule.stringToDate(sharedPreferences.getString(Keys.TOKEN_EXPIRATION, ""));

        isUserLoggedIn = userRule.isLoggedIn(tokenExp, authToken);

        Intent intent = getIntent();
        branchId = intent.getIntExtra("branchId", 0);
        hardwareStoreId = intent.getIntExtra("hardwareStoreId", 0);
        hardwareProductId = intent.getIntExtra("hardwareProductId", 0);

        initViews();
        loadProductDetails();
        //cart.setVisibility((isUserLoggedIn ? View.VISIBLE : View.GONE));

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addToCartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserLoggedIn){
                    if (!verified){
                        showAlert("Message", "You need to verify your account.", Keys.VERIFIED);
                        return;
                    }
                    buttonWithProgressBar = new ButtonWithProgressBar(view, ProductDetailsActivity.this,
                            "Add to Cart", "Adding...");
                    buttonWithProgressBar.activateProgressBar();
                    onAddToCart();
                }else {
                    showAlert("Message", "Please login first and add this item to cart.", "");
                }
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserLoggedIn){
                    Intent intent1 = new Intent(ProductDetailsActivity.this, CartActivity.class);
                    intent1.putExtra("branchId", branchId);
                    intent1.putExtra("hardwareStoreId", hardwareStoreId);
                    startActivity(intent1);
                }else {
                    showAlert("Message", "Please login first and navigate to cart to see the items that you added.", "");
                }
            }
        });

        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityStateCounter += 1;
                quantityInput.setText(String.valueOf(quantityStateCounter));
            }
        });

        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantityStateCounter == 1) return;

                quantityStateCounter -= 1;
                quantityInput.setText(String.valueOf(quantityStateCounter));
            }
        });

        quantityInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().isEmpty()){
                    quantityInput.setError("Invalid value of quantity");
                    return;
                }

                int qty = Integer.parseInt(charSequence.toString());
                if (qty <= 0){
                    quantityStateCounter = 1;
                    quantityInput.setText(String.valueOf(quantityStateCounter));
                }else {
                    quantityStateCounter = qty;
                }

                quantityInput.setError(null);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    void showAlert(String title, String message, String arg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (arg.equals(Keys.VERIFIED)){
                    Intent intent = new Intent(ProductDetailsActivity.this, VerificationFormActivity.class);
                    startActivity(intent);
                }
            }
        });

        alertDialog = builder.create();
        alertDialog.setTitle(title);
        alertDialog.show();
    }
    private Map<String, String> headers(){
        Map<String, String> headers = new HashMap<>();
        headers.put(Keys.CONTENT_TYPE, "application/json");
        headers.put(Keys.AUTHORIZATION, "Bearer " + authToken);

        return headers;
    }

    void initViews(){
        backBtn = (ImageView) findViewById(R.id.prod_d_back);
        addToCartView = findViewById(R.id.prod_details_add_to_cart_btn);
        productName = (TextView) findViewById(R.id.prod_details_name_tv);
        description = (TextView) findViewById(R.id.prod_details_description_tv);
        price = (TextView) findViewById(R.id.prod_details_price_tv);
        cart = (ConstraintLayout) findViewById(R.id.prod_details_cart);
        productImage = (ImageView) findViewById(R.id.prod_details_imgview);
        increase = findViewById(R.id.increase);
        decrease = findViewById(R.id.decrease);
        quantityInput = findViewById(R.id.quantity_input);
    }

    void onAddToCart(){
        if (productModel == null){
            showAlert("Error", "No item found.", "");
            return;
        }

        if (quantityInput.getText().toString().isEmpty()){
            quantityStateCounter = 1;
        }

        addToCartView.setEnabled(false);
        backBtn.setEnabled(false);
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CartService cartService = retrofit.create(CartService.class);
        Call<ServerResponse> result = cartService.initializeItem(headers(), new InitializeItemToCartDto(productModel.getHardwareStoreId(),
                productModel.getBranchId(), productModel.getHardwareProductId(), productModel.getCategoryId(),
                productModel.getName(), productModel.getPrice(), productModel.getDescription(),
                productModel.getBrand(), productModel.getQuality()), quantityStateCounter);
        result.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                addToCartView.setEnabled(true);
                backBtn.setEnabled(true);
                if (response.isSuccessful()){
                    Toast.makeText(ProductDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ProductDetailsActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                }
                buttonWithProgressBar.finish();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                addToCartView.setEnabled(true);
                backBtn.setEnabled(true);
                Toast.makeText(ProductDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                buttonWithProgressBar.finish();
            }

        });
    }

    void loadProductDetails(){
        loadingAlertDialog.showDialog();
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductService productService = retrofit.create(ProductService.class);
        Call<ProductModel> result = productService.getOne(branchId, hardwareProductId);
        result.enqueue(new Callback<ProductModel>() {
            @Override
            public void onResponse(Call<ProductModel> call, Response<ProductModel> response) {
                if (response.isSuccessful()){
                    setDataToViews(response.body());
                }

                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProductModel> call, Throwable t) {
                loadingAlertDialog.dismiss();
            }
        });
    }

    void setDataToViews(ProductModel productModel){
        this.productModel = productModel;

        productName.setText(productModel.getName());
        description.setText(productModel.getDescription());
        price.setText(MoneyFormat.getFormat(productModel.getPrice()));
        Glide.with(this)
                .load(RetrofitService.API_URL + productModel.getImageFile())
                .fitCenter()
                .into(productImage);
    }

}