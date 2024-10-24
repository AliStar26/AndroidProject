/*package com.example.androidproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidproject.API.ApiClient;
import com.example.androidproject.Models.RegisterRequest;
import com.example.androidproject.Repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);

        authRepository = new AuthRepository();

        registerButton.setOnClickListener(v -> register());
    }

    private void register() {
        String username = usernameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        RegisterRequest registerRequest = new RegisterRequest(username,  password);
        authRepository.registerUser(registerRequest, this); // Использование AuthRepository для регистрации
    }
}
*/