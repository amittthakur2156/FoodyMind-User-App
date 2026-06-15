package com.example.foodymind.repository

import com.example.foodymind.Model.MenuItemModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuRepository {

    private val database =
        FirebaseDatabase.getInstance().reference

    fun getMenuItems(
        callback: (List<MenuItemModel>) -> Unit
    ) {

        database.child("menu")
            .get()
            .addOnSuccessListener { snapshot ->

                val list = mutableListOf<MenuItemModel>()

                for (foodSnapshot in snapshot.children) {

                    val item =
                        foodSnapshot.getValue(
                            MenuItemModel::class.java
                        )

                    item?.foodId = foodSnapshot.key

                    item?.let {

                        list.add(it)

                    }

                }

                callback(list)

            }

    }
    // MenuRepository.kt mein yeh function add karo

    fun searchMenu(
        onUpdate: (ArrayList<MenuItemModel>) -> Unit,
        onError: (String) -> Unit
    ): ValueEventListener {

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<MenuItemModel>()
                for (foodSnapshot in snapshot.children) {
                    val item = foodSnapshot.getValue(MenuItemModel::class.java)
                    if (item != null) {
                        item.foodId = foodSnapshot.key
                        list.add(item)
                    }
                }
                onUpdate(list)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        }

        database.child("menu").addValueEventListener(listener)
        return listener
    }

    fun removeMenuListener(listener: ValueEventListener) {
        database.child("menu").removeEventListener(listener)
    }

}