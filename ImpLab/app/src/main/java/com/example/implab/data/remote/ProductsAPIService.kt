package com.example.implab.data.remote

import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

interface ProductsAPIService {
    @GET("products")
     suspend fun getAllProducts(): ProductResponse
}