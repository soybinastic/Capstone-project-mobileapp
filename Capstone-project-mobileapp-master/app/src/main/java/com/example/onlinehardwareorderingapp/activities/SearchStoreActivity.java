package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.adapters.BranchAdapter;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.BranchService;
import com.example.onlinehardwareorderingapp.models.BranchModel;
import com.example.onlinehardwareorderingapp.models.ui.Branch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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

public class SearchStoreActivity extends AppCompatActivity implements Invoker<BranchModel, String> {
    private RecyclerView stores;
    private EditText search;
    private UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    private RetrofitService retrofitService = RetrofitService.getInstance();
    private Retrofit retrofit;
    private BranchService branchService;
    private ArrayList<BranchModel> branchModels;
    private double km = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_store);

        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        stores = findViewById(R.id.search_recyview_stores);
        search = findViewById(R.id.et_search_stores);
        stores.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        km = intent.getDoubleExtra("adjustedKm",5);
        toolbar.setTitle("Search");
        setSupportActionBar(toolbar);
        initializeRetrofit();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initializeRetrofit(){
        retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        branchService = retrofit.create(BranchService.class);
    }
    private void onSearch(String value){
        branchService.search(headers(), value, km)
                .enqueue(new Callback<ArrayList<BranchModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<BranchModel>> call, Response<ArrayList<BranchModel>> response) {
                        if (response.isSuccessful()){
                            branchModels = response.body();
                            stores.setAdapter(new BranchAdapter(branchModels, stores, SearchStoreActivity.this));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<BranchModel>> call, Throwable t) {

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
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
        double lat = Double.parseDouble(sharedPreferences.getString(Keys.LATITUDE, "").isEmpty() ? "0" : sharedPreferences.getString(Keys.LATITUDE, ""));
        double lng = Double.parseDouble(sharedPreferences.getString(Keys.LONGITUDE, "").isEmpty() ? "0" : sharedPreferences.getString(Keys.LONGITUDE, ""));

        headers.put(Keys.LATITUDE, String.valueOf(lat != 0 ? lat : 0));
        headers.put(Keys.LONGITUDE, String.valueOf(lng != 0 ? lng : 0));
        return headers;
    }

    @Override
    public void onClick(BranchModel data, String s) {
        Toast.makeText(this, data.getName(), Toast.LENGTH_SHORT).show();
        //Branch branch = (Branch) data;
        //Toast.makeText(this, branch.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BranchDetailAndProductsActivity.class);
        //intent.putExtra("id", branch.getId());
        intent.putExtra("name", data.getName());
        intent.putExtra("address", data.getAddress());
        intent.putExtra("branchId", data.getId());
        intent.putExtra("hardwareStoreId", data.getHardwareStoreId());
        startActivity(intent);
    }
}