package com.example.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.API.ProductApiService;
import com.example.androidproject.Adapter.ProductAdapter;
import com.example.androidproject.Models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private Button logoutButton, addProductButton;
    private RecyclerView productRecyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();
    private ProductApiService productApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        productApiService = new ProductApiService(this);

        TextView textView = findViewById(R.id.admin_message);
        textView.setText("Welcome, Admin! You have access to the Admin panel.");

        logoutButton = findViewById(R.id.logout_button);
        addProductButton = findViewById(R.id.add_product_button);

        logoutButton.setOnClickListener(v -> {
            new TokenManager(this).clearToken();
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        addProductButton.setOnClickListener(v -> openProductForm("add", -1));

        productRecyclerView = findViewById(R.id.product_recycler_view);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(productList, this::onUpdateProduct, this::onDeleteProduct);
        productRecyclerView.setAdapter(productAdapter);

        loadProducts();
    }

    private void loadProducts() {
        productApiService.getProducts(products -> {
            Log.d("Products Loaded", "Total products: " + products.size());
            productList.clear();
            productList.addAll(products);
            productAdapter.notifyDataSetChanged();

            for (Product product : products) {
                Log.d("Product", "ID: " + product.getId() + ", Name: " + product.getName() + ", Price: " + product.getPrice());
            }
        }, error -> {
            Log.e("Error", "Failed to fetch products: " + error.toString());
        });
    }

    private void onUpdateProduct(Product product) {
        Intent intent = new Intent(AdminActivity.this, ProductFormActivity.class);
        intent.putExtra("productId", product.getId());
        intent.putExtra("productName", product.getName());
        intent.putExtra("productPrice", product.getPrice());
        intent.putExtra("action", "update");
        startActivityForResult(intent, 1);
    }

    private void onDeleteProduct(Product product) {
        productApiService.deleteProduct(product.getId(), response -> {
            productList.remove(product);
            productAdapter.notifyDataSetChanged();
            Toast.makeText(AdminActivity.this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(AdminActivity.this, "Error deleting product", Toast.LENGTH_SHORT).show();
            Log.e("Delete Product", "Error: " + error.toString());
        });
    }

    private void openProductForm(String action, int productId) {
        Intent intent = new Intent(AdminActivity.this, ProductFormActivity.class);
        intent.putExtra("action", action);
        if (productId != -1) {
            intent.putExtra("product_id", productId);
        }
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadProducts();
        }
    }
}