package com.example.foodymind.Model

data class userModel(

    var uid: String? = "",
    var name: String? = "",
    var email: String? = "",
    var password: String? = "",
    var address: String? = "",
    var phone: String? = "",
    var photoUrl: String? = "",
    var provider: String? = "email"

)