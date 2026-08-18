package com.example.inventoryapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventoryapp.R;
import com.example.inventoryapp.data.entity.InventoryItem;

public class InventoryAdapter extends ListAdapter<InventoryItem, InventoryAdapter.InventoryViewHolder> {

    public interface OnDeleteClickListener {
        void onDeleteClick(InventoryItem item);
    }

    private final OnDeleteClickListener deleteClickListener;

    private static final DiffUtil.ItemCallback<InventoryItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<InventoryItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull InventoryItem oldItem,
                                               @NonNull InventoryItem newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull InventoryItem oldItem,
                                                  @NonNull InventoryItem newItem) {
                    return oldItem.getId() == newItem.getId()
                            && oldItem.getName().equals(newItem.getName())
                            && oldItem.getQuantity() == newItem.getQuantity()
                            && oldItem.getCategory().equals(newItem.getCategory());
                }
            };

    public InventoryAdapter(OnDeleteClickListener deleteClickListener) {
        super(DIFF_CALLBACK);
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryItem item = getItem(position);

        holder.itemNameTV.setText(item.getName());
        holder.itemQuantityTV.setText(String.valueOf(item.getQuantity()));
        holder.itemCategoryTV.setText(item.getCategory());

        holder.deleteBtn.setOnClickListener(v -> deleteClickListener.onDeleteClick(item));
    }

    static class InventoryViewHolder extends RecyclerView.ViewHolder {

        final TextView itemNameTV;
        final TextView itemQuantityTV;
        final TextView itemCategoryTV;
        final Button deleteBtn;

        InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTV = itemView.findViewById(R.id.itemNameTV);
            itemQuantityTV = itemView.findViewById(R.id.itemQuantityTV);
            itemCategoryTV = itemView.findViewById(R.id.itemCategoryTV);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
