package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.dtos.CustomerUpdateInformationDto;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CustomerService;
import com.example.onlinehardwareorderingapp.models.CustomerModel;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.time.LocalDateTime;

public class ProfileInformationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    AlertDialog.Builder builder;
    ConstraintLayout updateBtn;
    Date customerBirthDate;
    String customer_birthDate;
    EditText firstName, lastName, middleName, address, contactNo;
    TextView age, birthDate, errorMessage;
    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    RetrofitService retrofitService = RetrofitService.getInstance();
    String authToken;
    Map<String, String> headers = new HashMap<String, String>();
    private int year, month, day;
    private CustomerUpdateInformationDto customerUpdateInformationDto = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_information);

//        ActionBar actionBar = getSupportActionBar();
//        ColorDrawable color = new ColorDrawable(Color.parseColor("#212121"));
//        //actionBar.hide();
//        actionBar.setBackgroundDrawable(color);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.prof_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        builder = new AlertDialog.Builder(this);
        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        authToken = userInformationStorage.getSharedPreferences().getString(Keys.AUTH_TOKEN, "");

        initializeControls();
        initializeRequest();

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileInformationActivity.this, ProfileInformationActivity.this, year, month, day);
                datePickerDialog.show();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // to be fix soon
                if (isValid()){
                    onUpdateInfo();

                }
                //showDialog("Birthdate", convertDateFormat(customerBirthDate).toString());
            }
        });
    }

    void setResponseToControls(CustomerModel customerModel){
        firstName.setText(customerModel.getFirstName());
        lastName.setText(customerModel.getLastName());
        //middleName.setText(customerModel.getMiddleName());
        address.setText(customerModel.getAddress());
        contactNo.setText(customerModel.getContactNo());
        //age.setText(String.valueOf(customerModel.getAge()));
        birthDate.setText(birthDateFormat(customerModel.getBirthDate()));
        //customer_birthDate = customerModel.getBirthDate();
        customerUpdateInformationDto = new CustomerUpdateInformationDto(customerModel);
        //Toast.makeText(this, customerModel.getBirthDate(), Toast.LENGTH_SHORT).show();

        try {
            Date dateNow = new Date();
            Date birthdate = parseDate(customerModel.getBirthDate());
            int customerAge = ((dateNow.getMonth() >= birthdate.getMonth() && dateNow.getDay() >= birthdate.getDay()) || (dateNow.getMonth() > birthdate.getMonth() && dateNow.getDay() < birthdate.getDay())) ? (dateNow.getYear() - birthdate.getYear()) : (dateNow.getYear() - birthdate.getYear()) - 1;
            _age = customerAge;
            Toast.makeText(this, String.valueOf(_age), Toast.LENGTH_LONG).show();
        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void initializeRequest(){

        loadingAlertDialog.showDialog();
        headers.put(Keys.CONTENT_TYPE, "application/json");
        headers.put(Keys.AUTHORIZATION, "Bearer " + authToken);
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CustomerService customerService = retrofit.create(CustomerService.class);
        Call<CustomerModel> result = customerService.getInformation(headers);
        result.enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                if (response.isSuccessful()){
                    setResponseToControls(response.body());
                }else {
                    Toast.makeText(ProfileInformationActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                Toast.makeText(ProfileInformationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingAlertDialog.dismiss();
            }
        });
    }

    void initializeControls(){
        firstName = (EditText) findViewById(R.id.et_prof_info_firstname);
        lastName = (EditText) findViewById(R.id.et_prof_info_lastname);
        //middleName = (EditText) findViewById(R.id.et_prof_info_middlename);
        address = (EditText) findViewById(R.id.et_prof_info_address);
        contactNo = (EditText) findViewById(R.id.et_prof_info_contactno);
        //age = (TextView) findViewById(R.id.tv_prof_info_age);
        birthDate = (TextView) findViewById(R.id.tv_prof_info_birthdate);
        updateBtn = (ConstraintLayout) findViewById(R.id.prof_info_update_btn);
        errorMessage = findViewById(R.id.error_message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.essential_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private boolean isValid(){
        return (is_fn_valid() && is_ln_valid() && is_address_valid() && is_contact_no_valid() && is_birthdate_valid());
    }

    private boolean is_fn_valid(){
        if (firstName.getText().toString().isEmpty()){
            firstName.setError("This field is required.");
            return false;
        }
        firstName.setError(null);
        return true;
    }
    private boolean is_ln_valid(){
        if (lastName.getText().toString().isEmpty()){
            lastName.setError("This field is required");
            return false;
        }
        lastName.setError(null);
        return true;
    }
//    private boolean is_middle_name_valid(){
//        if (middleName.getText().toString().isEmpty()){
//            middleName.setError("This field is required");
//            return false;
//        }
//        middleName.setError(null);
//        return true;
//    }
    private int _age = 0;
    private boolean is_birthdate_valid(){
       if (_age >= 18){
           birthDate.setError(null);
           errorMessage.setVisibility(View.GONE);
           return true;
       }
       //birthDate.setError("Not legal age");
        errorMessage.setVisibility(View.VISIBLE);
       errorMessage.setText("You must be 18 or older to use this app.");
       return false;
    }
    private boolean is_address_valid(){
        if (address.getText().toString().isEmpty()){
            address.setError(null);
            return false;
        }
        address.setError(null);
        return true;
    }
    private boolean is_contact_no_valid(){
        if (contactNo.getText().toString().isEmpty()){
            contactNo.setError("This field is required");
            return false;
        }
        if (contactNo.getText().toString().length() > 11 || contactNo.getText().toString().length() < 11){
            contactNo.setError("This field required 11 digit of numbers");
            return false;
        }
        contactNo.setError(null);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        Intent intent = new Intent(this, IntroActivity.class);
//        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private Date parseDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(date);
    }

    private String birthDateFormat(String bdString){
        return bdString.split("T")[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        try {
            this.year = i;
            this.month = i1;
            this.day = i2;
            String date = "" + i + "/" + (i1+1) + "/" + i2;

            Date dateNow = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/M/dd");
            Date birthdate = formatter.parse(date);

            int month = birthdate.getMonth();
            int day = birthdate.getDate();
            int customerAge = ((dateNow.getMonth() >= month && dateNow.getDate() >= day) || (dateNow.getMonth() > month && dateNow.getDate() < day)) ? (dateNow.getYear() - birthdate.getYear()) : (dateNow.getYear() - birthdate.getYear()) - 1;
            _age = customerAge;
            customerBirthDate = birthdate;
            //birthDate.setText(birthdate.toString());
            //age.setText(String.valueOf(customerAge));
            String date_format_string = "T11:36:09.358Z";
            customer_birthDate = "" + i + "-" + numFormat(i1+1) + "-" + numFormat(i2) + date_format_string;
            customerUpdateInformationDto.setBirthDate(customer_birthDate);
//            DateTimeFormatter dtfInput = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
//            LocalDateTime bd = LocalDateTime.of(LocalDate.parse(d, dtfInput), LocalTime.of(21, 0));
//            ZonedDateTime DATE = bd.atZone(ZoneOffset.UTC);
//            DateTimeFormatter dtfOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH);
//            String birth_DATE = dtfOutput.format(DATE);
            birthDate.setText("" + i + "-" + numFormat(i1+1) + "-" + numFormat(i2));
            //showDialog("Date", );

        }catch (Exception ex){

        }


//        int age =
    }

    private String numFormat(int n){

        if (n > 9){
            return String.valueOf(n);
        }

        return "0" + n;
    }

    private String convertDateFormat(Date birthDate){
//        try {
//            // 2022-08-01T15:42:25.097Z
//            String stringDate = "" + birthDate.getYear() + "-" + (birthDate.getMonth() + 1) + "-" + birthDate.getDate() + "T15:42:25.097Z";
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
//            return formatter.parse(stringDate);
//        }catch (Exception ex){
//            return  null;
//        }
        return "" + birthDate.getYear() + "-" + (birthDate.getMonth() + 1) + "-" + birthDate.getDate() + "T15:42:25.097Z";
    }

    void onUpdateDataFromInputs(){
        //                    onUpdateInfo(new CustomerUpdateInformationDto(firstName.getText().toString(),
//                            lastName.getText().toString(), middleName.getText().toString(),
//                            address.getText().toString(), contactNo.getText().toString(),
//                            Integer.parseInt(age.getText().toString()), customer_birthDate));
        customerUpdateInformationDto.setFirstName(firstName.getText().toString());
        customerUpdateInformationDto.setLastName(lastName.getText().toString());
        //customerUpdateInformationDto.setMiddleName(middleName.getText().toString());
        customerUpdateInformationDto.setAddress(address.getText().toString());
        customerUpdateInformationDto.setContactNo(contactNo.getText().toString());
        //customerUpdateInformationDto.setAge(Integer.parseInt(age.getText().toString()));
    }

    void onUpdateInfo(){

        //Toast.makeText(this, customerUpdateInformationDto.getBirthDate(), Toast.LENGTH_SHORT).show();
        //Map<String, String> headers = new HashMap<>();
//        headers.put(Keys.CONTENT_TYPE, "application/json");
//        headers.put(Keys.AUTHORIZATION, "Bearer " + authToken);
        if (customerUpdateInformationDto == null){
            showDialog("Message", "Unable to update");
            return;
        }
        loadingAlertDialog.showDialog();
        onUpdateDataFromInputs();
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CustomerService customerService = retrofit.create(CustomerService.class);
        Call<ServerResponse> result = customerService.updateInfo(customerUpdateInformationDto, headers);
        result.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()){
                    showDialog("Message", "Successfully updated profile");
                }else {
                    try {
                        showDialog("Error", response.errorBody().string().toString());
                    }catch (Exception ex){

                    }
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                showDialog("Error", t.getMessage());
                loadingAlertDialog.dismiss();
            }
        });
    }

    void showDialog(String title, String message){
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
}