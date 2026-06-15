package com.example.foodymind.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodymind.DetailActivity
import com.example.foodymind.Model.MenuItemModel
import com.example.foodymind.databinding.MenuItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MenuAdapter(private val MenuItems: List<MenuItemModel>
      , private val requireContext: Context )
    : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MenuAdapter.MenuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(p0.context), p0, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.bind(position)

    }

    override fun getItemCount(): Int = MenuItems.size

    inner class MenuViewHolder(private val binding: MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.Menuimage.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val menuItem = MenuItems[position]

                    val intent = Intent(requireContext, DetailActivity::class.java)
                    intent.putExtra("MenuItemName", menuItem.foodName)
                    intent.putExtra("MenuItemImage", menuItem.foodImage)
                    intent.putExtra("MenuItemDescription", menuItem.foodDescription)
                    intent.putExtra("MenuItemIngredients", menuItem.foodIngredient)
                    intent.putExtra("MenuItemPrice", "$${menuItem.foodPrice?.replace("₹", "")}")
                    intent.putExtra("MenuItemId", menuItem.foodId)

                    requireContext.startActivity(intent)
                }
            }
        }

        fun bind(position: Int) {

            val menuItem = MenuItems[position]

            binding.apply {

                FoodNameMenu.text = menuItem.foodName
                priceMenu.text = "$${menuItem.foodPrice?.replace("₹", "")}"

                Glide.with(requireContext)
                    .load(menuItem.foodImage)
                    .into(Menuimage)

                AddToCartMenu.setOnClickListener {

                    val user = FirebaseAuth.getInstance().currentUser
                        ?: return@setOnClickListener

                    // Same key for Home + Search + Popular
                    val key = menuItem.foodName!!
                        .trim()
                        .replace("\\s+".toRegex(), "_")
                        .lowercase()

                    val cartRef = FirebaseDatabase.getInstance()
                        .reference
                        .child("cart")
                        .child(user.uid)
                        .child(key)

                    cartRef.get().addOnSuccessListener { snapshot ->

                        if (snapshot.exists()) {

                            val quantity =
                                snapshot.child("foodQuantity")
                                    .getValue(Long::class.java)
                                    ?.toInt() ?: 1

                            cartRef.child("foodQuantity")
                                .setValue(quantity + 1)

                            Toast.makeText(
                                requireContext,
                                "Quantity Updated",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            val cartItem = HashMap<String, Any?>()

                            cartItem["foodName"] = menuItem.foodName
                            cartItem["foodPrice"] = menuItem.foodPrice
                            cartItem["foodImage"] = menuItem.foodImage
                            cartItem["foodDescription"] = menuItem.foodDescription
                            cartItem["foodIngredient"] = menuItem.foodIngredient
                            cartItem["foodId"] = menuItem.foodId
                            cartItem["foodQuantity"] = 1

                            cartRef.setValue(cartItem)
                                .addOnSuccessListener {

                                    Toast.makeText(
                                        requireContext,
                                        "Added To Cart",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                }
            }
        }
    }
}

