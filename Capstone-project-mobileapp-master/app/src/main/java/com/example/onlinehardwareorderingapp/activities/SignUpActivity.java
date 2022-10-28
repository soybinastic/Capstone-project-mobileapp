package com.example.onlinehardwareorderingapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.dtos.CustomerCreateAccountDto;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CustomerService;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    ImageView imgViewBackBtn;
    EditText firstName, lastName, address, cellNo, username, password, confirmPassword;
    ConstraintLayout signUpBtn;
    TextView signUpBtnText;
    AlertDialog.Builder alertBuilder;
    RetrofitService retrofitService = RetrofitService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        //getSupportActionBar().hide();
        initializeViews();
        alertBuilder = new AlertDialog.Builder(this);

        imgViewBackBtn = (ImageView) findViewById(R.id.sign_up_back_btn);
        imgViewBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()){
                    //Toast.makeText(SignUpActivity.this, "Valid", Toast.LENGTH_SHORT).show();
                    signUpBtn.setEnabled(false);
                    onCreateAccount();
                }else {
                    Toast.makeText(SignUpActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void onCreateAccount(){
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CustomerService customerService = retrofit.create(CustomerService.class);
        Call<ServerResponse> result = customerService.register(new CustomerCreateAccountDto(firstName.getText().toString(),
                lastName.getText().toString(), address.getText().toString(), cellNo.getText().toString(),
                username.getText().toString() ,password.getText().toString(), confirmPassword.getText().toString()));
        signUpBtnText.setText("Creating...");
        loadingAlertDialog.showDialog();
        result.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                signUpBtnText.setText("Create account");
                signUpBtn.setEnabled(true);
                loadingAlertDialog.dismiss();
                if (response.isSuccessful()){
                    //Toast.makeText(SignUpActivity.this, "Successfully created.", Toast.LENGTH_LONG).show();
                    showSuccessAlertDialog();
                }else {
                    //Toast.makeText(SignUpActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    showAlertDialog("Failed to create an account");
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                signUpBtnText.setText("Create account");
                signUpBtn.setEnabled(true);
                loadingAlertDialog.dismiss();
                showAlertDialog("Please try again.");
                //finish();
                //Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    void showAlertDialog(String message){
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Error Message");
        alert.show();
    }
    void showSuccessAlertDialog(){
        alertBuilder.setMessage("Account Created Successfully");
        alertBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Success");
        alert.show();
    }
    boolean isValid() {
        return (isFirstNameValid() && isLastNameValid() && isAddressValid() && isCellNoValid() && isUsernameValid() && isPasswordValid() && isConfirmPasswordValid());
    }
    boolean isCellNoValid(){
        if (cellNo.getText().toString().isEmpty()){
            cellNo.setError("This field is required");
            return false;
        }

        if (cellNo.getText().toString().length() > 11 || cellNo.getText().toString().length() < 11){
            cellNo.setError("Cell number must contain 11 characters");
            return false;
        }
        cellNo.setError(null);
        return true;
    }
    boolean isConfirmPasswordValid(){
        if (!confirmPassword.getText().toString().equals(password.getText().toString())){
            confirmPassword.setError("Password and confirm password not matched.");
            return false;
        }
        confirmPassword.setError(null);
        return true;
    }
    private boolean isMatched(String regex, String passwordVal){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passwordVal);
        return matcher.matches();
    }
    boolean isPasswordValid(){
        //String nonAlphaNumeric = ".+[^A-Za-z0-9]";
        String digits = ".+[0-9]";
        String lowerCase = ".+[a-z]";
        String upperCase = ".+[A-Z]";
        //String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#&()-[{}]:;',?/*~$^+=<>]).{5,24}$";
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()-[{}]:;',?/*~$^+=<>]).{5,24}$";
        if (!password.getText().toString().equals(confirmPassword.getText().toString())){
            password.setError("Password and confirm password not matched.");
            return  false;
        }

        if (password.getText().toString().isEmpty()){
            password.setError("This field is required.");
            return  false;
        }
        if (password.getText().toString().length() < 5){
            password.setError("Password must have more than 5 characters.");
            return false;
        }
//        if (!isMatched(nonAlphaNumeric, password.getText().toString())){
//            password.setError("Password must contain non-alphanumeric value");
//            return false;
//        }
//        if (!isMatched(digits, password.getText().toString())){
//            password.setError("Password must contain at least 3 numbers");
//            return false;
//        }
//
//        if (!isMatched(lowerCase, password.getText().toString())){
//            password.setError("Password must contain lowercase [a-z]");
//            return false;
//        }
//
//        if (!isMatched(upperCase, password.getText().toString())){
//            password.setError("Password must contain uppercase [A-Z]");
//            return false;
//        }
        if (!isMatched(regex, password.getText().toString())){
            password.setError("Password must contain uppercase, lowercase, digits and special characters.");
            return false;
        }
        password.setError(null);
        return true;
    }
    boolean isUsernameValid(){
        if (username.getText().toString().isEmpty()){
            username.setError("This field is required");
            return false;
        }
        username.setError(null);
        return true;
    }
    boolean isAddressValid(){
        if (address.getText().toString().isEmpty()){
            address.setError("This field is required");
            return false;
        }
        address.setError(null);
        return true;
    }
    boolean isLastNameValid(){
        if (lastName.getText().toString().isEmpty()){
            lastName.setError("This field is required.");
            return false;
        }
        lastName.setError(null);
        return true;
    }
    boolean isFirstNameValid(){
        if (firstName.getText().toString().isEmpty()){
            firstName.setError("This field is required.");
            return false;
        }
        firstName.setError(null);
        return true;
    }
    void initializeViews(){
        signUpBtn = (ConstraintLayout) findViewById(R.id.signupBtn);
        signUpBtnText = (TextView) findViewById(R.id.sign_up_btn_textview); 
        firstName = (EditText) findViewById(R.id.editTextFirstName);
        lastName = (EditText) findViewById(R.id.editTextLastName);
        address = (EditText) findViewById(R.id.editTextAddress);
        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        confirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        cellNo = (EditText) findViewById(R.id.editTextCellNo);
    }
}