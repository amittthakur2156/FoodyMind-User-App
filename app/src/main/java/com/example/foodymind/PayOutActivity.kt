package com.example.foodymind

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodymind.Model.userModel
import com.example.foodymind.databinding.ActivityPayOutBinding
import com.example.foodymind.viewmodel.PayOutViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding
    private val viewModel: PayOutViewModel by viewModels()
    private var foodNameList: ArrayList<String>? = null
    private var foodImageList: ArrayList<String>? = null
    private var foodQuantityList: ArrayList<Int>? = null
    private var foodPriceList: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.user.observe(this) { user ->

            binding.Name.setText(user.name ?: "")
            binding.etAddress.setText(user.address ?: "")
            binding.phone.setText(user.phone ?: "")

        }

        foodNameList =
            intent.getStringArrayListExtra("FoodItemName")

        foodImageList =
            intent.getStringArrayListExtra("FoodItemImage")

        foodPriceList =
            intent.getStringArrayListExtra("FoodItemPrice")

        foodQuantityList =
            intent.getIntegerArrayListExtra("FoodItemQuantity")

        var totalAmount = 0

        if (foodPriceList != null && foodQuantityList != null) {

            for (i in foodPriceList!!.indices) {

                val price = foodPriceList!![i]
                    .replace("₹", "")
                    .replace("$", "")
                    .trim()
                    .toIntOrNull() ?: 0

                totalAmount += price * foodQuantityList!![i]
            }
        }

        binding.total.setText("$$totalAmount")
       // loadUserData()
        viewModel.loadUserData()
        binding.PayOutbackButton.setOnClickListener {
            finish()
        }


        binding.PlaceOrder.setOnClickListener {

            val uid =
                FirebaseAuth.getInstance().currentUser?.uid
                    ?: return@setOnClickListener

            val orderRef = FirebaseDatabase.getInstance()
                .reference
                .child("orderDetails")
                .push()

            val orderData = HashMap<String, Any?>()

            // User Details
            orderData["userUid"] = uid
            orderData["name"] = binding.Name.text.toString().trim()
            orderData["address"] = binding.etAddress.text.toString().trim()
            orderData["phone"] = binding.phone.text.toString().trim()
            orderData["totalPrice"] = binding.total.text.toString()

            // Food Details
            orderData["foodNames"] = foodNameList ?: ArrayList<String>()
            orderData["foodImages"] = foodImageList ?: ArrayList<String>()
            orderData["foodPrices"] = foodPriceList ?: ArrayList<String>()
            orderData["foodQuantities"] = foodQuantityList ?: ArrayList<Int>()

            // Order Info
            orderData["itemPushKey"] = orderRef.key ?: ""
            orderData["status"] = "Pending"
            orderData["accepted"] = false
            orderData["currentTime"] = System.currentTimeMillis()

            val userOrderHistoryRef =
                FirebaseDatabase.getInstance()
                    .reference
                    .child("user")
                    .child(uid)
                    .child("BuyHistory")
                    .child(orderRef.key ?: "")

            // Check Duplicate Order
            FirebaseDatabase.getInstance()
                .reference
                .child("orderDetails")
                .get()
                .addOnSuccessListener { snapshot ->

                    var alreadyExists = false

                    for (order in snapshot.children) {

                        val userUid =
                            order.child("userUid")
                                .getValue(String::class.java)

                        val totalPrice =
                            order.child("totalPrice")
                                .getValue(String::class.java)

                        val status =
                            order.child("status")
                                .getValue(String::class.java)

                        if (
                            userUid == uid &&
                            totalPrice == binding.total.text.toString() &&
                            status == "Pending"
                        ) {
                            alreadyExists = true
                            break
                        }
                    }

                    if (alreadyExists) {

                        Toast.makeText(
                            this,
                            "Order already exists",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        orderRef.setValue(orderData)
                            .addOnSuccessListener {

                                userOrderHistoryRef
                                    .setValue(orderData)
                                    .addOnSuccessListener {

                                        removeItemFromCart()

                                        val bottomSheetFragment =
                                            CongratsFragment()

                                        bottomSheetFragment.show(
                                            supportFragmentManager,
                                            "Congrats"
                                        )
                                    }
                                    .addOnFailureListener {

                                        Toast.makeText(
                                            this,
                                            "Failed to update history",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                            .addOnFailureListener { e ->

                                Toast.makeText(
                                    this,
                                    e.message,
                                    Toast.LENGTH_LONG
                                ).show()

                                e.printStackTrace()
                            }
                    }
                }
                .addOnFailureListener { e ->

                    Toast.makeText(
                        this,
                        e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }


    }

    private fun removeItemFromCart() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseDatabase.getInstance()
            .reference
            .child("cart")
            .child(uid)
            .removeValue()
    }

    private fun loadUserData() {

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseDatabase.getInstance()
            .reference
            .child("user")
            .child(uid)
            .get()
            .addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {

                    val user = snapshot.getValue(userModel::class.java)

                    binding.Name.setText(user?.name ?: "")
                    binding.etAddress.setText(user?.address ?: "")
                    binding.phone.setText(user?.phone ?: "")
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
    }
}