package com.example.onlinehardwareorderingapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CustomerService;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.VerificationService;
import com.example.onlinehardwareorderingapp.models.CustomerModel;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerificationFormActivity extends AppCompatActivity {
    LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(this);
    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    private String authToken = "";
    private String nbi_file, bc_file, government_id_file;
    RetrofitService retrofitService = RetrofitService.getInstance();
    Retrofit retrofit;
    TextView fullNameTextView, addressTextView, contactNoTextView;
    Button nbiBtn, barangayClearanceBtn, governmentIdBtn, submitBtn;
    private CustomerModel customerModel;
    private int counter = 0;

    private static final int REQUEST_PICK_NBI_IMAGE = 100;
    private static final int REQUEST_PICK_BC_IMAGE = 200;
    private static final int REQUEST_PICK_GOV_ID_IMAGE = 300;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.verification_toolbar);
        toolbar.setTitle("Verification Form");
        setSupportActionBar(toolbar);
        fullNameTextView = (TextView) findViewById(R.id.verification_full_name);
        addressTextView = (TextView) findViewById(R.id.verification_address);
        contactNoTextView = (TextView) findViewById(R.id.verification_contact_no);

        submitBtn = (Button) findViewById(R.id.verification_submit_btn);
        nbiBtn = (Button) findViewById(R.id.nbi_browse_btn);
        barangayClearanceBtn = (Button) findViewById(R.id.barangay_clearance_browse_btn);
        governmentIdBtn = (Button) findViewById(R.id.government_id_browse_btn);

        userInformationStorage.setSharedPreferences(getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, MODE_PRIVATE));
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
        authToken = sharedPreferences.getString(Keys.AUTH_TOKEN, "");
        loadingAlertDialog.showDialog();

        initRetrofit();
        loadCustomerData();
        submitBtn.setEnabled(true);
        nbiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(view, REQUEST_PICK_NBI_IMAGE);
            }
        });

        barangayClearanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(view, REQUEST_PICK_BC_IMAGE);
            }
        });

        governmentIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage(view, REQUEST_PICK_GOV_ID_IMAGE);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((nbi_file != null && bc_file != null && government_id_file != null)){
                    Toast.makeText(VerificationFormActivity.this, customerModel.getBirthDate(), Toast.LENGTH_LONG).show();
                    onSubmit();
                }
            }
        });
        //pickImage(nbiBtn, REQUEST_PICK_NBI_IMAGE);
    }

    void enableSubmitBtn(){
        //submitBtn.setEnabled((nbi_file != null && bc_file != null && government_id_file != null));
    }

    void pickImage(View view, final int reqCode){
        verifyStoragePermissions(this);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //String result = "";
        //String[] projection = {MediaStore.Images.Media.DATA};
//        if (requestCode == REQUEST_PICK_NBI_IMAGE && resultCode == RESULT_OK){
//            nbi_file = getFileName(data);
//            //nbi_file = result;
//            nbiBtn.setText(nbi_file);
//        }
//        if (requestCode == REQUEST_PICK_BC_IMAGE && resultCode == RESULT_OK){
//            bc_file = getFileName(data);
//            //bc_file = result;
//            barangayClearanceBtn.setText(bc_file);
//        }
//
//        if (requestCode == REQUEST_PICK_GOV_ID_IMAGE && resultCode == RESULT_OK){
//            government_id_file = getFileName(data);
//            //government_id_file = res;
//            governmentIdBtn.setText(government_id_file);
//        }
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_PICK_NBI_IMAGE:
                    nbi_file = getFileName(data);
                    //nbi_file = result;
                    nbiBtn.setText(nbi_file);
                    break;
                case REQUEST_PICK_BC_IMAGE:
                    bc_file = getFileName(data);
                    //bc_file = result;
                    barangayClearanceBtn.setText(bc_file);
                    break;
                case REQUEST_PICK_GOV_ID_IMAGE:
                    government_id_file = getFileName(data);
                    //government_id_file = res;
                    governmentIdBtn.setText(government_id_file);
                    break;
                default:
                    break;
            }
        }
    }

    private MultipartBody.Part createMultipartData(String stringFileName, String fieldName){
        File file = new File(stringFileName);
        RequestBody reqBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part formData = MultipartBody.Part.createFormData(fieldName, file.getName(), reqBody);

        return formData;
    }

    private MultipartBody.Part createReqBody(String field, String val){
        MultipartBody.Part req = MultipartBody.Part.createFormData(field, val);
        return req;
    }

    private String getFileName(Intent data){
        String result = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Uri uri = data.getData();
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            int i = cursor.getColumnIndex(projection[0]);
            result = cursor.getString(i);
        }

        return result;
    }

    void showDialogMessage(String message){
        AlertDialog alert = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alert.setTitle("Message");
        alert.show();
    }

    void onSubmit(){
        loadingAlertDialog.showDialog();
        RequestBody firstName = RequestBody.create(MediaType.parse("text/plain"), customerModel.getFirstName());
        RequestBody lastName = RequestBody.create(MediaType.parse("text/plain"), customerModel.getLastName());
        RequestBody middleName = RequestBody.create(MediaType.parse("text/plain"), customerModel.getMiddleName().isEmpty() ? "No Middle name" : customerModel.getMiddleName());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), customerModel.getAddress());
        RequestBody birthDate = RequestBody.create(MediaType.parse("text/plain"), customerModel.getBirthDate());
        RequestBody age = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(customerModel.getAge() > 0 ? customerModel.getAge() : 1));
//        MultipartBody.Part firstName = createReqBody("firstName", customerModel.getFirstName());
//        MultipartBody.Part lastName = createReqBody("lastName", customerModel.getLastName());
//        MultipartBody.Part middleName = createReqBody("middleName", customerModel.getMiddleName());
//        MultipartBody.Part address = createReqBody("address", customerModel.getAddress());
//        MultipartBody.Part birthDate = createReqBody("birthDate", customerModel.getBirthDate());
//        MultipartBody.Part age = createReqBody("age", String.valueOf(customerModel.getAge()));
        MultipartBody.Part governmentId = createMultipartData(government_id_file, "governmentId");
        MultipartBody.Part nbi = createMultipartData(nbi_file, "nbi");
        MultipartBody.Part bc = createMultipartData(bc_file, "barangayClearance");


        VerificationService verificationService = retrofit.create(VerificationService.class);
        Call<ServerResponse> result = verificationService.post(firstName, lastName,
                middleName, address, birthDate, age, nbi, bc, governmentId, "Bearer " + authToken);
        result.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.isSuccessful()){
                    showDialogMessage(response.body().getMessage());
                }else {

                    try{
                        Toast.makeText(VerificationFormActivity.this, response.errorBody().string().toString(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(VerificationFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(VerificationFormActivity.this, "Something", Toast.LENGTH_SHORT).show();
                loadingAlertDialog.dismiss();
            }
        });

    }


    void verifyStoragePermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    void initRetrofit(){
        retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    void loadCustomerData(){
        CustomerService customerService = retrofit.create(CustomerService.class);
        Call<CustomerModel> result = customerService.getInformation(headers());
        result.enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                if (response.isSuccessful()){
                    customerModel = response.body();
                    setCustomerData();
                }else {
                    Toast.makeText(VerificationFormActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                loadingAlertDialog.dismiss();
                Toast.makeText(VerificationFormActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setCustomerData(){
        fullNameTextView.setText(customerModel.getFirstName() + ", " + customerModel.getLastName());
        contactNoTextView.setText(customerModel.getContactNo());
        addressTextView.setText(customerModel.getAddress());
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
}