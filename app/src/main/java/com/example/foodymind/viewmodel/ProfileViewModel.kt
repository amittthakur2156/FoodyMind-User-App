package com.example.foodymind.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodymind.Model.userModel
import com.example.foodymind.repository.ProfileRepository

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _user = MutableLiveData<userModel?>()
    val user: LiveData<userModel?> = _user

    private val _updateStatus = MutableLiveData<Boolean?>()
    val updateStatus: LiveData<Boolean?> = _updateStatus

    private val _updateMessage = MutableLiveData<String?>()
    val updateMessage: LiveData<String?> = _updateMessage

    fun loadUserData() {
        repository.loadUserData { _user.postValue(it) }
    }

    fun updateProfile(name: String, email: String, address: String, phone: String) {
        repository.updateProfile(
            name, email, address, phone,
            onSuccess = {
                _updateStatus.postValue(true)
                _updateMessage.postValue("Profile Updated Successfully")
            },
            onFailure = {
                _updateStatus.postValue(false)
                _updateMessage.postValue("Failed to Update Profile: $it")
            }
        )
    }

    fun resetUpdateStatus() {
        _updateStatus.value = null
        _updateMessage.value = null
    }
}