package com.example.onlinehardwareorderingapp.interfaces.requestservices;

import com.example.onlinehardwareorderingapp.dtos.CustomerCreateAccountDto;
import com.example.onlinehardwareorderingapp.dtos.CustomerUpdateInformationDto;
import com.example.onlinehardwareorderingapp.models.CustomerModel;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface CustomerService {
    @POST("api/customer/register-customer")
    Call<ServerResponse> register(@Body CustomerCreateAccountDto customerInfo);
    @GET("api/customer/get-customer-info")
    Call<CustomerModel> getInformation(@HeaderMap Map<String, String> headers);
    @PUT("api/Customer/update")
    Call<ServerResponse> updateInfo(@Body CustomerUpdateInformationDto updateInformationDto, @HeaderMap Map<String, String> headers);
}
