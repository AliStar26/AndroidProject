package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.API.ReceptionApiService;
import com.example.androidproject.API.ReceptionClient;
import com.example.androidproject.Adapter.ClientsAdapter;
import com.example.androidproject.Models.ClientDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceptionActivity extends AppCompatActivity {
    private ListView clientsListView;
    private Button registerClientButton;
    private EditText keyNumberEditText;
    private EditText nameEditText;
    private EditText phoneEditText;
    private Button logoutButton;
    private TokenManager tokenManager;
    private ClientsAdapter clientsAdapter;
    private List<ClientDto> clientsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);

        clientsListView = findViewById(R.id.clientsListView);
        registerClientButton = findViewById(R.id.registerClientButton);
        keyNumberEditText = findViewById(R.id.keyNumberEditText);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);

        clientsList = new ArrayList<>();
        clientsAdapter = new ClientsAdapter(this, clientsList, this::getActiveClients); // Передаем метод обновления списка
        clientsListView.setAdapter(clientsAdapter);

        // Получить список активных клиентов при запуске
        getActiveClients();

        registerClientButton.setOnClickListener(v -> registerClient());

        clientsListView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("ReceptionActivity", "Item clicked: " + position);
            ClientDto selectedClient = clientsList.get(position);
            Log.d("ReceptionActivity", "Selected Client ID: " + selectedClient.getId());
            Log.e("ReceptionActivity", "keyNumber: " + selectedClient.getKeyNumber());
            Intent intent = new Intent(ReceptionActivity.this, ClientDetailActivity.class);
            intent.putExtra("keyNumber", selectedClient.getKeyNumber());

            startActivity(intent);
        });
        Log.d("ReceptionActivity", "Number of clients: " + clientsList.size());

        tokenManager = new TokenManager(this);
        logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(v -> {
            tokenManager.clearToken();
            Intent intent = new Intent(ReceptionActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void getActiveClients() {
        ReceptionApiService apiService = ReceptionClient.getClient().create(ReceptionApiService.class);
        Call<List<ClientDto>> call = apiService.getActiveClients();
        call.enqueue(new Callback<List<ClientDto>>() {
            @Override
            public void onResponse(Call<List<ClientDto>> call, Response<List<ClientDto>> response) {
                Log.d("ReceptionActivity", "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    clientsList.clear();
                    clientsList.addAll(response.body());
                    Log.d("ReceptionActivity", "Loaded clients: " + response.body().size()); // Добавьте этот лог
                    clientsAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ReceptionActivity.this, "Ошибка при загрузке клиентов: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ClientDto>> call, Throwable t) {
                Toast.makeText(ReceptionActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerClient() {
        String keyNumber = keyNumberEditText.getText().toString();
        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        if (keyNumber.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля.", Toast.LENGTH_SHORT).show();
            return;
        }

        ClientDto clientDto = new ClientDto(name, phone, Integer.parseInt(keyNumber));

        ReceptionApiService apiService = ReceptionClient.getClient().create(ReceptionApiService.class);

        Call<ResponseBody> call = apiService.registerClient(clientDto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReceptionActivity.this, "Клиент успешно зарегистрирован.", Toast.LENGTH_SHORT).show();
                    getActiveClients(); // Обновляем список активных клиентов
                    clearInputFields(); // Очищаем поля ввода
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Неизвестная ошибка";
                        Log.e("ReceptionActivity", "Failed to register client: " + errorBody);
                        Toast.makeText(ReceptionActivity.this, "Ошибка: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ReceptionActivity", "Failed to register client: " + t.getMessage());
                Toast.makeText(ReceptionActivity.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearInputFields() {
        keyNumberEditText.setText("");
        nameEditText.setText("");
        phoneEditText.setText("");
    }
}