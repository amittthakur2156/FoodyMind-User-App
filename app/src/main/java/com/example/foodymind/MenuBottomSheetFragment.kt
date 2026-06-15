package com.example.foodymind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodymind.Adapter.MenuAdapter
import com.example.foodymind.Model.MenuItemModel
import com.example.foodymind.databinding.FragmentMenuBottomSheetBinding
import com.example.foodymind.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentMenuBottomSheetBinding

    private val menuList = ArrayList<MenuItemModel>()
    private lateinit var adapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.Backbtn.setOnClickListener {
            dismiss()
        }


        adapter = MenuAdapter(menuList, requireContext())

        binding.menuRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.menuRecyclerView.adapter = adapter
        viewModel.getMenuItems()

        viewModel.menuList.observe(viewLifecycleOwner) {

            menuList.clear()

            menuList.addAll(it)

            adapter.notifyDataSetChanged()

        }

        return binding.root
    }

}