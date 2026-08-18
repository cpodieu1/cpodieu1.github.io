package com.example.inventoryapp.util;

/**
 * Centralizes inventory input validation so the Activity does not contain
 * business rules for validating inventory records.
 */
public final class InventoryValidator {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_CATEGORY_LENGTH = 50;
    private static final int MAX_QUANTITY = 1_000_000;

    private InventoryValidator() {
        // Utility class.
    }

    public static String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Item name is required.";
        }
        if (name.trim().length() > MAX_NAME_LENGTH) {
            return "Item name must be 100 characters or fewer.";
        }
        return null;
    }

    public static String validateCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return "Category is required.";
        }
        if (category.trim().length() > MAX_CATEGORY_LENGTH) {
            return "Category must be 50 characters or fewer.";
        }
        return null;
    }

    public static String validateQuantity(String quantityText) {
        if (quantityText == null || quantityText.trim().isEmpty()) {
            return "Quantity is required.";
        }

        try {
            int quantity = Integer.parseInt(quantityText.trim());
            if (quantity < 0) {
                return "Quantity cannot be negative.";
            }
            if (quantity > MAX_QUANTITY) {
                return "Quantity must be 1,000,000 or less.";
            }
        } catch (NumberFormatException exception) {
            return "Quantity must be a whole number.";
        }

        return null;
    }
}
