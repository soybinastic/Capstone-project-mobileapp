package com.example.onlinehardwareorderingapp.interfaces.requestservices;

import com.example.onlinehardwareorderingapp.models.BranchModel;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface BranchService {
    @GET("api/branch/all-branches")
    Call<ArrayList<BranchModel>> getBranches(@HeaderMap Map<String, String> headers, @Query("adjusted_km")double adjusted_km);

    @GET("api/branch/search")
    Call<ArrayList<BranchModel>> search(@HeaderMap Map<String, String> headers, @Query("search")String search, @Query("adjusted_km")double adjusted_km);
}
