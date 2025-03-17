package com.example.implab.data.local

import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insertProduct(product: Product) : Long
    suspend fun deleteProduct(product: Product) :Int
    fun getAllProducts(): Flow<List<Product>>


}
