package com.example.implab.FavProducts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.implab.data.models.Product
import com.example.implab.data.remote.Response
import com.example.implab.data.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavProductsViewModel(private val repo: Repo) : ViewModel() {
    // StateFlow for products list
    private val _productsList = MutableStateFlow<Response>(Response.Loading)
    val productsList = _productsList.asStateFlow()


    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    init {
        getFavProducts()
    }

    fun getFavProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _productsList.value = Response.Loading
                val products = repo.getAllProductsFav(isOnline = false)
                products
                    ?.catch { ex ->
                        _productsList.value = Response.Error(ex)
                        _message.value = ex.message
                    }
                    ?.collect { products ->
                        _productsList.value = Response.Success(products)
                    }
            } catch (e: Exception) {
                _message.value = e.message // Update message state
            }
        }
    }

    fun deleteProduct(product: Product?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repo.deleteProduct(product!!)
                if (result > 0) {
                    _message.value = "Deleted from favorites"
                } else {
                    _message.value = "Couldn't delete from favorites, please try again"
                }
            } catch (e: Exception) {
                _message.value = e.message
            }
        }
    }
}
    class FavProductsFactory(private val repo: Repo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavProductsViewModel(repo) as T
        }
    }
