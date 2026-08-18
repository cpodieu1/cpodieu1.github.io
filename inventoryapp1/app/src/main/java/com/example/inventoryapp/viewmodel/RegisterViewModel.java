package com.example.inventoryapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.inventoryapp.data.entity.User;
import com.example.inventoryapp.data.repository.UserRepository;

public class RegisterViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void register(User user) {
        repository.register(user);
    }
}