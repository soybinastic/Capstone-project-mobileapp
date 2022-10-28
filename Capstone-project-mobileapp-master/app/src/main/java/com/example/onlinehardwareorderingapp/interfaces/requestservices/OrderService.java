package com.example.onlinehardwareorderingapp.interfaces.requestservices;

import com.example.onlinehardwareorderingapp.dtos.order.PostOrderDto;
import com.example.onlinehardwareorderingapp.dtos.order.UpdateOrderDto;
import com.example.onlinehardwareorderingapp.models.OrderItemModel;
import com.example.onlinehardwareorderingapp.models.OrderModel;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderService {
    @POST("api/order/post-order")
    Call<ServerResponse> placeOrder(@Body PostOrderDto order, @HeaderMap Map<String, String> headers);

    @GET("api/Order/get-customer-order-history")
    Call<ArrayList<OrderModel>> getAll(@HeaderMap Map<String, String> headers);

    @GET("api/Order/customer-order-products/{storeId}/{branchId}/{orderId}")
    Call<ArrayList<OrderItemModel>> getOrderProducts(@Path("storeId")int storeId,
                                                     @Path("branchId")int branchId,
                                                     @Path("orderId")int orderId,
                                                     @HeaderMap Map<String, String> headers);
    @PUT("/api/order/update-order/{orderId}")
    Call<ServerResponse> cancelOrder(@Path("orderId")int orderId,
                                     @Body UpdateOrderDto updateOrderDto,
                                     @HeaderMap Map<String, String> headers);
}
