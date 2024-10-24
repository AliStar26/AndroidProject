package com.example.androidproject.Repository;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.androidproject.API.ApiService;
import com.example.androidproject.API.ApiClient;
import com.example.androidproject.LoginActivity;
import com.example.androidproject.Models.JwtResponse;
import com.example.androidproject.Models.LoginRequest;
import com.example.androidproject.Models.RegisterRequest;
import com.example.androidproject.Models.RoleResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;

    public AuthRepository() {
        String BASE_URL = "http://10.0.2.2:5175/";
        apiService = ApiClient.getApiService(BASE_URL);
    }

    public void loginUser(LoginRequest loginRequest, Context context, OnLoginSuccess listener) {
        Call<JwtResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<JwtResponse>() {
            @Override
            public void onResponse(Call<JwtResponse> call, Response<JwtResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body().getToken());
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JwtResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnLoginSuccess {
        void onSuccess(String token);
    }

}
