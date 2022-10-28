package com.example.onlinehardwareorderingapp.interfaces.requestservices;

import com.example.onlinehardwareorderingapp.dtos.AdjustItemQuantityDto;
import com.example.onlinehardwareorderingapp.dtos.InitializeItemToCartDto;
import com.example.onlinehardwareorderingapp.models.CartDetailsModel;
import com.example.onlinehardwareorderingapp.models.CartItemModel;
import com.example.onlinehardwareorderingapp.responsemessages.ServerResponse;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartService {

    // fetch all items in cart
    @GET("api/cart/get-products-in-cart/{storeId}/{branchId}")
    Call<CartDetailsModel> getItems(@Path("storeId")int storeId, @Path("branchId")int branchId,
                                    @HeaderMap Map<String, String> headers);
    // add item to cart
    @POST("api/cart/add-to-cart")
    Call<ServerResponse> initializeItem(@HeaderMap Map<String, String> headers,
                                        @Body InitializeItemToCartDto addToCart,
                                        @Query("qty")int quantity);

    // adjust the quantity by inputting qty value (qty value must greater than zero)
    @PUT("api/cart/adjust-quantity/{storeId}/{cartId}/{productId}/{branchId}")
    Call<ServerResponse> adjustQuantity(@Path("storeId")int storeId, @Path("cartId")int cartId,
                                        @Path("productId")int productId, @Path("branchId")int branchId,
                                        @HeaderMap Map<String, String> headers,
                                        @Body AdjustItemQuantityDto adjustItemQuantityDto);
    // increment by one the quantity of item in cart
    @PUT("api/cart/increment-quantity/{storeId}/{cartId}/{productId}/{branchId}")
    Call<ServerResponse> incrementQuantity(@Path("storeId")int storeId, @Path("cartId")int cartId,
                                        @Path("productId")int productId, @Path("branchId")int branchId,
                                        @HeaderMap Map<String, String> headers);
    // decrement by one the quantity of item in cart
    @PUT("api/cart/decrement-quantity/{storeId}/{cartId}/{productId}/{branchId}")
    Call<ServerResponse> decrementQuantity(@Path("storeId")int storeId, @Path("cartId")int cartId,
                                           @Path("productId")int productId, @Path("branchId")int branchId,
                                           @HeaderMap Map<String, String> headers);
    // remove an item from cart
    @DELETE("api/cart/remove-to-cart/{storeId}/{cartId}/{productId}/{branchId}")
    Call<ServerResponse> remove(@Path("storeId")int storeId, @Path("cartId")int cartId, @Path("productId")int productId,
                                @Path("branchId")int branchId, @HeaderMap Map<String, String> headers);
}
