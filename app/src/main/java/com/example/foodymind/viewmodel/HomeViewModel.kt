package com.example.foodymind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodymind.Model.MenuItemModel
import com.example.foodymind.repository.MenuRepository

class HomeViewModel : ViewModel() {

    private val repository = MenuRepository()

    private val _menuList =
        MutableLiveData<List<MenuItemModel>>()

    val menuList: LiveData<List<MenuItemModel>>
        get() = _menuList

    fun getMenuItems() {

        repository.getMenuItems {

            _menuList.postValue(it)

        }

    }

}