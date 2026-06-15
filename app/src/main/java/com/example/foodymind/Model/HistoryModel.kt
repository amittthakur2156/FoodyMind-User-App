package com.example.foodymind.Model

data class HistoryModel(

    val foodNames: ArrayList<String> = arrayListOf(),
    val foodPrices: ArrayList<String> = arrayListOf(),
    val foodImages: ArrayList<String> = arrayListOf(),

    val recentFoodName: String = "",
    val recentFoodPrice: String = "",
    val recentFoodImage: String = ""

)