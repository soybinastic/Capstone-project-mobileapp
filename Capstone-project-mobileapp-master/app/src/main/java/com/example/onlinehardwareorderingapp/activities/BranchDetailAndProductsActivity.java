package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.SampleDataSet;
import com.example.onlinehardwareorderingapp.adapters.ProductAdapter;
import com.example.onlinehardwareorderingapp.adapters.ProductCategoryAdapter;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.helper.UserRule;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CategoryService;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.ProductService;
import com.example.onlinehardwareorderingapp.models.CategoryModel;
import com.example.onlinehardwareorderingapp.models.ProductModel;
import com.example.onlinehardwareorderingapp.models.ui.Category;
import com.example.onlinehardwareorderingapp.models.ui.Product;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BranchDetailAndProductsActivity extends AppCompatActivity implements Invoker<BaseModel, String> {
    private int count = 0;
    private int branchId;
    int hardwareStoreId = 0;
    private LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    AlertDialog alertDialog;
    RetrofitService retrofitService = RetrofitService.getInstance();
    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView textViewAddress, resultMessage;
    EditText search;
    RecyclerView branchProductCategories;
    RecyclerView branchProducts;
    Toolbar toolbar;
    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    UserRule userRule = UserRule.getInstance();
    Retrofit retrofit;
    ProductService productService;
    CategoryService categoryService;
    private boolean isUserLoggedIn = false;
    private String authToken = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_detail_and_products);
        //getSupportActionBar().hide();
        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
        authToken = sharedPreferences.getString(Keys.AUTH_TOKEN, "");
        String expString = sharedPreferences.getString(Keys.TOKEN_EXPIRATION, "");
        Date exp = userRule.stringToDate(expString);
        isUserLoggedIn = userRule.isLoggedIn(exp, authToken);

        Intent intent = getIntent();
        branchId = intent.getIntExtra("branchId", 0);
        hardwareStoreId = intent.getIntExtra("hardwareStoreId", 0);
        String name = intent.getStringExtra("name");
        String address = intent.getStringExtra("address");

        initializeViews();

        collapsingToolbarLayout.setTitle(name);
        textViewAddress.setText(address);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        productService = retrofit.create(ProductService.class);
        categoryService = retrofit.create(CategoryService.class);
        initializeRecycleViews(hardwareStoreId, branchId);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadProducts(branchId, charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.branch_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void showAlert(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.setTitle(title);
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cart:
                if (isUserLoggedIn){
                    Intent intent = new Intent(this, CartActivity.class);
                    intent.putExtra("hardwareStoreId", hardwareStoreId);
                    intent.putExtra("branchId", branchId);
                    startActivity(intent);
                }else {
                    showAlert("Message", "Please login first and navigate to cart to see the items that you added.");
                }
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProducts(int branchId ,String search){
        Call<ArrayList<ProductModel>> productResult = productService.getAll(branchId, search);
        productResult.enqueue(new Callback<ArrayList<ProductModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductModel>> call, Response<ArrayList<ProductModel>> response) {
                count += 1;
                dismissDialog();
                if (response.isSuccessful()){
                    resultMessage.setVisibility(response.body().size() > 0 ? View.GONE : View.VISIBLE);
                    branchProducts.setAdapter(new ProductAdapter(response.body(), BranchDetailAndProductsActivity.this, branchProducts, BranchDetailAndProductsActivity.this));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductModel>> call, Throwable t) {

            }
        });
    }

    private void initializeRecycleViews(int storeId, int branchId){
        loadingAlertDialog.showDialog();
        branchProductCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //branchProductCategories.setAdapter(new ProductCategoryAdapter(SampleDataSet.getProductCategories()));

        branchProducts.setLayoutManager(new GridLayoutManager(this, 2));
//        branchProducts.setAdapter(new ProductAdapter(SampleDataSet.getProducts()));




        Call<ArrayList<CategoryModel>> categoryResult = categoryService.getAll(storeId);


        categoryResult.enqueue(new Callback<ArrayList<CategoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryModel>> call, Response<ArrayList<CategoryModel>> response) {
                count += 1;
                dismissDialog();
                if (response.isSuccessful()){
                    branchProductCategories.setAdapter(new ProductCategoryAdapter(response.body(), BranchDetailAndProductsActivity.this, branchProductCategories));
                }else{
                    Toast.makeText(BranchDetailAndProductsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryModel>> call, Throwable t) {

            }
        });
        loadProducts(branchId, "");


    }

    private void dismissDialog(){
        if (count >= 2){
            loadingAlertDialog.dismiss();
            count = 0;
        }
    }

    private void initializeViews(){
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.branchCollapsingToolbar);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        branchProductCategories = (RecyclerView) findViewById(R.id.categoriesRecyclerView);
        branchProducts = (RecyclerView) findViewById(R.id.productRecyclerView);
        toolbar = (Toolbar) findViewById(R.id.branchToolBar);
        resultMessage = findViewById(R.id.message_result);
        search = findViewById(R.id.search_product);
    }

    @Override
    public void onClick(BaseModel data, String s) {
        if (data instanceof Category){
            Category category = (Category) data;
            onSelectedCategory(category.getCategoryId());
        }else if (data instanceof Product){
            Product product = (Product) data;
            onNavigate(product.getHardwareProductId());
        }
    }

    void onNavigate(int hardwareProductId){
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("hardwareStoreId", hardwareStoreId);
        intent.putExtra("branchId", branchId);
        intent.putExtra("hardwareProductId", hardwareProductId);
        startActivity(intent);
    }

    void onSelectedCategory(int categoryId){
        loadingAlertDialog.showDialog();
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ProductService productService = retrofit.create(ProductService.class);
        Call<ArrayList<ProductModel>> result = productService.getByCategory(branchId, categoryId);
        result.enqueue(new Callback<ArrayList<ProductModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductModel>> call, Response<ArrayList<ProductModel>> response) {
                loadingAlertDialog.dismiss();
                if (response.isSuccessful()){
                    resultMessage.setVisibility(response.body().size() > 0 ? View.GONE : View.VISIBLE);
                    branchProducts.setAdapter(new ProductAdapter(response.body(), BranchDetailAndProductsActivity.this, branchProducts, BranchDetailAndProductsActivity.this));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductModel>> call, Throwable t) {

            }
        });
    }
}