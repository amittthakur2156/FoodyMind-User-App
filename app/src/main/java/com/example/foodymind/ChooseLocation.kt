package com.example.foodymind

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodymind.databinding.ActivityChooseLocationBinding

class ChooseLocation : AppCompatActivity() {
    private lateinit var binding: ActivityChooseLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChooseLocationBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
       val locatonList=arrayOf("Agra","Delhi", "Mumbai", "Kolkata", "Chennai", "Bangalore", "Hyderabad", "Pune", "Ahmedabad", "Jaipur", "Lucknow", "Kanpur", "Surat", "Bhopal", "Indore", "Patna", "Chandigarh", "Noida", "Gurgaon", "Agra", "Varanasi", "Prayagraj", "Amritsar", "Jammu", "Srinagar", "Shimla", "Dehradun", "Rishikesh", "Haridwar", "Goa", "Kochi", "Mysore", "Nagpur", "Nashik", "Vadodara", "Raipur", "Ranchi", "Bhubaneswar", "Guwahati", "Shillong", "Imphal", "Aizawl", "Gangtok", "Udaipur", "Jodhpur", "Ajmer", "Aligarh", "Meerut", "Faridabad", "Jabalpur", "Gwalior")
        val adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,locatonList)
        val autoCompleteTextView=binding.listofLocation
        autoCompleteTextView.setAdapter(adapter)
    }
}