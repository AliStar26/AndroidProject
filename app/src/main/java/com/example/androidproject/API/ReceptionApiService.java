package com.example.androidproject.API;

import com.example.androidproject.Models.Client;
import com.example.androidproject.Models.ClientDto;
import com.example.androidproject.Models.ClientOrdersResponse;
import com.example.androidproject.Models.EndVisitResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReceptionApiService {
    @POST("Reception/register")
    Call<ResponseBody> registerClient(@Body ClientDto clientDto);

    @GET("Reception/active-clients")
    Call<List<ClientDto>> getActiveClients();

    @POST("Reception/end-visit/{keyNumber}")
    Call<Void> endVisit(@Path("keyNumber") int keyNumber);

    @GET("Reception/get-client/{keyNumber}")
    Call<ClientOrdersResponse> getClientOrders(@Path("keyNumber") int keyNumber);
}
