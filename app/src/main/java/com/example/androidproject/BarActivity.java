package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.API.BarApiClient;
import com.example.androidproject.API.BarApiService;
import com.example.androidproject.Adapter.BarAdapter;
import com.example.androidproject.Adapter.ProductAdapter;
import com.example.androidproject.Models.OrderItem;
import com.example.androidproject.Models.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BarAdapter adapter;
    private List<Product> productsList = new ArrayList<>();
    private List<OrderItem> cartItems = new ArrayList<>();
    private Button openCartButton;
    private ActivityResultLauncher<Intent> cartActivityLauncher;
    private Button logoutButton;
    private TokenManager tokenManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BarAdapter(productsList, this::addToCart);
        recyclerView.setAdapter(adapter);

        openCartButton = findViewById(R.id.open_cart_button);
        openCartButton.setOnClickListener(v -> openCart());

        loadProducts();

        tokenManager = new TokenManager(this);


        logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(v -> {
            tokenManager.clearToken();
            Intent intent = new Intent(BarActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadProducts() {
        BarApiService apiService = BarApiClient.getClient().create(BarApiService.class);
        Call<List<Product>> call = apiService.getAllProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productsList.clear();
                    productsList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(BarActivity.this, "Ошибка загрузки продуктов", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(BarActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart(Product product) {
        OrderItem orderItem = new OrderItem(product.getName(), 1, product.getPrice());
        cartItems.add(orderItem);
        Toast.makeText(this, "Продукт добавлен в корзину", Toast.LENGTH_SHORT).show();
    }

    private void openCart() {
        Intent intent = new Intent(this, CartActivity.class);
        intent.putParcelableArrayListExtra("cartItems", new ArrayList<>(cartItems));
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cartItems.clear();
            Toast.makeText(this, "Корзина очищена после успешного заказа", Toast.LENGTH_SHORT).show();
        }
    }
}