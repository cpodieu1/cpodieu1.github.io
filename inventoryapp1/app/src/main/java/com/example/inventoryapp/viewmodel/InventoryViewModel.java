package com.example.inventoryapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inventoryapp.data.entity.InventoryItem;
import com.example.inventoryapp.data.repository.InventoryRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class InventoryViewModel extends AndroidViewModel {

    public static final String SORT_NAME = "Name";
    public static final String SORT_QUANTITY_LOW = "Quantity: Low to High";
    public static final String SORT_QUANTITY_HIGH = "Quantity: High to Low";

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final InventoryRepository repository;
    private final LiveData<List<InventoryItem>> allItems;
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<String> categoryFilter = new MutableLiveData<>("All");
    private final MutableLiveData<String> sortOption = new MutableLiveData<>(SORT_NAME);
    private final MutableLiveData<Boolean> lowStockOnly = new MutableLiveData<>(false);

    private final MediatorLiveData<List<InventoryItem>> displayedItems = new MediatorLiveData<>();

    public InventoryViewModel(@NonNull Application application) {
        super(application);

        repository = new InventoryRepository(application);
        allItems = repository.getAllItems();

        displayedItems.addSource(allItems, items -> applyFiltersAndSort());
        displayedItems.addSource(searchQuery, value -> applyFiltersAndSort());
        displayedItems.addSource(categoryFilter, value -> applyFiltersAndSort());
        displayedItems.addSource(sortOption, value -> applyFiltersAndSort());
        displayedItems.addSource(lowStockOnly, value -> applyFiltersAndSort());
    }

    public LiveData<List<InventoryItem>> getAllItems() {
        return allItems;
    }

    public LiveData<List<InventoryItem>> getDisplayedItems() {
        return displayedItems;
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query == null ? "" : query.trim());
    }

    public void setCategoryFilter(String category) {
        categoryFilter.setValue(category == null ? "All" : category);
    }

    public void setSortOption(String option) {
        sortOption.setValue(option == null ? SORT_NAME : option);
    }

    public void setLowStockOnly(boolean enabled) {
        lowStockOnly.setValue(enabled);
    }

    private void applyFiltersAndSort() {
        List<InventoryItem> source = allItems.getValue();
        if (source == null) {
            displayedItems.setValue(new ArrayList<>());
            return;
        }

        String query = searchQuery.getValue() == null
                ? ""
                : searchQuery.getValue().toLowerCase(Locale.US);
        String selectedCategory = categoryFilter.getValue() == null
                ? "All"
                : categoryFilter.getValue();
        boolean lowStock = Boolean.TRUE.equals(lowStockOnly.getValue());
        String selectedSort = sortOption.getValue() == null
                ? SORT_NAME
                : sortOption.getValue();

        List<InventoryItem> filtered = new ArrayList<>();

        // O(n) filtering over the current inventory list.
        for (InventoryItem item : source) {
            boolean matchesSearch = query.isEmpty()
                    || item.getName().toLowerCase(Locale.US).contains(query)
                    || item.getCategory().toLowerCase(Locale.US).contains(query);

            boolean matchesCategory = selectedCategory.equals("All")
                    || item.getCategory().equalsIgnoreCase(selectedCategory);

            boolean matchesStock = !lowStock || item.getQuantity() <= LOW_STOCK_THRESHOLD;

            if (matchesSearch && matchesCategory && matchesStock) {
                filtered.add(item);
            }
        }

        if (SORT_QUANTITY_LOW.equals(selectedSort)) {
            filtered.sort(Comparator.comparingInt(InventoryItem::getQuantity)
                    .thenComparing(InventoryItem::getName, String.CASE_INSENSITIVE_ORDER));
        } else if (SORT_QUANTITY_HIGH.equals(selectedSort)) {
            filtered.sort(Comparator.comparingInt(InventoryItem::getQuantity)
                    .reversed()
                    .thenComparing(InventoryItem::getName, String.CASE_INSENSITIVE_ORDER));
        } else {
            filtered.sort(Comparator.comparing(InventoryItem::getName,
                    String.CASE_INSENSITIVE_ORDER));
        }

        displayedItems.setValue(filtered);
    }

    public void insert(InventoryItem item) {
        repository.insert(item);
    }

    public void update(InventoryItem item) {
        repository.update(item);
    }

    public void delete(InventoryItem item) {
        repository.delete(item);
    }

    public void deleteAllItems() {
        repository.deleteAllItems();
    }
}
