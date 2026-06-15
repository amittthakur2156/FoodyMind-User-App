package com.example.foodymind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodymind.repository.UserRepository

class SignUpViewModel : ViewModel() {

    private val repository = UserRepository()

    private val _signUpResult =
        MutableLiveData<Pair<Boolean, String?>>()

    val signUpResult:
            LiveData<Pair<Boolean, String?>>
        get() = _signUpResult

    fun createAccount(
        userName: String,
        email: String,
        password: String
    ) {

        repository.createAccount(
            userName,
            email,
            password
        ) { success, message ->

            _signUpResult.postValue(
                Pair(success, message)
            )

        }

    }

}