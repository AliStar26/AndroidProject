package com.example.androidproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.API.BarApiClient;
import com.example.androidproject.API.BarApiService;
import com.example.androidproject.Adapter.CartAdapter;
import com.example.androidproject.Models.OrderItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private List<OrderItem> cartItems;
    private EditText keyNumberEditText;
    private Button addOrderButton;
    private CartAdapter cartAdapter;
    private TextView totalAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItems, this::updateTotalAmount);
        recyclerView.setAdapter(cartAdapter);

        keyNumberEditText = findViewById(R.id.key_number_edit_text);
        addOrderButton = findViewById(R.id.add_order_button);
        totalAmountTextView = findViewById(R.id.total_amount_text_view);

        updateTotalAmount();  // Рассчитать общую сумму

        addOrderButton.setOnClickListener(v -> {
            String keyNumber = keyNumberEditText.getText().toString();
            if (!keyNumber.isEmpty()) {
                sendOrderToServer(Integer.parseInt(keyNumber));
            } else {
                Toast.makeText(this, "Введите номер ключа", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalAmount() {
        double totalAmount = 0;
        for (OrderItem item : cartItems) {
            totalAmount += item.getPrice() * item.getCount();  // Пересчитать с учетом количества
        }
        totalAmountTextView.setText("Общая сумма: " + totalAmount + " тенге");
    }

    private void sendOrderToServer(int keyNumber) {
        BarApiService apiService = BarApiClient.getClient().create(BarApiService.class);
        Call<ResponseBody> call = apiService.addOrder(keyNumber, cartItems);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Заказ отправлен", Toast.LENGTH_SHORT).show();
                    clearCart();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(CartActivity.this, "Ошибка при отправке заказа", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearCart() {
        if (!cartItems.isEmpty()) {
            cartItems.clear();
            cartAdapter.notifyDataSetChanged();
            updateTotalAmount();
            Toast.makeText(this, "Корзина очищена", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Корзина уже пуста", Toast.LENGTH_SHORT).show();
        }
    }
}