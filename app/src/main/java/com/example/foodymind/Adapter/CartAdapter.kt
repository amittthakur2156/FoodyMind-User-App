package com.example.foodymind.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodymind.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CartAdapter(
    private val cartItems: MutableList<String>,
    private val cartItemPrice: MutableList<String>,
    private val cartImage: MutableList<String>,
    private val cartQuantity: MutableList<Int>,
    private val cartKey: MutableList<String>
)
    : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CartViewHolder {
        val binding= CartItemBinding.inflate(LayoutInflater.from(p0.context),p0,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(private val binding: CartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = cartQuantity[position]
                FoodNameCart.text = cartItems[position]
                val price = cartItemPrice[position]
                    .replace("₹","")
                    .replace("$","")
                    .toInt()
                PriceCart.text = "$${price * quantity}"
                Glide.with(binding.root.context)
                    .load(cartImage[position])
                    .into(binding.CartImage)
                itemQuantityCart.text = quantity.toString()

                MinusBtnCart.setOnClickListener {
                    decreaseQuantity(position)
                }
                PlusBtnCart.setOnClickListener {
                    increaseQuantity(position)

                }
                DeleteBtnCart.setOnClickListener {
                    deleteitem(position)

                }


            }
        }
        private fun increaseQuantity(position: Int) {

            if (cartQuantity[position] < 10) {

                cartQuantity[position]++

                val price = cartItemPrice[position]
                    .replace("₹", "")
                    .replace("$", "")
                    .toInt()

                binding.PriceCart.text =
                    "$${price * cartQuantity[position]}"

                binding.itemQuantityCart.text =
                    cartQuantity[position].toString()

                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
                val key = cartKey[position]

                FirebaseDatabase.getInstance().reference
                    .child("cart")
                    .child(uid)
                    .child(key)
                    .child("foodQuantity")
                    .setValue(cartQuantity[position])
            }
        }
        private fun deleteitem(position: Int) {

            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
            val key = cartKey[position]

            FirebaseDatabase.getInstance().reference
                .child("cart")
                .child(uid)
                .child(key)
                .removeValue()
                .addOnSuccessListener {

                    cartItems.removeAt(position)
                    cartItemPrice.removeAt(position)
                    cartImage.removeAt(position)
                    cartQuantity.removeAt(position)
                    cartKey.removeAt(position)

                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)

                }
        }
        private fun decreaseQuantity(position: Int) {

            if (cartQuantity[position] > 1) {

                cartQuantity[position]--

                val price = cartItemPrice[position]
                    .replace("$", "")
                    .toInt()


                binding.PriceCart.text =
                    "$${price * cartQuantity[position]}"

                binding.itemQuantityCart.text =
                    cartQuantity[position].toString()

                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
                val key = cartKey[position]

                FirebaseDatabase.getInstance().reference
                    .child("cart")
                    .child(uid)
                    .child(key)
                    .child("foodQuantity")
                    .setValue(cartQuantity[position])
            }
        }

    }
}