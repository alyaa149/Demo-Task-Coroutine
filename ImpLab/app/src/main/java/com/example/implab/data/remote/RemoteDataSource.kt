package com.example.implab.data.remote

import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getAllProducts(): Flow<List<Product>>?
}