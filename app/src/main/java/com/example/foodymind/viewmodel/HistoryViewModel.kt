package com.example.foodymind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodymind.repository.OrderRepository

class HistoryViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _historyResult = MutableLiveData<OrderRepository.HistoryResult?>()
    val historyResult: LiveData<OrderRepository.HistoryResult?> = _historyResult

    private val _historyError = MutableLiveData<String?>()
    val historyError: LiveData<String?> = _historyError

    private val _cartMessage = MutableLiveData<String?>()
    val cartMessage: LiveData<String?> = _cartMessage

    fun loadHistory() {
        repository.getHistory(
            onSuccess = { _historyResult.postValue(it) },
            onFailure = { _historyError.postValue(it) }
        )
    }

    fun addToCart(foodName: String, foodPrice: String, foodImage: String) {
        repository.addToCart(
            foodName, foodPrice, foodImage,
            onSuccess = { _cartMessage.postValue(it) },
            onFailure = { _cartMessage.postValue(it) }
        )
    }

    fun resetCartMessage() {
        _cartMessage.value = null
    }
}