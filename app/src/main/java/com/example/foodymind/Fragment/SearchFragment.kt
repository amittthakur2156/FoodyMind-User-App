package com.example.foodymind.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodymind.Adapter.MenuAdapter
import com.example.foodymind.Model.MenuItemModel
import com.example.foodymind.databinding.FragmentSearchBinding
import com.example.foodymind.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var adapter: MenuAdapter
    private val filteredMenuList = ArrayList<MenuItemModel>()
    private val originalMenuList = ArrayList<MenuItemModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        adapter = MenuAdapter(filteredMenuList, requireContext())
        binding.SearchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.SearchRecyclerView.adapter = adapter

        observeViewModel()
        viewModel.startListening()
        setUpSearchView()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.menuList.observe(viewLifecycleOwner) { list ->
            originalMenuList.clear()
            originalMenuList.addAll(list)

            // Current query maintain karo
            val currentQuery = binding.searchView.query?.toString() ?: ""
            filterMenu(currentQuery)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error ?: return@observe
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMenu(query ?: "")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterMenu(newText ?: "")
                return true
            }
        })
    }

    private fun filterMenu(query: String) {
        filteredMenuList.clear()
        if (query.isEmpty()) {
            filteredMenuList.addAll(originalMenuList)
        } else {
            originalMenuList.filterTo(filteredMenuList) {
                it.foodName?.contains(query, ignoreCase = true) == true
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopListening()
    }
}