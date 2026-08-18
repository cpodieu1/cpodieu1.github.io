package com.example.inventoryapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.inventoryapp.util.InventoryValidator;

import org.junit.Test;

public class InventoryValidatorTest {

    @Test
    public void validInventoryValuesReturnNoError() {
        assertNull(InventoryValidator.validateName("Laptop"));
        assertNull(InventoryValidator.validateCategory("Electronics"));
        assertNull(InventoryValidator.validateQuantity("25"));
    }

    @Test
    public void blankNameIsRejected() {
        assertEquals(
                "Item name is required.",
                InventoryValidator.validateName("   ")
        );
    }

    @Test
    public void negativeQuantityIsRejected() {
        assertEquals(
                "Quantity cannot be negative.",
                InventoryValidator.validateQuantity("-1")
        );
    }

    @Test
    public void nonNumericQuantityIsRejected() {
        assertEquals(
                "Quantity must be a whole number.",
                InventoryValidator.validateQuantity("abc")
        );
    }
}
