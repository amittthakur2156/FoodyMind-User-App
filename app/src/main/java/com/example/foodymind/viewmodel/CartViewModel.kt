package com.example.foodymind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodymind.Model.MenuItemModel
import com.example.foodymind.repository.CartRepository

class CartViewModel : ViewModel() {

    private val repository = CartRepository()

    private val _cartItems =
        MutableLiveData<List<MenuItemModel>>()

    val cartItems: LiveData<List<MenuItemModel>>
        get() = _cartItems

    fun getCartItems() {

        repository.getCartItems {

            _cartItems.postValue(it)

        }

    }
}