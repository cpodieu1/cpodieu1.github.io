package com.example.inventoryapp.data.repository;

import android.app.Application;

import com.example.inventoryapp.data.dao.UserDao;
import com.example.inventoryapp.data.database.InventoryDatabase;
import com.example.inventoryapp.data.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    public interface LoginCallback {
        void onResult(User user);
    }

    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Application application) {
        InventoryDatabase database = InventoryDatabase.getDatabase(application);
        userDao = database.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void register(User user) {
        executorService.execute(() -> userDao.insert(user));
    }

    /**
     * Database authentication is performed off the main UI thread.
     */
    public void login(String username, String password, LoginCallback callback) {
        executorService.execute(() -> {
            User user = userDao.login(username, password);
            callback.onResult(user);
        });
    }
}
