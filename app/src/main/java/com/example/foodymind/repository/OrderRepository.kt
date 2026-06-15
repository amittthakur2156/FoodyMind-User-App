package com.example.foodymind.repository

import com.example.foodymind.Model.MenuItemModel
import com.example.foodymind.Model.userModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class OrderRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun loadUserData(callback: (userModel?) -> Unit) {
        val uid = auth.currentUser?.uid ?: run {
            callback(null)
            return
        }
        database.child("user").child(uid).get()
            .addOnSuccessListener { callback(it.getValue(userModel::class.java)) }
            .addOnFailureListener { callback(null) }
    }

    fun placeOrder(
        name: String,
        address: String,
        phone: String,
        totalPrice: String,
        foodNameList: ArrayList<String>,
        foodImageList: ArrayList<String>,
        foodPriceList: ArrayList<String>,
        foodQuantityList: ArrayList<Int>,
        callback: (success: Boolean, message: String?) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            callback(false, "User not logged in")
            return
        }

        val orderRef = database.child("orderDetails").push()
        val pushKey = orderRef.key ?: ""

        val orderData = HashMap<String, Any?>().apply {
            put("userUid", uid)
            put("name", name)
            put("address", address)
            put("phone", phone)
            put("totalPrice", totalPrice)
            put("foodNames", foodNameList)
            put("foodImages", foodImageList)
            put("foodPrices", foodPriceList)
            put("foodQuantities", foodQuantityList)
            put("itemPushKey", pushKey)
            put("status", "Pending")
            put("accepted", false)
            put("currentTime", System.currentTimeMillis())
        }

        database.child("orderDetails").get()
            .addOnSuccessListener { snapshot ->

                val alreadyExists = snapshot.children.any { order ->
                    order.child("userUid").getValue(String::class.java) == uid &&
                            order.child("totalPrice").getValue(String::class.java) == totalPrice &&
                            order.child("status").getValue(String::class.java) == "Pending"
                }

                if (alreadyExists) {
                    callback(false, "Order already exists")
                    return@addOnSuccessListener
                }

                orderRef.setValue(orderData)
                    .addOnSuccessListener {
                        database.child("user").child(uid)
                            .child("BuyHistory").child(pushKey)
                            .setValue(orderData)
                            .addOnSuccessListener {
                                removeCart()
                                callback(true, null)
                            }
                            .addOnFailureListener { e ->
                                callback(false, "Failed to update history: ${e.message}")
                            }
                    }
                    .addOnFailureListener { e ->
                        callback(false, e.message)
                    }
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    fun removeCart() {
        val uid = auth.currentUser?.uid ?: return
        database.child("cart").child(uid).removeValue()
    }

    // HistoryFragment ke liye
    data class HistoryResult(
        val foodNames: ArrayList<String>,
        val foodPrices: ArrayList<String>,
        val foodImages: ArrayList<String>,
        val recentFoodName: String,
        val recentFoodPrice: String,
        val recentFoodImage: String
    )

    fun getHistory(
        onSuccess: (HistoryResult) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onFailure("User not logged in")
            return
        }

        database.child("orderDetails").get()
            .addOnSuccessListener { snapshot ->

                val foodNames = ArrayList<String>()
                val foodPrices = ArrayList<String>()
                val foodImages = ArrayList<String>()

                var latestTime = -1L
                var recentFoodName = ""
                var recentFoodPrice = ""
                var recentFoodImage = ""

                for (order in snapshot.children) {
                    val status = order.child("status").getValue(String::class.java)
                    val userId = order.child("userUid").getValue(String::class.java)

                    if (status == "Delivered" && userId == uid) {
                        val currentTime = order.child("currentTime").getValue(Long::class.java) ?: 0L
                        val names = order.child("foodNames").children.toList()
                        val prices = order.child("foodPrices").children.toList()
                        val images = order.child("foodImages").children.toList()

                        if (currentTime > latestTime && names.isNotEmpty()) {
                            latestTime = currentTime
                            recentFoodName = names[0].getValue(String::class.java) ?: ""
                            recentFoodPrice = prices[0].getValue(String::class.java) ?: ""
                            recentFoodImage = images[0].getValue(String::class.java) ?: ""
                        }

                        for (i in names.indices) {
                            foodNames.add(names[i].getValue(String::class.java) ?: "")
                            foodPrices.add(prices[i].getValue(String::class.java) ?: "")
                            foodImages.add(images[i].getValue(String::class.java) ?: "")
                        }
                    }
                }

                onSuccess(
                    HistoryResult(
                        foodNames, foodPrices, foodImages,
                        recentFoodName, recentFoodPrice, recentFoodImage
                    )
                )
            }
            .addOnFailureListener { onFailure(it.message ?: "Failed to load history") }
    }

    fun addToCart(
        foodName: String,
        foodPrice: String,
        foodImage: String,
        onSuccess: (String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onFailure("User not logged in")
            return
        }

        val cartRef = database.child("cart").child(uid)

        cartRef.get().addOnSuccessListener { cartSnapshot ->
            var itemFound = false

            for (cartItem in cartSnapshot.children) {
                val item = cartItem.getValue(MenuItemModel::class.java)
                if (item?.foodName == foodName) {
                    val qty = item.foodQuantity ?: 1
                    cartItem.ref.child("foodQuantity").setValue(qty + 1)
                    itemFound = true
                    onSuccess("Quantity Updated")
                    break
                }
            }

            if (!itemFound) {
                val key = cartRef.push().key ?: run {
                    onFailure("Failed to generate key")
                    return@addOnSuccessListener
                }

                val newItem = MenuItemModel(
                    foodName = foodName,
                    foodPrice = foodPrice,
                    foodImage = foodImage,
                    foodQuantity = 1,
                    foodId = key
                )

                cartRef.child(key).setValue(newItem)
                    .addOnSuccessListener { onSuccess("Added to Cart") }
                    .addOnFailureListener { onFailure(it.message ?: "Failed to add") }
            }
        }.addOnFailureListener { onFailure(it.message ?: "Failed to fetch cart") }
    }
}