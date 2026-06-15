package com.example.foodymind

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodymind.databinding.ActivityDetailBinding
import com.example.foodymind.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModels()

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.result.observe(this) { success ->

            if (success) {

                Toast.makeText(
                    this,
                    "Added to Cart",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Failed to Add",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val foodName = intent.getStringExtra("MenuItemName")
        val foodPrice = intent.getStringExtra("MenuItemPrice")
        val foodImage = intent.getStringExtra("MenuItemImage")
        val foodDescription = intent.getStringExtra("MenuItemDescription")
        val foodIngredient = intent.getStringExtra("MenuItemIngredients")

        binding.FoodName.text = foodName
        binding.Decription.text = foodDescription
        binding.IngredientItems.text = foodIngredient

        Glide.with(this)
            .load(foodImage)
            .into(binding.DetailImage)

        binding.Backbtn.setOnClickListener {
            finish()
        }
        binding.button.setOnClickListener {
            viewModel.addToCart(

                foodName ?: "",
                foodPrice ?: "",
                foodImage ?: "",
                foodDescription ?: "",
                foodIngredient ?: ""

            )
        }
    }
}