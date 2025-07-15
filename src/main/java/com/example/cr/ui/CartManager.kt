package com.example.cr.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class CartItem(
    val name: String,
    val price: String,
    val imageResId: Int,
    val quantity: MutableState<Int> = mutableStateOf(1) // Make quantity observable state
)

// Shared list to hold cart items
object CartManager {
    val cartItems: SnapshotStateList<CartItem> = mutableStateListOf()

    fun addItem(item: CartItem) {
        val existingItem = cartItems.find { it.name == item.name && it.imageResId == item.imageResId }
        if (existingItem != null) {
            existingItem.quantity.value++ // Update the state value
        } else {
            // When adding a new item, create a new CartItem instance
            // The default quantity will be mutableStateOf(1)
            cartItems.add(item.copy(quantity = mutableStateOf(1)))
        }
    }

    fun removeItem(item: CartItem) {
        cartItems.remove(item)
    }

    fun decreaseItemQuantity(item: CartItem) {
        val existingItem = cartItems.find { it.name == item.name && it.imageResId == item.imageResId }
        existingItem?.let {
            if (it.quantity.value > 1) {
                it.quantity.value-- // Update the state value
            } else {
                cartItems.remove(it)
            }
        }
    }

    fun clearCart() {
        cartItems.clear()
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { item ->
            val priceString = item.price.replace("[^0-9.]".toRegex(), "")
            priceString.toDoubleOrNull()?.let { it * item.quantity.value } ?: 0.0
        }
    }
} 