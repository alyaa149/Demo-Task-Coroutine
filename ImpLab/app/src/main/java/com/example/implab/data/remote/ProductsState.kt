package com.example.implab.data.remote

import com.example.implab.data.models.Product

sealed class Response {
    data class Success(val data: List<Product>) : Response()
    data class Error(val error: Throwable) : Response()
    object Loading : Response()

}