package com.example.androidproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.androidproject.API.ReceptionApiService;
import com.example.androidproject.API.ReceptionClient;
import com.example.androidproject.Models.ClientOrdersResponse;
import com.example.androidproject.Models.Order;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientDetailActivity extends AppCompatActivity {
    private static final String TAG = "ClientOrdersActivity";
    private ReceptionApiService receptionApiService;

    private TextView messageTextView;
    private TextView totalAmountTextView;
    private ListView ordersListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail);

        messageTextView = findViewById(R.id.messageTextViews);
        totalAmountTextView = findViewById(R.id.totalAmountTextViews);
        ordersListView = findViewById(R.id.ordersListView);

        receptionApiService = ReceptionClient.getClient().create(ReceptionApiService.class);

        int clientId = getIntent().getIntExtra("clientId", -1);
        if (clientId != -1) {
            fetchClientOrders(clientId);
        } else {
            Log.e(TAG, "Не удалось получить clientId");
        }
    }

    private void fetchClientOrders(int clientId) {
        Call<ClientOrdersResponse> call = receptionApiService.getClientOrders(clientId);
        call.enqueue(new Callback<ClientOrdersResponse>() {
            @Override
            public void onResponse(Call<ClientOrdersResponse> call, Response<ClientOrdersResponse> response) {
                if (response.isSuccessful()) {
                    ClientOrdersResponse ordersResponse = response.body();
                    if (ordersResponse != null) {
                        messageTextView.setText(ordersResponse.getMessage());
                        totalAmountTextView.setText("Итоговый счёт: " + ordersResponse.getTotalAmount());
                        // Здесь можно добавить адаптер для отображения заказов в ListView
                    } else {
                        Log.e(TAG, "Ответ пустой");
                    }
                } else {
                    Log.e(TAG, "Ошибка: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ClientOrdersResponse> call, Throwable t) {
                Log.e(TAG, "Ошибка: " + t.getMessage());
            }
        });
    }
}