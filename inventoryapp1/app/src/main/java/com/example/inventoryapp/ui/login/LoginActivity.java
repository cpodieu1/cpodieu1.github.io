package com.example.inventoryapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryapp.R;
import com.example.inventoryapp.data.entity.User;
import com.example.inventoryapp.ui.inventory.InventoryActivity;
import com.example.inventoryapp.ui.registration.RegisterActivity;
import com.example.inventoryapp.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText passwordET;
    private Button loginBtn;
    private Button registerBtn;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginBtn.setOnClickListener(v -> authenticateUser());

        registerBtn.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }

    private void authenticateUser() {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,
                    "Please enter username and password.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        loginBtn.setEnabled(false);

        loginViewModel.login(username, password, this::handleLoginResult);
    }

    private void handleLoginResult(User user) {
        runOnUiThread(() -> {
            loginBtn.setEnabled(true);

            if (user != null) {
                Toast.makeText(this,
                        "Login successful.",
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(
                        LoginActivity.this,
                        InventoryActivity.class
                ));
                finish();
            } else {
                Toast.makeText(this,
                        "Invalid username or password.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
