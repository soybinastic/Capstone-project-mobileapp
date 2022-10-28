package com.example.onlinehardwareorderingapp.interfaces.requestservices;

import com.example.onlinehardwareorderingapp.dtos.UserLoginDto;
import com.example.onlinehardwareorderingapp.models.AuthResponseModel;
import com.example.onlinehardwareorderingapp.models.WeatherForecast;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface AuthService {

    @POST("api/account/login")
    Call<AuthResponseModel> login(@Body UserLoginDto credentials);

    @POST("api/account/logout")
    Call<ServerResponse> logout(@HeaderMap Map<String, String> headers);

    @GET("WeatherForecast")
    Call<List<WeatherForecast>> Test();
}
