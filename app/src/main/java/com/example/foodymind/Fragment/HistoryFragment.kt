package com.example.foodymind.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodymind.Adapter.BuyAgainAdapter
import com.example.foodymind.databinding.FragmentHistoryBinding
import com.example.foodymind.viewmodel.HistoryViewModel

class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        binding.HistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        observeViewModel()
        viewModel.loadHistory()

        return binding.root
    }

    private fun observeViewModel() {

        viewModel.historyResult.observe(viewLifecycleOwner) { result ->
            result ?: return@observe

            // Recent Buy UI
            binding.BuyAgainItemFoodName.text = result.recentFoodName
            binding.BuyAgainItemPrice.text = "$" + result.recentFoodPrice
                .replace("$", "")
                .replace("₹", "")

            Glide.with(requireContext())
                .load(result.recentFoodImage)
                .into(binding.BuyAgainItemImage)

            // RecyclerView setup
            val adapter = BuyAgainAdapter(
                result.foodNames,
                result.foodPrices,
                result.foodImages
            ) { position ->
                viewModel.addToCart(
                    foodName = result.foodNames[position],
                    foodPrice = result.foodPrices[position],
                    foodImage = result.foodImages[position]
                )
            }

            binding.HistoryRecyclerView.adapter = adapter
        }

        viewModel.historyError.observe(viewLifecycleOwner) { error ->
            error ?: return@observe
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        viewModel.cartMessage.observe(viewLifecycleOwner) { message ->
            message ?: return@observe
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            viewModel.resetCartMessage()
        }
    }
}