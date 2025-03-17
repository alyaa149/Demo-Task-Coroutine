package com.example.implab.AllProducts.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.implab.data.models.Product
import com.example.implab.data.remote.Response
import com.example.implab.data.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AllProductsViewModel(private val repo: Repo): ViewModel() {
    private val _productsList = MutableStateFlow<Response>(Response.Loading)
    val productsList = _productsList.asStateFlow()

    private val mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    init {
        getAllProducts()
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _productsList.value = Response.Loading
            try {
                repo.getAllProducts(true)
                    ?.catch { ex ->
                        Log.e("ViewModel", "Error fetching products", ex)
                        _productsList.value = Response.Error(ex)
                        mutableMessage.emit(ex.message ?: "Unknown error")
                    }
                    ?.collect { products ->
                        Log.d("ViewModel", "Products fetched: ${products.size}")
                        _productsList.value = Response.Success(products)
                    }
            } catch (e: Exception) {
                Log.e("ViewModel", "Unexpected error", e)
                _productsList.value = Response.Error(e)
                mutableMessage.emit(e.message ?: "Unknown error")
            }
        }
    }


    fun addToFav(product: Product?){
        if(product!=null){
            viewModelScope.launch (Dispatchers.IO) {
                try {
                    val result = repo.addProduct(product)
                    if (result > 0) {
                        Log.i("result",result.toString())
                        Log.i("product",product.toString())
                        mutableMessage.emit("added to fav")
                    }
                }catch (e: Exception){
                    Response.Error(e)
                }
            }


        }
    }
}


class AllProductsFactory(private val repo: Repo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllProductsViewModel(repo) as T

}
}