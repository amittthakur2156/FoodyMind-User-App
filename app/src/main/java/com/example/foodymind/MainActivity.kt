package com.example.foodymind

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodymind.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var NavController=findNavController(R.id.fragmentContainerView)
        var BottomNavigation=findViewById<BottomNavigationView>(R.id.BottomNavigation)
        BottomNavigation.setupWithNavController(NavController)
        binding.Notificationbell.setOnClickListener {
            val bottomSheetDailouge= NotificationBottomFragment()
            bottomSheetDailouge.show(supportFragmentManager,"test")
        }
    }
}