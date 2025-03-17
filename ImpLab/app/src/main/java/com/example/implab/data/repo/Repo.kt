package com.example.implab.data.repo

import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow

interface Repo {
    suspend fun addProduct(product: Product) : Long
    suspend fun deleteProduct(product: Product) :Int
    suspend fun getAllProductsFav(isOnline: Boolean): Flow<List<Product>>?
    suspend fun getAllProducts(isOnline: Boolean): Flow<List<Product>>?


}