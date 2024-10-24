package com.example.androidproject.API;

import com.example.androidproject.Models.OrderItem;
import com.example.androidproject.Models.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BarApiService {
    @POST("add-order/{keyNumber}")
    Call<ResponseBody> addOrder(@Path("keyNumber") int keyNumber, @Body List<OrderItem> orderItems);

    @GET("products")
    Call<List<Product>> getAllProducts();
}
