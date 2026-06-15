package com.example.foodymind.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodymind.databinding.BuyItemAgainBinding

class BuyAgainAdapter(
    private val buyAgainFoodName: ArrayList<String>,
    private val buyAgainFoodPrice: ArrayList<String>,
    private val buyAgainFoodImage: ArrayList<String>,
    private val onBuyAgainClick: (Int) -> Unit
) : RecyclerView.Adapter<BuyAgainAdapter.BuyAgainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyAgainViewHolder {

        val binding = BuyItemAgainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return BuyAgainViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BuyAgainViewHolder,
        position: Int
    ) {

        holder.bind(
            buyAgainFoodName[position],
            buyAgainFoodPrice[position],
            buyAgainFoodImage[position]
        )

        holder.binding.BuyAgainItemButton.setOnClickListener {

            val pos = holder.bindingAdapterPosition

            if (pos != RecyclerView.NO_POSITION) {
                onBuyAgainClick(pos)
            }
        }
    }

    override fun getItemCount(): Int {
        return buyAgainFoodName.size
    }

    class BuyAgainViewHolder(
        val binding: BuyItemAgainBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            foodName: String,
            foodPrice: String,
            foodImage: String
        ) {

            binding.BuyAgainItemFoodName.text = foodName

            binding.BuyAgainItemPrice.text =
                "$" + foodPrice
                    .replace("$", "")
                    .replace("₹", "")

            Glide.with(binding.root.context)
                .load(foodImage)
                .into(binding.BuyAgainItemImage)
        }
    }
}