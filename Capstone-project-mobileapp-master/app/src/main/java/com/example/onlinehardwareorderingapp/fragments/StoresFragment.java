package com.example.onlinehardwareorderingapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinehardwareorderingapp.R;
import com.example.onlinehardwareorderingapp.RetrofitService;
import com.example.onlinehardwareorderingapp.adapters.BranchAdapter;
import com.example.onlinehardwareorderingapp.fragments.bottomsheets.BottomSheetMap;
import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.helper.LoadingAlertDialog;
import com.example.onlinehardwareorderingapp.helper.UserInformationStorage;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.CallBackClickListener;
import com.example.onlinehardwareorderingapp.interfaces.callbacks.Invoker;
import com.example.onlinehardwareorderingapp.interfaces.requestservices.BranchService;
import com.example.onlinehardwareorderingapp.models.BranchModel;
import com.example.onlinehardwareorderingapp.models.ui.Branch;
import com.example.onlinehardwareorderingapp.models.ui.Search;

import java.util.ArrayList;
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
 * Use the {@link StoresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoresFragment extends Fragment implements Invoker<BranchModel, String> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserInformationStorage userInformationStorage = UserInformationStorage.getInstance();
    private ArrayList<BranchModel> branches;
    RecyclerView storeRecyclerView;
    private CallBackClickListener clickListener;
    private RetrofitService retrofitService;
    private BottomSheetMap map = null;
    private double adjustedKm = 5;
    private TextView messageResult;
    //BranchAdapter branchAdapter;
    public StoresFragment() {
        // Required empty public constructor
    }
    public StoresFragment(ArrayList<BranchModel> branches, CallBackClickListener listener, RetrofitService retrofitService){
        this.branches = branches;
        clickListener = listener;
        this.retrofitService = retrofitService;
    }
    public StoresFragment(CallBackClickListener listener, RetrofitService retrofitService){
        this.branches = branches;
        clickListener = listener;
        this.retrofitService = retrofitService;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoresFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoresFragment newInstance(String param1, String param2) {
        StoresFragment fragment = new StoresFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stores, container, false);
        userInformationStorage.setSharedPreferences(getActivity().getSharedPreferences(UserInformationStorage.SHARED_PREF_NAME, Context.MODE_PRIVATE));
        storeRecyclerView = (RecyclerView) view.findViewById(R.id.storesRecyclerView);
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //storeRecyclerView.setAdapter(new BranchAdapter(branches, storeRecyclerView, this));
        ImageView searchIcon = view.findViewById(R.id.search_store_icon);
        Button viewInMap = (Button) view.findViewById(R.id.view_as_map);
        Button findBtn = (Button) view.findViewById(R.id.find_btn);
        EditText km = (EditText) view.findViewById(R.id.et_km);
        messageResult = view.findViewById(R.id.stores_res);

        km.setText(String.valueOf(adjustedKm));
        viewInMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "" + branches.size(), Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = userInformationStorage.getSharedPreferences();
                double lat = Double.parseDouble(sharedPreferences.getString(Keys.LATITUDE, "").isEmpty() ? "0" : sharedPreferences.getString(Keys.LATITUDE, ""));
                double lng = Double.parseDouble(sharedPreferences.getString(Keys.LONGITUDE, "").isEmpty() ? "0" : sharedPreferences.getString(Keys.LONGITUDE, ""));
                map = new BottomSheetMap(branches.size(), lat, lng, branches);
                map.show(getParentFragmentManager(), map.getTag());
            }
        });
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (km.getText().toString().isEmpty()){
                    adjustedKm = 5;
                    km.setText(String.valueOf(adjustedKm));
                    km.setError("Please put some value here.");
                }else {
                    adjustedKm = Double.parseDouble(km.getText().toString());
                    km.setError(null);
                }

                init();
            }
        });
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(new Search((km.getText().toString().isEmpty() ? 5 : Double.parseDouble(km.getText().toString()))), "");
            }
        });
        init();
        return view;
    }

    @Override
    public void onClick(BranchModel data, String s) {
        Toast.makeText(getContext(), data.getName()+"..", Toast.LENGTH_SHORT).show();
        clickListener.onClick(new Branch(data.getId(), data.getName(), data.getAddress(), data.getHardwareStoreId()), "");
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

    private void init(){
        LoadingAlertDialog loadingAlertDialog = new LoadingAlertDialog(getActivity());
        loadingAlertDialog.showDialog();
        Retrofit retrofit = retrofitService.getRetrofitBuilder()
                .baseUrl(RetrofitService.API_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BranchService branchService = retrofit.create(BranchService.class);
        Call<ArrayList<BranchModel>> result = branchService.getBranches(headers(), adjustedKm);
        result.enqueue(new Callback<ArrayList<BranchModel>>() {
            @Override
            public void onResponse(Call<ArrayList<BranchModel>> call, Response<ArrayList<BranchModel>> response) {
                loadingAlertDialog.dismiss();
                if (response.isSuccessful()){
                    branches = response.body();
                    messageResult.setVisibility(response.body().size() > 0 ? View.GONE : View.VISIBLE);
                    storeRecyclerView.setAdapter(new BranchAdapter(branches, storeRecyclerView, StoresFragment.this));
                }else {
                    messageResult.setVisibility(response.body().size() > 0 ? View.GONE : View.VISIBLE);
                    messageResult.setText("Please try again!!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<BranchModel>> call, Throwable t) {
                loadingAlertDialog.dismiss();
            }
        });
    }
}