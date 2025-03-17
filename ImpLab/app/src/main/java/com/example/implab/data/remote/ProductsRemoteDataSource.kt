package com.example.implab.data.remote

import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class ProductsRemoteDataSource(private val productsAPIService: ProductsAPIService) : RemoteDataSource {
    override suspend fun getAllProducts(): Flow<List<Product>> {
        return flow {
            val response = productsAPIService.getAllProducts()
            emit(response.products)
        }.catch { ex ->
             Response.Error(ex)
        }
    }
}