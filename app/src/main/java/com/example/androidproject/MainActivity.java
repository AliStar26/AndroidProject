package com.example.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            String role = extractRoleFromToken(token);

            if (role.equals("Admin")) {
                navigateToActivity(AdminActivity.class);
            } else if (role.equals("Reception")) {
                navigateToActivity(ReceptionActivity.class);
            } else if (role.equals("Bar")) {
                navigateToActivity(BarActivity.class);
            } else {
                Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String extractRoleFromToken(String token) {
        try {
            String[] splitToken = token.split("\\.");
            String base64Payload = splitToken[1]; // Получаем Payload
            byte[] decodedBytes = Base64.decode(base64Payload, Base64.DEFAULT);
            String decodedPayload = new String(decodedBytes);

            JSONObject jsonObject = new JSONObject(decodedPayload);

            String role = jsonObject.getString("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(MainActivity.this, activityClass);
        startActivity(intent);
        finish();
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("token");
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
