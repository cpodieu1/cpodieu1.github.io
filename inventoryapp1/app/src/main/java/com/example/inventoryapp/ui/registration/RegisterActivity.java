package com.example.inventoryapp.ui.registration;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventoryapp.R;
import com.example.inventoryapp.data.entity.User;
import com.example.inventoryapp.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameET;
    private EditText passwordET;
    private Button createBtn;

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        createBtn = findViewById(R.id.createBtn);

        registerViewModel = new ViewModelProvider(this)
                .get(RegisterViewModel.class);

        createBtn.setOnClickListener(v -> {

            String username = usernameET.getText().toString().trim();
            String password = passwordET.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        this,
                        "Please fill in all fields.",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            User user = new User(username, password);

            registerViewModel.register(user);

            Toast.makeText(
                    this,
                    "User Registered Successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });
    }
}