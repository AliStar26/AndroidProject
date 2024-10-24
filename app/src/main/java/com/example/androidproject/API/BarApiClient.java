package com.example.androidproject.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BarApiClient {
    private static final String BASE_URL = "http://10.0.2.2:5175/api/Bar/"; // Замените на URL вашего API
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
