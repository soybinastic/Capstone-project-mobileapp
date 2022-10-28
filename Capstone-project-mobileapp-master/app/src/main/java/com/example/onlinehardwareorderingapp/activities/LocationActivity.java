package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import  com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.dtos.CustomerUpdateInformationDto;
import com.example.onlinehardwareorderingapp.fragments.bottomsheets.BottomSheetMap;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CustomerService;
import com.example.onlinehardwareorderingapp.models.CustomerModel;
import com.example.onlinehardwareorderingapp.models.ui.LatLong;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationActivity extends AppCompatActivity implements Invoker<LatLong, String> {
    LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    RetrofitService retrofitService = RetrofitService.getInstance();
    Retrofit retrofit;
    CustomerModel customerModel = null;
    CustomerUpdateInformationDto customerUpdateInformationDto = null;
    String authToken = "";
    TextView lat, lng;
    Button update, setCooords;
    BottomSheetMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = findViewById(R.id.location_toolbar);
        toolbar.setTitle("Location");
        setSupportActionBar(toolbar);
        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        authToken = userInformationStorage.getSharedPreferences().getString(Keys.AUTH_TOKEN, "");
        lat = findViewById(R.id.lat);
        lng = findViewById(R.id.lng);
        update = findViewById(R.id.location_update_btn);
        setCooords = findViewById(R.id.set_coords_btn);
        initRetrofit();
        loadInformation();

        setCooords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map = new BottomSheetMap(true, customerModel.getLatitude(), customerModel.getLongitude(), LocationActivity.this);
                map.show(getSupportFragmentManager(), map.getTag());
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpdate();
            }
        });
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

    private void initRetrofit(){
        retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    private Map<String, String> headers(){
        Map<String, String> headers = new HashMap<>();
        headers.put(Keys.CONTENT_TYPE, "application/json");
        headers.put(Keys.AUTHORIZATION, "Bearer " + authToken);
        return headers;
    }
    private void loadInformation(){
        loadingAlertDialog.showDialog();
        CustomerService customerService = retrofit.create(CustomerService.class);
        Call<CustomerModel> result = customerService.getInformation(headers());
        result.enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                if (response.isSuccessful()){
                    customerModel = response.body();
                    lat.setText(String.valueOf(customerModel.getLatitude()));
                    lng.setText(String.valueOf(customerModel.getLongitude()));
                    customerUpdateInformationDto = new CustomerUpdateInformationDto(customerModel);
                }else {
                    Toast.makeText(LocationActivity.this, "Failed to load information", Toast.LENGTH_SHORT).show();
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                loadingAlertDialog.dismiss();
                Toast.makeText(LocationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(String message){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setMessage(message)
                .create();
        alertDialog.setTitle("Information");
        alertDialog.show();
    }

    private void onUpdate() {
        if (customerUpdateInformationDto == null){
            showDialog("Unable to update");
            return;
        }

        loadingAlertDialog.showDialog();
        CustomerService customerService = retrofit.create(CustomerService.class);
        Call<ServerResponse> result = customerService.updateInfo(customerUpdateInformationDto, headers());
        result.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                loadingAlertDialog.dismiss();
                if (response.isSuccessful()){
                    SharedPreferences.Editor editor = userInformationStorage.getEditor();
                    editor.putString(Keys.LONGITUDE, String.valueOf(customerUpdateInformationDto.getLng()));
                    editor.putString(Keys.LATITUDE, String.valueOf(customerUpdateInformationDto.getLat()));
                    editor.commit();
                    showDialog(response.body().getMessage());
                }else {
                    showDialog("Failed to update coordinates");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                loadingAlertDialog.dismiss();
                showDialog("Something went wrong.");
            }
        });
    }

    @Override
    public void onClick(LatLong data, String s) {
        customerUpdateInformationDto.setLat(data.getLat());
        customerUpdateInformationDto.setLng(data.getLng());
        lat.setText(String.valueOf(customerUpdateInformationDto.getLat()));
        lng.setText(String.valueOf(customerUpdateInformationDto.getLng()));
    }
}