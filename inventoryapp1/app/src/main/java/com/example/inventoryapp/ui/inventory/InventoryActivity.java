package com.example.inventoryapp.ui.inventory;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryapp.R;
import com.example.inventoryapp.adapter.InventoryAdapter;
import com.example.inventoryapp.data.entity.InventoryItem;
import com.example.inventoryapp.util.InventoryValidator;
import com.example.inventoryapp.viewmodel.InventoryViewModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class InventoryActivity extends AppCompatActivity {

    private InventoryViewModel viewModel;
    private InventoryAdapter adapter;

    private EditText itemNameET;
    private EditText itemQuantityET;
    private EditText searchET;
    private EditText categoryET;
    private Spinner categoryFilterSpinner;
    private Spinner sortSpinner;
    private CheckBox lowStockCheckBox;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        itemNameET = findViewById(R.id.itemNameET);
        itemQuantityET = findViewById(R.id.itemQuantityET);
        categoryET = findViewById(R.id.categoryET);
        searchET = findViewById(R.id.searchET);
        categoryFilterSpinner = findViewById(R.id.categoryFilterSpinner);
        sortSpinner = findViewById(R.id.sortSpinner);
        lowStockCheckBox = findViewById(R.id.lowStockCheckBox);
        addBtn = findViewById(R.id.addBtn);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        adapter = new InventoryAdapter(this::confirmDelete);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);

        setupSortSpinner();
        setupSearch();
        setupFilters();

        viewModel.getDisplayedItems().observe(this, items -> adapter.submitList(items));
        viewModel.getAllItems().observe(this, items -> updateCategoryFilter(items));

        addBtn.setOnClickListener(v -> addInventoryItem());
    }

    private void setupSortSpinner() {
        String[] sortOptions = {
                InventoryViewModel.SORT_NAME,
                InventoryViewModel.SORT_QUANTITY_LOW,
                InventoryViewModel.SORT_QUANTITY_HIGH
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                sortOptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(int position) {
                viewModel.setSortOption(sortOptions[position]);
            }
        });
    }

    private void setupSearch() {
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action required.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSearchQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action required.
            }
        });
    }

    private void setupFilters() {
        lowStockCheckBox.setOnCheckedChangeListener(
                (buttonView, isChecked) -> viewModel.setLowStockOnly(isChecked)
        );
    }

    private void updateCategoryFilter(List<InventoryItem> items) {
        String currentSelection = categoryFilterSpinner.getSelectedItem() == null
                ? "All"
                : categoryFilterSpinner.getSelectedItem().toString();

        Set<String> categories = new LinkedHashSet<>();
        categories.add("All");
        for (InventoryItem item : items) {
            if (item.getCategory() != null && !item.getCategory().trim().isEmpty()) {
                categories.add(item.getCategory());
            }
        }

        List<String> categoryList = new ArrayList<>(categories);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categoryList
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryFilterSpinner.setAdapter(adapter);

        int position = categoryList.indexOf(currentSelection);
        categoryFilterSpinner.setSelection(position >= 0 ? position : 0);

        categoryFilterSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override
            public void onItemSelected(int selectedPosition) {
                viewModel.setCategoryFilter(categoryList.get(selectedPosition));
            }
        });
    }

    private void addInventoryItem() {
        String name = itemNameET.getText().toString().trim();
        String quantity = itemQuantityET.getText().toString().trim();
        String category = categoryET.getText().toString().trim();

        String error = InventoryValidator.validateName(name);
        if (error == null) {
            error = InventoryValidator.validateQuantity(quantity);
        }
        if (error == null) {
            error = InventoryValidator.validateCategory(category);
        }

        if (error != null) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            return;
        }

        InventoryItem item = new InventoryItem(
                name,
                Integer.parseInt(quantity),
                category
        );

        viewModel.insert(item);

        itemNameET.setText("");
        itemQuantityET.setText("");
        categoryET.setText("");

        Toast.makeText(this, "Item added successfully.", Toast.LENGTH_SHORT).show();
    }

    private void confirmDelete(InventoryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Delete " + item.getName() + " from inventory?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.delete(item);
                    Toast.makeText(this, "Item deleted.", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private abstract static class SimpleItemSelectedListener
            implements android.widget.AdapterView.OnItemSelectedListener {

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {
            // No action required.
        }

        public abstract void onItemSelected(int position);

        @Override
        public final void onItemSelected(android.widget.AdapterView<?> parent,
                                         View view,
                                         int position,
                                         long id) {
            onItemSelected(position);
        }
    }
}
