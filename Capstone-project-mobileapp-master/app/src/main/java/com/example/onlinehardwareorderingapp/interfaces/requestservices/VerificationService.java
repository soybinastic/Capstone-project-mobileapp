package com.example.onlinehardwareorderingapp.interfaces.requestservices;

import com.example.onlinehardwareorderingapp.helper.Keys;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VerificationService {

    @Multipart
    @POST("api/Verification/post")
    Call<ServerResponse> post(@Part("firstName")RequestBody firstName,
                            @Part("lastName")RequestBody lastName,
                            @Part("middleName")RequestBody middleName,
                            @Part("address")RequestBody address,
                            @Part("birthDate")RequestBody birthDate,
                            @Part("age")RequestBody age,
                            @Part MultipartBody.Part nbiFile,
                            @Part MultipartBody.Part barangayClearanceFile,
                            @Part MultipartBody.Part governmentIdFile,
                            @Header(Keys.AUTHORIZATION)String authToken);
//    @Multipart
//    @POST("api/Verification/post")
//    Call<ResponseBody> post(@Part MultipartBody.Part firstName,
//                            @Part MultipartBody.Part lastName,
//                            @Part MultipartBody.Part middleName,
//                            @Part MultipartBody.Part address,
//                            @Part MultipartBody.Part birthDate,
//                            @Part MultipartBody.Part age,
//                            @Part MultipartBody.Part nbiFile,
//                            @Part MultipartBody.Part barangayClearanceFile,
//                            @Part MultipartBody.Part governmentIdFile,
//                            @Header(Keys.AUTHORIZATION)String authToken);
}
