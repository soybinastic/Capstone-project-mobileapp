package com.example.onlinehardwareorderingapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.adapters.AccountOptionAdapter;
import com.example.onlinehardwareorderingapp.helper.AccountOption;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.BaseModel;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.CallBackClickListener;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.CustomerService;
import com.example.onlinehardwareorderingapp.models.CustomerModel;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment implements Invoker<AccountOption,String> {

    UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    RetrofitService retrofitService = RetrofitService.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<AccountOption> options;
    private RecyclerView accountOptionsRecyclerView;
    private CallBackClickListener callback;
    public AccountFragment() {
        // Required empty public constructor
    }
    public AccountFragment(ArrayList<AccountOption> options, CallBackClickListener callback){
        this.options = options;
        this.callback = callback;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void loadCustomerData(String token){
        LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(getActivity());
        loadingAlertDialog.showDialog();
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> headers = new HashMap<>();
        headers.put(Keys.CONTENT_TYPE, "application/json");
        headers.put(Keys.AUTHORIZATION, "Bearer " + token);
        CustomerService customerService = retrofit.create(CustomerService.class);
        Call<CustomerModel> result = customerService.getInformation(headers);
        result.enqueue(new Callback<CustomerModel>() {
            @Override
            public void onResponse(Call<CustomerModel> call, Response<CustomerModel> response) {
                if (response.isSuccessful()){
                    verifyBtn.setVisibility(response.body().isVerified() ? View.GONE : View.VISIBLE);
                    fullName.setText(response.body().getFirstName() + " " + response.body().getLastName());
                }else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
                loadingAlertDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CustomerModel> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                loadingAlertDialog.dismiss();
            }
        });
    }
    ConstraintLayout verifyBtn;
    TextView fullName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        userInformationStorage.setSharedPreferences(getActivity().getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, Context.MODE_PRIVATE));
        SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
        String token = sharedPreferences.getString(Keys.TOKEN_EXPIRATION, "");
        String authToken = sharedPreferences.getString(Keys.AUTH_TOKEN, "");
        accountOptionsRecyclerView = (RecyclerView) view.findViewById(R.id.accountOptions);
        verifyBtn = (ConstraintLayout) view.findViewById(R.id.verifyAccountBtn);
        fullName = (TextView) view.findViewById(R.id.userFullName);
        accountOptionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        accountOptionsRecyclerView.setAdapter(new AccountOptionAdapter(options, accountOptionsRecyclerView, this));

        loadCustomerData(authToken);
        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClick(new ClickVerifyButton(), "");
            }
        });

        return view;
    }

    private Date stringToDate(String stringDate){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            Date date = formatter.parse(stringDate);
            return date;
        }catch(Exception ex){
            return null;
        }
    }

    @Override
    public void onClick(AccountOption data, String s) {
        //Toast.makeText(getContext(), data.getTitle(), Toast.LENGTH_LONG).show();
        callback.onClick(data, s);
    }

    public static class ClickVerifyButton implements BaseModel{ }
}