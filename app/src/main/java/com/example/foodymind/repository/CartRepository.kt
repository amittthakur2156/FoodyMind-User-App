package com.example.foodymind.repository

import com.example.foodymind.Model.MenuItemModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CartRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun addToCart(

        foodName: String,
        foodPrice: String,
        foodImage: String,
        foodDescription: String,
        foodIngredient: String,

        callback: (Boolean) -> Unit

    ) {

        val currentUser = auth.currentUser

        if (currentUser == null) {

            callback(false)
            return

        }

        val cartRef =
            database.child("cart")
                .child(currentUser.uid)

        val cartKey = cartRef.push().key

        val cartItem = HashMap<String, Any>()

        cartItem["foodName"] = foodName
        cartItem["foodPrice"] = foodPrice
        cartItem["foodImage"] = foodImage
        cartItem["foodDescription"] = foodDescription
        cartItem["foodIngredient"] = foodIngredient
        cartItem["foodQuantity"] = 1

        if (cartKey != null) {

            cartRef.child(cartKey)
                .setValue(cartItem)
                .addOnSuccessListener {

                    callback(true)

                }
                .addOnFailureListener {

                    callback(false)

                }

        }

    }
    fun getCartItems(
        callback: (List<MenuItemModel>) -> Unit
    ) {

        val currentUser = auth.currentUser

        if (currentUser == null) {
            callback(emptyList())
            return
        }

        database.child("cart")
            .child(currentUser.uid)
            .get()
            .addOnSuccessListener { snapshot ->

                val list = mutableListOf<MenuItemModel>()

                for (itemSnapshot in snapshot.children) {

                    val item =
                        itemSnapshot.getValue(
                            MenuItemModel::class.java
                        )

                    item?.foodId = itemSnapshot.key

                    item?.let {
                        list.add(it)
                    }

                }

                callback(list)

            }
            .addOnFailureListener {

                callback(emptyList())

            }

    }

}