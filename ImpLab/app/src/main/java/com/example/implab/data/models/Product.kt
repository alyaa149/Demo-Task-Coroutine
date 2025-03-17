package com.example.implab.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products_table")

data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var description: String,
    var title: String,
    var rating: Double,
    var thumbnail: String,
    var price: Double
) {}