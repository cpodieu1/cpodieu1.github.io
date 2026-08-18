package com.example.inventoryapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.inventoryapp.data.entity.User;
import com.example.inventoryapp.data.repository.UserRepository;

public class LoginViewModel extends AndroidViewModel {

    public interface LoginResultListener {
        void onLoginResult(User user);
    }

    private final UserRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void login(String username, String password, LoginResultListener listener) {
        repository.login(username, password, listener::onLoginResult);
    }
}
