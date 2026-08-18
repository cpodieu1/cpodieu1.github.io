package com.example.inventoryapp.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.inventoryapp.data.dao.InventoryDao;
import com.example.inventoryapp.data.dao.UserDao;
import com.example.inventoryapp.data.entity.InventoryItem;
import com.example.inventoryapp.data.entity.User;

@Database(
        entities = {
                User.class,
                InventoryItem.class
        },
        version = 2,
        exportSchema = false
)
public abstract class InventoryDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract InventoryDao inventoryDao();

    private static volatile InventoryDatabase INSTANCE;

    /**
     * Migration 1 -> 2 adds a category while preserving existing inventory.
     * Existing rows receive the value "General" instead of being deleted.
     */
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "ALTER TABLE inventory ADD COLUMN category TEXT NOT NULL DEFAULT 'General'"
            );
        }
    };

    public static InventoryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (InventoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    InventoryDatabase.class,
                                    "inventory_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
