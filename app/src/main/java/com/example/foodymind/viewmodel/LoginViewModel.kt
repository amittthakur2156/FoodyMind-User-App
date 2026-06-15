package com.example.foodymind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodymind.repository.UserRepository

class LoginViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _loginResult =
        MutableLiveData<Pair<Boolean, String?>>()

    val loginResult: LiveData<Pair<Boolean, String?>>
        get() = _loginResult

    fun login(
        email: String,
        password: String
    ) {

        repository.login(
            email,
            password
        ) { success, message ->

            _loginResult.postValue(
                Pair(success, message)
            )

        }

    }
}