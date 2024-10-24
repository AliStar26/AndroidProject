package com.example.androidproject.API;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.Models.Product;
import com.example.androidproject.TokenManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductApiService {
    private static final String BASE_URL = "http://10.0.2.2:5175/api/admin/"; // Замените на ваш URL
    private final RequestQueue queue;
    private final TokenManager tokenManager;

    public ProductApiService(Context context) {
        queue = Volley.newRequestQueue(context);
        tokenManager = new TokenManager(context);
    }
    public void getProducts(Response.Listener<List<Product>> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "products";
        Log.d("API URL", "Fetching products from: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Product> products = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject productJson = response.getJSONObject(i);
                            Product product = new Product(
                                    productJson.getInt("id"),
                                    productJson.getString("name"),
                                    productJson.getDouble("price"),
                                    productJson.optString("imagePath", null)
                            );
                            products.add(product);
                        } catch (JSONException e) {
                            Log.e("JSON Error", "Error parsing product: " + e.getMessage());
                        }
                    }
                    listener.onResponse(products);
                },
                error -> {
                    Log.e("API Error", "Error fetching products: " + error.toString());
                    errorListener.onErrorResponse(error);
                });

        queue.add(jsonArrayRequest);
    }
    public void addProduct(String name, double price, String imagePath, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "add-product";
        JSONObject product = new JSONObject();
        try {
            product.put("name", name);
            product.put("price", price);
            product.put("imagePath", imagePath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, product, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + tokenManager.getToken());
                return headers;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        request.setRetryPolicy(policy);

        queue.add(request);
    }

    public void updateProduct(int id, String name, double price, String imagePath, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "update-product";
        JSONObject product = new JSONObject();
        try {
            product.put("id", id);
            product.put("name", name);
            product.put("price", price);
            product.put("imagePath", imagePath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, product, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + tokenManager.getToken());
                return headers;
            }
        };
        queue.add(request);
    }

    public void deleteProduct(int id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        String url = BASE_URL + "delete-product/" + id;
        StringRequest request = new StringRequest(Request.Method.DELETE, url, listener, errorListener) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + tokenManager.getToken());
                return headers;
            }
        };
        queue.add(request);
    }

}
