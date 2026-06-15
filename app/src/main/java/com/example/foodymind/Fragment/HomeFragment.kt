package com.example.foodymind.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodymind.Adapter.PopularAdapter
import com.example.foodymind.MenuBottomSheetFragment
import com.example.foodymind.R
import com.example.foodymind.databinding.FragmentHomeBinding
import com.example.foodymind.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding= FragmentHomeBinding.inflate(inflater,container, false)
        binding.VIewMore.setOnClickListener {
            val bottomSheetFragment = MenuBottomSheetFragment()
            bottomSheetFragment.show(parentFragmentManager, "Test")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))
        binding.imageSlider.setImageList(imageList, ScaleTypes.FIT)
        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
                val itemMessage = "Double Clicked Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
            override fun onItemSelected(position: Int) {
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.getMenuItems()
        viewModel.menuList.observe(viewLifecycleOwner) { menuList ->

            val foodName = mutableListOf<String>()
            val price = mutableListOf<String>()
            val popularFoodImages = mutableListOf<String>()
            val description = mutableListOf<String>()
            val ingredient = mutableListOf<String>()

            for (item in menuList) {

                foodName.add(item.foodName ?: "")
                price.add(item.foodPrice ?: "")
                popularFoodImages.add(item.foodImage ?: "")
                description.add(item.foodDescription ?: "")
                ingredient.add(item.foodIngredient ?: "")

            }

            val adapter = PopularAdapter(
                foodName,
                price,
                popularFoodImages,
                description,
                ingredient,
                requireContext()
            )

            binding.PopularRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())

            binding.PopularRecyclerView.adapter = adapter

        }
    }
}