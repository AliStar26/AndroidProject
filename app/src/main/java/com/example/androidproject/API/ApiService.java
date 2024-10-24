package com.example.androidproject.API;

import com.example.androidproject.Models.JwtResponse;
import com.example.androidproject.Models.LoginRequest;
import com.example.androidproject.Models.RegisterRequest;
import com.example.androidproject.Models.RoleResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/Account/register")
    Call<Void> registerUser(@Body RegisterRequest registerRequest);

    @POST("api/Account/login")
    Call<JwtResponse> loginUser(@Body LoginRequest loginRequest);

}
