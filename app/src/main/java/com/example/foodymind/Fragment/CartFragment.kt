package com.example.foodymind.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodymind.Adapter.CartAdapter
import com.example.foodymind.PayOutActivity
import com.example.foodymind.databinding.FragmentCartBinding
import com.example.foodymind.viewmodel.CartViewModel

class CartFragment : Fragment() {
    private val viewModel: CartViewModel by viewModels()
    private val foodKeyList = ArrayList<String>()

    private lateinit var binding: FragmentCartBinding

    private val foodNameList = ArrayList<String>()
    private val foodPriceList = ArrayList<String>()
    private val foodImageList = ArrayList<String>()
    private val foodQuantityList = ArrayList<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCartBinding.inflate(inflater, container, false)


        viewModel.getCartItems()
        viewModel.cartItems.observe(viewLifecycleOwner) { menuList ->

            foodNameList.clear()
            foodPriceList.clear()
            foodImageList.clear()
            foodQuantityList.clear()
            foodKeyList.clear()

            for (item in menuList) {

                foodNameList.add(item.foodName ?: "")
                foodPriceList.add(item.foodPrice ?: "")
                foodImageList.add(item.foodImage ?: "")
                foodQuantityList.add(item.foodQuantity ?: 1)
                foodKeyList.add(item.foodId ?: "")

            }

            val adapter = CartAdapter(
                foodNameList,
                foodPriceList,
                foodImageList,
                foodQuantityList,
                foodKeyList
            )

            binding.RecyclerViewCart.layoutManager =
                LinearLayoutManager(requireContext())

            binding.RecyclerViewCart.adapter = adapter

        }

        binding.ProceedBtn.setOnClickListener {

            val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putExtra("FoodItemName", foodNameList)
            intent.putExtra("FoodItemPrice", foodPriceList)
            intent.putExtra("FoodItemImage", foodImageList)
            intent.putExtra("FoodItemQuantity", foodQuantityList)
            startActivity(intent)

        }

        return binding.root
    }
}