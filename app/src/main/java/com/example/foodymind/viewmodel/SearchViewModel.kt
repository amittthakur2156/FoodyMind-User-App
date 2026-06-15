package com.example.foodymind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodymind.Model.MenuItemModel
import com.example.foodymind.repository.MenuRepository
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {

    private val repository = MenuRepository()

    private val _menuList = MutableLiveData<ArrayList<MenuItemModel>>()
    val menuList: LiveData<ArrayList<MenuItemModel>> = _menuList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var menuListener: ValueEventListener? = null

    fun startListening() {
        menuListener = repository.searchMenu(
            onUpdate = { _menuList.postValue(it) },
            onError = { _errorMessage.postValue(it) }
        )
    }

    fun stopListening() {
        menuListener?.let { repository.removeMenuListener(it) }
        menuListener = null
    }

    override fun onCleared() {
        super.onCleared()
        stopListening()
    }
}