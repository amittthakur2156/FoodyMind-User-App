package com.example.foodymind.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodymind.DetailActivity
import com.example.foodymind.databinding.PopularItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PopularAdapter(
    private val items: List<String>,
    private val price: List<String>,
    private val images: List<String>,
    private val description: List<String>,
    private val ingredient: List<String>,
    private val requireContext: Context
) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularViewHolder {

        return PopularViewHolder(
            PopularItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: PopularViewHolder,
        position: Int
    ) {

        holder.bind(
            items[position],
            price[position],
            images[position]
        )

        holder.binding.Popularimage.setOnClickListener {

            val intent = Intent(
                holder.itemView.context,
                DetailActivity::class.java
            )

            intent.putExtra("MenuItemName", items[position])
            intent.putExtra("MenuItemPrice", price[position])
            intent.putExtra("MenuItemImage", images[position])
            intent.putExtra("MenuItemDescription", description[position])
            intent.putExtra("MenuItemIngredients", ingredient[position])

            holder.itemView.context.startActivity(intent)
        }
        holder.binding.AddToCartPopular.setOnClickListener {

            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser == null) {
                Toast.makeText(requireContext, "Please Login First", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val key = items[position]
                .trim()
                .replace("\\s+".toRegex(), "_")
                .lowercase()


            val cartRef = FirebaseDatabase.getInstance()
                .reference
                .child("cart")
                .child(currentUser.uid)
                .child(key)

            cartRef.get().addOnSuccessListener { snapshot ->

                if (snapshot.exists()) {

                    val quantity =
                        snapshot.child("foodQuantity")
                            .getValue(Int::class.java) ?: 1

                    cartRef.child("foodQuantity")
                        .setValue(quantity + 1)

                    Toast.makeText(
                        requireContext,
                        "Quantity Updated",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    val cartItem = HashMap<String, Any?>()

                    cartItem["foodName"] = items[position]
                    cartItem["foodPrice"] = price[position]
                    cartItem["foodImage"] = images[position]
                    cartItem["foodDescription"] = description[position]
                    cartItem["foodIngredient"] = ingredient[position]
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

    override fun getItemCount(): Int {
        return items.size
    }

    class PopularViewHolder(
        val binding: PopularItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: String,
            price: String,
            image: String
        ) {

            binding.FoodNamePopular.text = item
            binding.pricePopular.text = "$${price.replace("₹", "")}"

            Glide.with(binding.root.context)
                .load(image)
                .into(binding.Popularimage)
        }
    }
}