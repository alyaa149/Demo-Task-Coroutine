package com.example.implab.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    val retrofitInstance =
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    val service : ProductsAPIService = retrofitInstance.create(ProductsAPIService::class.java)
}