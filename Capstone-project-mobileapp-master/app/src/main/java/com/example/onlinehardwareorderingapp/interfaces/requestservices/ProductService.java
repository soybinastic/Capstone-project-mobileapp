package com.example.onlinehardwareorderingapp.interfaces.requestservices;

import com.example.onlinehardwareorderingapp.models.ProductModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {
    @GET("api/Product/get-hardwareproducts/{branchId}")
    Call<ArrayList<ProductModel>> getAll(@Path("branchId")int branchId, @Query("search")String search);
    @GET("api/Product/get-hardwareproduct/{branchId}/{hardwareProductId}")
    Call<ProductModel> get(@Path("branchId")int branchId, @Path("hardwareProductId")int hardwareProductId);
    @GET("api/product/get-hardwareproduct-by-category/{branchId}/{categoryId}")
    Call<ArrayList<ProductModel>> getByCategory(@Path("branchId")int branchId, @Path("categoryId")int categoryId);
    @GET("api/Product/get-hardwareproduct/{branchId}/{productId}")
    Call<ProductModel> getOne(@Path("branchId")int branchId, @Path("productId")int productId);
}
