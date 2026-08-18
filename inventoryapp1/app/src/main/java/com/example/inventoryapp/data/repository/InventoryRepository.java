package com.example.inventoryapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.inventoryapp.data.dao.InventoryDao;
import com.example.inventoryapp.data.database.InventoryDatabase;
import com.example.inventoryapp.data.entity.InventoryItem;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InventoryRepository {

    private final InventoryDao dao;
    private final LiveData<List<InventoryItem>> allItems;
    private final ExecutorService executorService;

    public InventoryRepository(Application application) {

        InventoryDatabase database =
                InventoryDatabase.getDatabase(application);

        dao = database.inventoryDao();

        allItems = dao.getAllItems();

        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<InventoryItem>> getAllItems() {
        return allItems;
    }

    public void insert(InventoryItem item) {
        executorService.execute(() -> dao.insert(item));
    }

    public void update(InventoryItem item) {
        executorService.execute(() -> dao.update(item));
    }

    public void delete(InventoryItem item) {
        executorService.execute(() -> dao.delete(item));
    }

    public void deleteAllItems() {
        executorService.execute(dao::deleteAllItems);
    }
}