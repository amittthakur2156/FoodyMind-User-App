package com.example.foodymind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodymind.repository.CartRepository

class DetailViewModel : ViewModel() {

    private val repository = CartRepository()

    private val _result =
        MutableLiveData<Boolean>()

    val result: LiveData<Boolean>
        get() = _result

    fun addToCart(

        foodName: String,
        foodPrice: String,
        foodImage: String,
        foodDescription: String,
        foodIngredient: String

    ) {

        repository.addToCart(

            foodName,
            foodPrice,
            foodImage,
            foodDescription,
            foodIngredient

        ) {

            _result.postValue(it)

        }

    }

}