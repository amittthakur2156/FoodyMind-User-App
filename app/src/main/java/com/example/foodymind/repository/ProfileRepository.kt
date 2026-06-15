package com.example.foodymind.repository

import com.example.foodymind.Model.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun loadUserData(callback: (userModel?) -> Unit) {
        val uid = auth.currentUser?.uid ?: run {
            callback(null)
            return
        }
        database.child("user").child(uid).get()
            .addOnSuccessListener { snapshot ->
                callback(snapshot.getValue(userModel::class.java))
            }
            .addOnFailureListener { callback(null) }
    }

    fun updateProfile(
        name: String,
        email: String,
        address: String,
        phone: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onFailure("User not logged in")
            return
        }

        val updates = hashMapOf<String, Any>(
            "name" to name,
            "email" to email,
            "address" to address,
            "phone" to phone
        )

        database.child("user").child(uid).updateChildren(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Update failed") }
    }
}