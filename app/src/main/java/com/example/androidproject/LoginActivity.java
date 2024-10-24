package com.example.androidproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.API.ApiClient;
import com.example.androidproject.Models.LoginRequest;
import com.example.androidproject.Models.LoginResponse;
import com.example.androidproject.Models.RoleResponse;
import com.example.androidproject.Repository.AuthRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        authRepository = new AuthRepository();

        loginButton.setOnClickListener(v -> login());
    }

    private void login() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        LoginRequest loginRequest = new LoginRequest(username, password);
        authRepository.loginUser(loginRequest, this, token -> {
            saveToken(token);
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
        });
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(LoginActivity.this, activityClass);
        startActivity(intent);
        finish();
    }

    private void saveToken(String token) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("token", token);
        editor.apply();
    }

    private String extractRoleFromToken(String token) {
        try {
            String[] splitToken = token.split("\\.");
            String base64Payload = splitToken[1];
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
}
