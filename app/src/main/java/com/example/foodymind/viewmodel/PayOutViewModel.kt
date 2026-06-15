package com.example.foodymind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodymind.Model.userModel
import com.example.foodymind.repository.OrderRepository

class PayOutViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _user = MutableLiveData<userModel>()
    val user: LiveData<userModel> = _user

    // 3 states: null = idle, true = success, false = failure
    private val _orderStatus = MutableLiveData<Boolean?>()
    val orderStatus: LiveData<Boolean?> = _orderStatus

    private val _orderMessage = MutableLiveData<String?>()
    val orderMessage: LiveData<String?> = _orderMessage

    fun loadUserData() {
        repository.loadUserData { user ->
            user?.let { _user.postValue(it) }
        }
    }

    fun placeOrder(
        name: String,
        address: String,
        phone: String,
        totalPrice: String,
        foodNameList: ArrayList<String>,
        foodImageList: ArrayList<String>,
        foodPriceList: ArrayList<String>,
        foodQuantityList: ArrayList<Int>
    ) {
        repository.placeOrder(
            name, address, phone, totalPrice,
            foodNameList, foodImageList, foodPriceList, foodQuantityList
        ) { success, message ->
            _orderStatus.postValue(success)
            _orderMessage.postValue(message)
        }
    }

    // Order place hone ke baad state reset karo
    fun resetOrderStatus() {
        _orderStatus.value = null
        _orderMessage.value = null
    }
}