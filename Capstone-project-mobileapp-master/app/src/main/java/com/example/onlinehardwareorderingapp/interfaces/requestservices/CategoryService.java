package com.example.onlinehardwareorderingapp.interfaces.requestservices;

import com.example.onlinehardwareorderingapp.models.CategoryModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {
    @GET("api/category/get-categories/{storeId}")
    Call<ArrayList<CategoryModel>> getAll(@Path("storeId")int storeId);
}
