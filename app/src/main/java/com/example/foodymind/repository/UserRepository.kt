package com.example.foodymind.repository

import com.example.foodymind.Model.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun createAccount(
        userName: String,
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val uid = auth.currentUser?.uid ?: ""

                    val user = userModel(
                        uid = uid,
                        name = userName,
                        email = email,
                        password = password,
                        photoUrl = "",
                        provider = "email"
                    )

                    database.child("user")
                        .child(uid)
                        .setValue(user)
                        .addOnSuccessListener {

                            callback(true, "Account Created Successfully")

                        }
                        .addOnFailureListener {

                            callback(false, it.message)

                        }

                } else {

                    callback(false, task.exception?.message)

                }

            }

    }
    fun login(
        email: String,
        password: String,
        callback: (Boolean, String?) -> Unit
    ) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    callback(true, "Login Successful")

                } else {

                    callback(
                        false,
                        task.exception?.message
                    )

                }

            }

    }

}