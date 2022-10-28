package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.SampleDataSet;
import com.example.onlinehardwareorderingapp.fragments.AccountFragment;
import com.example.onlinehardwareorderingapp.fragments.HomeFragment;
import com.example.onlinehardwareorderingapp.fragments.StoresFragment;
import com.example.onlinehardwareorderingapp.helper.AccountOption;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.helper.UserRule;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.CallBackClickListener;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.AuthService;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CustomerService;
import com.example.onlinehardwareorderingapp.models.BranchModel;
import com.example.onlinehardwareorderingapp.models.CustomerModel;
import com.example.onlinehardwareorderingapp.models.ui.Branch;
import com.example.onlinehardwareorderingapp.models.ui.Search;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class IntroActivity extends AppCompatActivity implements CallBackClickListener {

    LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    RetrofitService retrofitService = RetrofitService.getInstance();
    UserRule userRule = UserRule.getInstance();
    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    AccountOption.Dataset accountOptionDataset = new AccountOption.Dataset();

    BottomNavigationView nav;
    HomeFragment homeFragment;
    AccountFragment accountFragment;
    AlertDialog.Builder builder;
    boolean isUserLoggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        builder = new AlertDialog.Builder(this);
        //getSupportActionBar().hide();

        initializeSettings();
        initializeNav(savedInstanceState);


//        homeFragment.settingUp(getApplicationContext());
    }

    void initializeSettings(){
        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();

        String authToken = sharedPreferences.getString(Keys.AUTH_TOKEN, "");
        Date tokenExpDate = userRule.stringToDate(sharedPreferences.getString(Keys.TOKEN_EXPIRATION, ""));

        isUserLoggedIn = userRule.isLoggedIn(tokenExpDate, authToken);
        //hideViews(isUserLoggedIn);
        if (!isUserLoggedIn){
            showAlertDialogAlert("Message", "Please sign in");

            // to be  fix on how to remove or clear data inside of sharedpref
            userInformationStorage.getEditor().remove(Keys.AUTH_TOKEN);
            clearData();
//            userInformationStorage.getEditor().commit();
        }else {
            loadCustomerInfo();
        }

        homeFragment = new HomeFragment(this, isUserLoggedIn);
        accountFragment = new AccountFragment(accountOptionDataset.getOptions(), this);
        //Toast.makeText(this, authToken, Toast.LENGTH_SHORT).show();
    }

    void hideViews(){
        Menu menuItem = nav.getMenu();
        menuItem.findItem(R.id.account).setVisible(isUserLoggedIn);
    }

    void showAlertDialogAlert(String title, String message){
          AlertDialog alert = builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
          alert.setTitle(title);
          alert.show();
    }

    private void initializeNav(Bundle savedInstanceState){

        nav = (BottomNavigationView) findViewById(R.id.bottomNavMenu);

        hideViews();
        //String result = (savedInstanceState == null) ? "SAve instance state is null" : "SAve instance state is not null";
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.navItemContainer, homeFragment)
                    .commit();
            nav.setSelectedItemId(R.id.home);
        }

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        //Toast.makeText(IntroActivity.this,result,Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.navItemContainer, homeFragment)
                                .commit();
                        break;
                    case R.id.stores:
                        //Toast.makeText(IntroActivity.this, "Store Branches", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.navItemContainer, new StoresFragment(IntroActivity.this, retrofitService))
                                .commit();
                        break;
                    case R.id.account:
                        //Toast.makeText(IntroActivity.this, "Account", Toast.LENGTH_LONG).show();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.navItemContainer, accountFragment)
                                .commit();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void onNavigate() {
        Toast.makeText(this, "You click the sign in btn", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(BaseModel data, String s) {
        if (data instanceof Branch){
            Branch branch = (Branch) data;
            Toast.makeText(this, branch.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, BranchDetailAndProductsActivity.class);
            //intent.putExtra("id", branch.getId());
            intent.putExtra("name", branch.getName());
            intent.putExtra("address", branch.getAddress());
            intent.putExtra("branchId", branch.getId());
            intent.putExtra("hardwareStoreId", branch.getHardwareStoreId());
            startActivity(intent);
        }
        if (data instanceof AccountOption){
            Toast.makeText(this, ((AccountOption) data).getTitle(), Toast.LENGTH_LONG).show();
            navigate(((AccountOption) data).getCode());
        }

        if (data instanceof AccountFragment.ClickVerifyButton){
            navigate("verify");
        }

        if (data instanceof Search){
            Search search = (Search) data;
            Intent intent = new Intent(this, SearchStoreActivity.class);
            intent.putExtra("adjustedKm", search.getAdjustedKm());
            startActivity(intent);
        }

    }
    void showDialogWithoutPositiveBtn(String title, String message){
        AlertDialog alert = builder.setMessage(message)
                .create();
        alert.show();
    }
    void navigate(String code){
        Intent intent;
        if (code.equals(Keys.PERSONAL_INFORMATION_CODE)){
            intent = new Intent(IntroActivity.this, ProfileInformationActivity.class);
            startActivity(intent);
        }else if (code.equals("logout")){
            logout();
        }else if (code.equals(Keys.CUSTOMER_ORDER_CODE)){
            intent = new Intent(IntroActivity.this, HistoryOrdersActivity.class);
            startActivity(intent);
        }else if (code.equals("verify")){
            intent = new Intent(IntroActivity.this, VerificationFormActivity.class);
            startActivity(intent);
        }else if (code.equals(Keys.LOCATION_CODE)){
            intent = new Intent(IntroActivity.this, LocationActivity.class);
            startActivity(intent);
        }


    }
    void loadCustomerInfo() {
        loadingAlertDialog.showDialog();
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CustomerService customerService = retrofit.create(CustomerService.class);
        Call<CustomerModel> result = customerService.getInformation(getHeaders());
        result.enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                loadingAlertDialog.dismiss();
                if (response.isSuccessful()){
                    SharedPreferences.Editor editor = userInformationStorage.getEditor();
                    editor.putString(Keys.LATITUDE, String.valueOf(response.body().getLatitude()));
                    editor.putString(Keys.LONGITUDE, String.valueOf(response.body().getLongitude()));
                    editor.putBoolean(Keys.VERIFIED, response.body().isVerified());
                    editor.commit();
                }else {
                    Toast.makeText(IntroActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                loadingAlertDialog.dismiss();
                Toast.makeText(IntroActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void clearData(){
        SharedPreferences.Editor editor = userInformationStorage.getEditor();

        editor.putString(Keys.AUTH_TOKEN, "");
        editor.putString(Keys.USER_ID, "");
        editor.putString(Keys.TOKEN_EXPIRATION, "");
        editor.putString(Keys.USERNAME, "");
        editor.putString(Keys.LONGITUDE, "");
        editor.putString(Keys.LATITUDE, "");
        editor.commit();
    }
    void newInstanceFragment(){
        homeFragment = new HomeFragment(this, isUserLoggedIn);
    }
    void logout(){
        //showDialogWithoutPositiveBtn("", "Logging out...");
        loadingAlertDialog.showDialog();
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AuthService authService = retrofit.create(AuthService.class);
        Call<ServerResponse> result = authService.logout(getHeaders());
        result.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()){
                    clearData();
                    isUserLoggedIn = false;
                    newInstanceFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.navItemContainer, homeFragment)
                            .commit();
                    nav.setSelectedItemId(R.id.home);
                    hideViews();
                }else {
                    showAlertDialogAlert("Message", response.message());
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                showAlertDialogAlert("Error", t.getLocalizedMessage());
                loadingAlertDialog.dismiss();
            }
        });

    }

    private Map<String, String> getHeaders(){
        String authToken = userInformationStorage.getSharedPreferences().getString(Keys.AUTH_TOKEN, "");
        Map<String, String> headers = new HashMap<>();
        headers.put(Keys.CONTENT_TYPE, "application/json");
        headers.put(Keys.AUTHORIZATION, "Bearer " + authToken);
        return headers;
    }
}