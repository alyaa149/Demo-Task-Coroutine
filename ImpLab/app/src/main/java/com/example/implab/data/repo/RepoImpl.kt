package com.example.implab.data.repo

import com.example.implab.data.local.LocalDataSource
import com.example.implab.data.models.Product
import com.example.implab.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class RepoImpl (
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
):Repo {
    override suspend fun addProduct(product: Product): Long {
    return localDataSource.insertProduct(product)
    }

    override suspend fun deleteProduct(product: Product): Int {
    return localDataSource.deleteProduct(product)
    }

    override suspend fun getAllProductsFav(isOnline: Boolean): Flow<List<Product>>? {
        return localDataSource.getAllProducts()
    }

    override suspend fun getAllProducts(isOnline: Boolean): Flow<List<Product>>?{
        return  remoteDataSource.getAllProducts()
    }



    companion object {
    private var instance: RepoImpl? = null

    fun getInstance(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): RepoImpl {
        return instance ?: synchronized(this) {
            val newInstance = RepoImpl(localDataSource, remoteDataSource)
            instance = newInstance
            newInstance
        }

    }
}
}