package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.dtos.UserLoginDto;
import com.example.onlinehardwareorderingapp.helper.JwtHelper;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.AuthService;
import com.example.onlinehardwareorderingapp.models.AuthResponseModel;
import com.example.onlinehardwareorderingapp.models.WeatherForecast;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {
    LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    SharedPreferences.Editor editor;
    RetrofitService retrofitService = RetrofitService.getInstance();
    EditText username, password;
    ConstraintLayout signInBtn, createAccBtn;
    TextView signInTxt;
    AlertDialog.Builder alertBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//        ActionBar actionBar = getSupportActionBar();
//        ColorDrawable color = new ColorDrawable(Color.parseColor("#212121"));
//        //actionBar.hide();
//        actionBar.setBackgroundDrawable(color);
//        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.sign_in_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        initializeControls();
        alertBuilder = new AlertDialog.Builder(this);

        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        editor = userInformationStorage.getEditor();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.essential_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void signIn(){

        if (isUsernameValid() && isPasswordValid()){
            loadingAlertDialog.showDialog();
            signInBtn.setEnabled(false);
            createAccBtn.setEnabled(false);
            signInTxt.setText("Signing in...");

            Retrofit retrofit = retrofitService.getRetrofitBuilder()
                    .baseUrl(RetrofitService.API_URL)
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthService authService = retrofit.create(AuthService.class);
            Call<AuthResponseModel> result = authService.login(new UserLoginDto(username.getText().toString(), password.getText().toString()));
            result.enqueue(new Callback<AuthResponseModel>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                    signInTxt.setText("Sign In");
                    signInBtn.setEnabled(true);
                    createAccBtn.setEnabled(true);
                    if (response.isSuccessful()){
                        //Toast.makeText(SignInActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();
                        String[]splitsToken = response.body().getToken().split("\\.");
                        String decodedString = JwtHelper.decoder(splitsToken[1]);
                        JwtHelper.Payload userCredentials = payloadJson(decodedString);
                        //Toast.makeText(SignInActivity.this, username, Toast.LENGTH_LONG).show();
                        editor.putString(Keys.AUTH_TOKEN, response.body().getToken());
                        editor.putString(Keys.USER_ID, userCredentials.getUserId());
                        editor.putString(Keys.TOKEN_EXPIRATION, userCredentials.getExpirationDate().toString());
                        editor.putString(Keys.USERNAME, userCredentials.getUserName());
                        editor.commit();

                        Intent intent = new Intent(SignInActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        //Toast.makeText(SignInActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        showAlertDialog();
                    }
                    loadingAlertDialog.dismiss();
                }

                @Override
                public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                    signInTxt.setText("Sign In");
                    signInBtn.setEnabled(true);
                    createAccBtn.setEnabled(true);
                    Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    loadingAlertDialog.dismiss();
                }
            });
            
        }else{
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
        }

    }

    private void showAlertDialog(){
        alertBuilder.setMessage("Username or password are invalid.");
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alert = alertBuilder.create();
        alert.setTitle("Signing in was failed");
        alert.show();
    }
    private JwtHelper.Payload payloadJson(String decodedString){
        try {
            JSONObject payload = JwtHelper.getData(decodedString);
            String userId = payload.getString("nameid");
            String username = payload.getString("unique_name");
            long tokenExpiration = payload.getLong("exp");
            return new JwtHelper.Payload(userId, new Date(tokenExpiration * 1000), username);
        }catch (Exception ex){
            return null;
        }
    }
    boolean isPasswordValid(){
        String pass_word = password.getText().toString();


        if (pass_word.isEmpty()){
            password.setError("This field is required.");
            return false;
        }
        if (pass_word.length() < 5){
            password.setError("Password must have more than 5 characters");
            return false;
        }
        password.setError(null);
        return true;
    }
    boolean isUsernameValid(){
        String user_name = username.getText().toString();
        if (user_name.isEmpty()){
            username.setError("This field is required.");
            return false;
        }
        username.setError(null);
        return true;
    }
    private void initializeControls(){
        username = findViewById(R.id.editTextUsername);
        password = findViewById(R.id.editTextPassword);
        signInBtn = findViewById(R.id.signInBtn);
        createAccBtn = findViewById(R.id.createAccountBtn);
        signInTxt = findViewById(R.id.signInTxt);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }
}