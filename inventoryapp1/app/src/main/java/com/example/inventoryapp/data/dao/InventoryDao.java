package com.example.inventoryapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inventoryapp.data.entity.InventoryItem;

import java.util.List;

@Dao
public interface InventoryDao {

    @Insert
    void insert(InventoryItem item);

    @Update
    void update(InventoryItem item);

    @Delete
    void delete(InventoryItem item);

    @Query("SELECT * FROM inventory ORDER BY name COLLATE NOCASE ASC")
    LiveData<List<InventoryItem>> getAllItems();

    @Query("DELETE FROM inventory")
    void deleteAllItems();
}
